/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.Progress;
import com.acrolinx.client.sdk.check.*;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.GuidanceProfile;
import com.google.common.base.Strings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class CheckTest extends IntegrationTestBase {
    GuidanceProfile guidanceProfileEn;
    @Mock
    private ProgressListener progressListener;

    @Before
    public void beforeTest() throws AcrolinxException, ExecutionException, InterruptedException {
        assumeTrue(ACROLINX_API_TOKEN != null);
        Capabilities capabilities = endpoint.getCapabilities(ACROLINX_API_TOKEN).get();

        for (GuidanceProfile profile : capabilities.getCheckingCapabilities().getGuidanceProfiles()) {
            if (profile.getLanguage().getId().equals("en")) {
                guidanceProfileEn = profile;
                break;
            }
        }
        assumeTrue(guidanceProfileEn != null);
    }

    public static class ProgressMatcher implements ArgumentMatcher<Progress> {
        private double prevPercent = 0;
        @Override
        public boolean matches(Progress value) {
            boolean valid = value.getPercent() >= this.prevPercent && value.getMessage() != null;
            this.prevPercent = value.getPercent();
            return valid ;
        }
    }

    @Test
    public void check() throws AcrolinxException, InterruptedException, ExecutionException, IOException, URISyntaxException {
        CheckResponse checkResponse = endpoint.check(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent("This textt has ann erroor.")
                        .setDocument(new DocumentDescriptorRequest("file.txt"))
                        .setCheckOptions(new CheckOptions(guidanceProfileEn.getId()))
                        .build()
        ).get();

        assertNotNull(checkResponse);
        assertThat(checkResponse.getData().getId(), not(isEmptyOrNullString()));
        assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
        assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));

        CheckResult checkResult = endpoint.pollForCheckResult(ACROLINX_API_TOKEN, checkResponse, progressListener);

        assertEquals(checkResponse.getData().getId(), checkResult.getId());
        final Quality quality = checkResult.getQuality();
        assertThat(quality.getScore(), lessThan(100));
        assertThat(quality.getScore(), not(lessThan(40)));
        assertNotNull(quality.getStatus());
    }

    @Test
    /**
     * This test might become pretty flaky, when the server is faster than expected. When we notice this problem,
     * we should rewrite it using a mocked server.
     */
    public void checkALargeTextAndGetProgress() throws AcrolinxException, InterruptedException, ExecutionException, IOException, URISyntaxException {
        CheckResponse checkResponse = endpoint.check(ACROLINX_API_TOKEN,
                CheckRequest.ofDocumentContent(Strings.repeat("This sentence is nice. \n", 300))
                        .setDocument(new DocumentDescriptorRequest("file.txt"))
                        .setCheckOptions(new CheckOptions(guidanceProfileEn.getId()))
                        .build()
        ).get();

        CheckResult checkResult = endpoint.pollForCheckResult(ACROLINX_API_TOKEN, checkResponse, progressListener);
        verify(progressListener, atLeast(2)).onProgress(argThat(new ProgressMatcher()));

        assertThat(checkResult.getQuality().getScore(), not(lessThan(40)));
    }
}
