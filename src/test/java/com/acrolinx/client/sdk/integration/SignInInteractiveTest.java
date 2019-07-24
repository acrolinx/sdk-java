package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.InteractiveCallback;
import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.exceptions.SignInException;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.List;

import static com.acrolinx.client.sdk.integration.CommonTestSetup.*;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class SignInInteractiveTest {
    private AcrolinxEndpoint endpoint;

    @Before
    public void beforeTest() throws URISyntaxException {
        assumeTrue(ACROLINX_URL != null);
        assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_TOKEN != null);
        endpoint = createTestAcrolinxEndpoint();
    }

    @Test
    public void testSignInWithPollingCallsCallback() throws SignInException {
        final List<String> interactiveUrls = newArrayList();
        long timeoutMs = 100;

        try {
            endpoint.singInInteractive(new InteractiveCallback() {
                @Override
                public void onInteractiveUrl(String url) {
                    interactiveUrls.add(url);
                }
            }, null, timeoutMs);
            fail("It should fail due to timeout.");
        } catch (SignInException e) {
            assertEquals(interactiveUrls.size(), 1);
            assertTrue(interactiveUrls.get(0).startsWith("http"));
        }

    }

    @Test
    public void testSignInWithPollingWithValidAuthToken() throws SignInException {
        final List<String> interactiveUrls = newArrayList();

        SignInSuccess signInSuccess = endpoint.singInInteractive(new InteractiveCallback() {
            @Override
            public void onInteractiveUrl(String url) {
                interactiveUrls.add(url);
            }
        }, ACROLINX_API_TOKEN);

        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
        assertEquals(interactiveUrls.size(), 0);
    }
}
