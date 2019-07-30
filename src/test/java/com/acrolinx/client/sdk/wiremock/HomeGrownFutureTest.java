package com.acrolinx.client.sdk.wiremock;

import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.User;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.platform.configuration.Integration;
import com.acrolinx.client.sdk.wiremock.common.MockedTestBase;
import com.acrolinx.client.sdk.wiremock.common.WireMockUtils;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests our homegrown Futures using the example of signInWithSSO
 */
public class HomeGrownFutureTest extends MockedTestBase {
    static final int DELAY_MS = 200;

    final SignInSuccess expectedSignInSuccess = new SignInSuccess("dummyAccessToken", new User("userId", "username"),
            new Integration(Maps.<String, String>newHashMap()),
            SignInSuccess.AuthorizationType.ACROLINX_SSO.toString());

    @Before
    public void beforeTest() {
        WireMockUtils.mockPostSuccessResponseWithDelay("auth/sign-ins", expectedSignInSuccess, DELAY_MS);
    }

    @Test
    public void get_should_get_the_result() throws AcrolinxException, InterruptedException, ExecutionException {
        Future<SignInSuccess> future = endpoint.signInWithSSO("genericToken", "username");
        SignInSuccess signInSuccess = future.get();
        assertEquals(expectedSignInSuccess.getAccessToken(), signInSuccess.getAccessToken());
    }

    @Test(timeout = 1000)
    public void isDone_should_be_done_at_some_point_without_calling_get() throws AcrolinxException, InterruptedException, ExecutionException {
        Future<SignInSuccess> future = endpoint.signInWithSSO("genericToken", "username");

        while (!future.isDone()) {
            Thread.sleep(100);
        }

        SignInSuccess signInSuccess = future.get();
        assertEquals(expectedSignInSuccess.getAccessToken(), signInSuccess.getAccessToken());
    }

    /**
     * Future specs: https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Future.html
     * {@link ApacheHttpClientFutureTest}
     */
    @Test(expected = CancellationException.class)
    public void cancel_should_really_cancel() throws AcrolinxException, InterruptedException, ExecutionException {
        Future<SignInSuccess> future = endpoint.signInWithSSO("genericToken", "username");

        assertFalse(future.isDone());
        boolean cancelResult = future.cancel(true);
        // Caused by internal future of ApacheHttpClientFuture
        assertFalse("actually cancelResult should be true, but we assert false because this is strange reality", cancelResult);

        assertTrue(future.isCancelled());
        assertTrue("A canceled Future should be also \"done\" according to the Future specs", future.isDone());

        // Here we expect a CancellationException according to the Future specs
        future.get();
    }
}
