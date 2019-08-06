/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.AccessToken;
import com.acrolinx.client.sdk.InteractiveCallback;
import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.check.CheckResult;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
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
    public void testSignInWithNoAccessToken() throws AcrolinxException, InterruptedException {
        try {
            SignInSuccess ss = endpoint.signInInteractive(interactiveCallback, null, 400L);
            fail("It should fail due to timeout.");
        } catch (SignInException e) {
            assertEquals("Timeout", e.getMessage());
            verify(interactiveCallback, times(1)).onInteractiveUrl(ArgumentMatchers.startsWith(ACROLINX_URL));
        }
    }

    @Test
    public void testSignInWithPollingWithValidAccessToken() throws AcrolinxException, InterruptedException {
        SignInSuccess signInSuccess = endpoint.signInInteractive(interactiveCallback, ACROLINX_API_TOKEN, 500L);

        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
        verifyZeroInteractions(interactiveCallback);
    }

    @Test
    public void testSignInWithPollingWithInvalidAccessToken() throws ExecutionException, InterruptedException, AcrolinxException {
        try {
            SignInSuccess ss = endpoint.signInInteractive(interactiveCallback, new AccessToken("accesstoken"), 1000L);
            fail("It should fail due to timeout.");
        } catch (SignInException e) {
            assertEquals("Timeout", e.getMessage());
            verify(interactiveCallback, times(1)).onInteractiveUrl(ArgumentMatchers.startsWith(ACROLINX_URL));
        }
    }

    @Test(expected = CancellationException.class)
    public void testSignCancel() throws ExecutionException, InterruptedException, TimeoutException {
        final long timeoutMs = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<SignInSuccess> future = executorService.submit(new Callable<SignInSuccess>() {
            @Override
            public SignInSuccess call() throws Exception {
                return endpoint.signInInteractive(interactiveCallback, ACROLINX_API_TOKEN, timeoutMs);
            }
        });

        boolean cancelled = future.cancel(true);
        assertTrue("User cancelled", cancelled);
        future.get(timeoutMs, TimeUnit.MILLISECONDS);
        fail("It should throw cancellation exception");
    }

}
