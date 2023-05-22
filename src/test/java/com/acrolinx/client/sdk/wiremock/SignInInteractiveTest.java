/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.wiremock;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.PLATFORM_PORT_MOCKED;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockGetResponseInScenario;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockPostResponse;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockSuccessResponse;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockSuccessResponseInScenario;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockUrlOfApiPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.acrolinx.client.sdk.InteractiveCallback;
import com.acrolinx.client.sdk.Progress;
import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.User;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.internal.SignInPollResponse;
import com.acrolinx.client.sdk.internal.SignInResponse;
import com.acrolinx.client.sdk.platform.configuration.Integration;
import com.acrolinx.client.sdk.wiremock.common.WireMockUtils;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.google.common.collect.Maps;

@WireMockTest(httpPort = PLATFORM_PORT_MOCKED)
class SignInInteractiveTest
{
    private static final String MOCKED_INTERACTIVE_URL = mockUrlOfApiPath("auth/sign-ins/interactive");
    private static final String MOCKED_POLL_PATH = "auth/sign-ins/poll";
    private static final String MOCKED_POLL_URL = mockUrlOfApiPath(MOCKED_POLL_PATH);

    private final InteractiveCallback interactiveCallback = Mockito.mock(InteractiveCallback.class);
    private final SignInSuccess expectedSignInSuccess = new SignInSuccess("dummyAccessToken",
            new User("userId", "username"), new Integration(Maps.<String, String> newHashMap()),
            SignInSuccess.AuthorizationType.ACROLINX_SIGN_IN.toString());
    private final SignInPollResponse.Progress mockedSignInPollMoreResponse = new SignInPollResponse.Progress(
            new Progress(1.0, null, null));
    private final SignInResponse.SignInLinks mockedSignInLinksResponse = new SignInResponse.SignInLinks(
            new SignInResponse.SignInLinksInternal(MOCKED_INTERACTIVE_URL, MOCKED_POLL_URL),
            new SignInResponse.SignInLinksData(10000.0));

    @BeforeEach
    void beforeEach()
    {
        mockPostResponse("auth/sign-ins", mockedSignInLinksResponse);
    }

    @Test
    void signInWithImmediatePollSuccess() throws AcrolinxException, InterruptedException, URISyntaxException
    {
        mockSuccessResponse(MOCKED_POLL_PATH, expectedSignInSuccess);

        SignInSuccess signInSuccess = WireMockUtils.createTestAcrolinxEndpoint().signInInteractive(interactiveCallback);

        verify(interactiveCallback).onInteractiveUrl(MOCKED_INTERACTIVE_URL);
        assertEquals(expectedSignInSuccess.getAccessToken(), signInSuccess.getAccessToken());
    }

    @Test
    void signInWithLaterPollSuccess() throws AcrolinxException, InterruptedException, URISyntaxException
    {
        final String scenario = "laterPollSuccess";
        final String signedInState = "SIGNED_IN_STATE";

        mockGetResponseInScenario(MOCKED_POLL_PATH, mockedSignInPollMoreResponse, scenario, Scenario.STARTED,
                signedInState);
        mockSuccessResponseInScenario(MOCKED_POLL_PATH, expectedSignInSuccess, scenario, signedInState);

        SignInSuccess signInSuccess = WireMockUtils.createTestAcrolinxEndpoint().signInInteractive(interactiveCallback);

        verify(interactiveCallback).onInteractiveUrl(MOCKED_INTERACTIVE_URL);
        assertEquals(expectedSignInSuccess.getAccessToken().getAccessTokenAsString(),
                signInSuccess.getAccessToken().getAccessTokenAsString());
    }
}
