package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.exceptions.SSOException;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;

import static com.acrolinx.client.sdk.CommonTestSetup.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

public class SignInSsoTest {
    private AcrolinxEndpoint endpoint;

    @Before
    public void beforeTest() throws URISyntaxException {
        assumeTrue(ACROLINX_URL != null);
        assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_SSO_TOKEN != null);
        endpoint = createTestAcrolinxEndpoint();
    }

    @Test
    public void testSignInWithSSO() throws SSOException {
        SignInSuccess signInSuccess = endpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN, ACROLINX_API_USERNAME);
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }
}
