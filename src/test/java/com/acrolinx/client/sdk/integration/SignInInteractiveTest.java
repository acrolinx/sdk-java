package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.InteractiveCallback;
import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.exceptions.SignInException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class SignInInteractiveTest extends IntegrationTestBase {
    @Mock
    private InteractiveCallback interactiveCallback;

    @Before
    public void beforeTest() {
        assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_TOKEN != null);
    }

    @Test
    public void testSignInWithPollingCallsCallback() throws SignInException {
        long timeoutMs = 100;

        try {
            endpoint.signInInteractive(interactiveCallback, null, timeoutMs);
            fail("It should fail due to timeout.");
        } catch (SignInException e) {
            verify(interactiveCallback, times(1)).onInteractiveUrl(ArgumentMatchers.startsWith(ACROLINX_URL));
        }
    }

    @Test
    public void testSignInWithPollingWithValidAuthToken() throws SignInException {
        SignInSuccess signInSuccess = endpoint.signInInteractive(interactiveCallback, ACROLINX_API_TOKEN);

        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
        verifyZeroInteractions(interactiveCallback);
    }
}
