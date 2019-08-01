/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.AccessToken;
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

import java.util.concurrent.*;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.*;
import static org.junit.Assert.*;
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
    public void testSignInWithNoAccessToken() throws SignInException, InterruptedException, ExecutionException, TimeoutException {
        try {
            Future<SignInSuccess> ss = endpoint.signInInteractive(interactiveCallback, null);
            ss.get(400, TimeUnit.MILLISECONDS);
            fail("It should fail due to timeout.");
        } catch (TimeoutException e) {
            verify(interactiveCallback, times(1)).onInteractiveUrl(ArgumentMatchers.startsWith(ACROLINX_URL));
        }
    }

    @Test
    public void testSignInWithPollingWithValidAccessToken() throws SignInException, ExecutionException, InterruptedException {
        SignInSuccess signInSuccess = endpoint.signInInteractive(interactiveCallback, null, ACROLINX_API_TOKEN, 500L).get();

        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
        verifyZeroInteractions(interactiveCallback);
    }

    @Test
    public void testSignInWithPollingWithInvalidAccessToken() throws ExecutionException, InterruptedException {
        try {
            Future<SignInSuccess> ss = endpoint.signInInteractive(interactiveCallback, null, new AccessToken("accesstoken"), 1000L);
            ss.get(500, TimeUnit.MILLISECONDS);
            fail("It should fail due to timeout.");
        } catch (TimeoutException e) {
            verify(interactiveCallback, times(1)).onInteractiveUrl(ArgumentMatchers.startsWith(ACROLINX_URL));
        }
    }

    @Test(expected = CancellationException.class)
    public void testSignCancel() throws ExecutionException, InterruptedException, TimeoutException {
        long timeoutMs = 1000;
        Future<SignInSuccess> ss = endpoint.signInInteractive(interactiveCallback, null, ACROLINX_API_TOKEN, timeoutMs);
        boolean cancelled = ss.cancel(true);
        assertTrue("User cancelled", cancelled);
        ss.get(timeoutMs, TimeUnit.MILLISECONDS);
        fail("It should throw cancellation exception");
    }

    @Test
    public void testUpperTimeLimit() throws InterruptedException {
        Throwable ex = null;
        try {
            Future<SignInSuccess> ss = endpoint.signInInteractive(interactiveCallback, null, null, 1000L);
            ss.get();
            fail("Sign In exception expected");
        } catch (ExecutionException e) {
            ex = e.getCause();
        }

        assertTrue("Cancellation exception", (ex instanceof SignInException));

    }
}
