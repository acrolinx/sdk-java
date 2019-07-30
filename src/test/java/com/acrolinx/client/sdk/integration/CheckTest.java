package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.check.*;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.platform.Capabilities;
import com.acrolinx.client.sdk.platform.GuidanceProfile;
import org.junit.Before;
import org.junit.Test;

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

public class CheckTest extends IntegrationTestBase {
    GuidanceProfile guidanceProfileEn;

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

        CheckResult checkResult = endpoint.pollForCheckResult(ACROLINX_API_TOKEN, checkResponse);

        assertEquals(checkResponse.getData().getId(), checkResult.getId());
        final Quality quality = checkResult.getQuality();
        assertThat(quality.getScore(), lessThan(100));
        assertThat(quality.getScore(), not(lessThan(40)));
        assertNotNull(quality.getStatus());
    }
}
