package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.exceptions.AcrolinxRuntimeException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_SSO_TOKEN;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_USERNAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

public class SignInSsoTest extends IntegrationTestBase {
    @Test
    public void testSignInWithSSO() throws AcrolinxException, ExecutionException, InterruptedException {
        assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_SSO_TOKEN != null);

        SignInSuccess signInSuccess = endpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN, ACROLINX_API_USERNAME).get();
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }

    @Test(expected = AcrolinxRuntimeException.class)
    public void testSignInWithSsoThrowsException() throws AcrolinxException, ExecutionException, InterruptedException {
        endpoint.signInWithSSO("invalidGenericToken", "invalidUserName").get();
    }

    @After
    public void afterTest() throws IOException {
        endpoint.close();
    }
}
