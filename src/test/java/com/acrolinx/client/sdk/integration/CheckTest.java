package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.check.CheckRequest;
import com.acrolinx.client.sdk.check.CheckResponse;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

public class CheckTest extends IntegrationTestBase {
    @Before
    public void beforeTest() {
        assumeTrue(ACROLINX_API_TOKEN != null);
    }

    @Test
    public void check() throws AcrolinxException, InterruptedException, ExecutionException {
        CheckResponse checkResponse = endpoint.check(ACROLINX_API_TOKEN, new CheckRequest("This textt has ann erroor.", null, null, null)).get();

        assertNotNull(checkResponse);
        assertThat(checkResponse.getData().getId(), not(isEmptyOrNullString()));
        assertThat(checkResponse.getLinks().getResult(), startsWith(ACROLINX_URL));
        assertThat(checkResponse.getLinks().getCancel(), startsWith(ACROLINX_URL));
    }
}
