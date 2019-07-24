package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.exceptions.SignInException;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.List;

import static com.acrolinx.client.sdk.CommonTestSetup.*;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
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
    public void testSignInWithPollingWithValidAuthToken() throws SignInException {
        final List<String> interactiveUrls = newArrayList();

        SignInSuccess signInSuccess = endpoint.singInInteractive(new InteractiveCallback() {
            @Override
            public void onInteractiveUrl(String url) {
                interactiveUrls.add(url);
            }
        }, ACROLINX_API_TOKEN);

        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }
}
