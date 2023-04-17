/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.wiremock;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.PLATFORM_PORT_MOCKED;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockGetResponseInScenario;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockPostResponse;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockSuccessResponse;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockSuccessResponseInScenario;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockUrlOfApiPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

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
import com.acrolinx.client.sdk.wiremock.common.MockedTestBase;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.google.common.collect.Maps;

@WireMockTest(httpPort = PLATFORM_PORT_MOCKED)
class SignInInteractiveTest extends MockedTestBase
{
    private static final String mockedInteractiveUrl = mockUrlOfApiPath("auth/sign-ins/interactive");
    private static final String mockedPollPath = "auth/sign-ins/poll";
    private static final String mockedPollUrl = mockUrlOfApiPath(mockedPollPath);

    private final InteractiveCallback interactiveCallback = Mockito.mock(InteractiveCallback.class);
    private final SignInSuccess expectedSignInSuccess = new SignInSuccess("dummyAccessToken",
            new User("userId", "username"), new Integration(Maps.<String, String> newHashMap()),
            SignInSuccess.AuthorizationType.ACROLINX_SIGN_IN.toString());
    private final SignInPollResponse.Progress mockedSignInPollMoreResponse = new SignInPollResponse.Progress(
            new Progress(1.0, null, null));
    private final SignInResponse.SignInLinks mockedSignInLinksResponse = new SignInResponse.SignInLinks(
            new SignInResponse.SignInLinksInternal(mockedInteractiveUrl, mockedPollUrl),
            new SignInResponse.SignInLinksData(10000.0));

    @BeforeEach
    void beforeEach()
    {
        mockPostResponse("auth/sign-ins", mockedSignInLinksResponse);
    }

    @Test
    void signInWithImmediatePollSuccess() throws AcrolinxException, InterruptedException
    {
        mockSuccessResponse(mockedPollPath, expectedSignInSuccess);

        SignInSuccess signInSuccess = acrolinxEndpoint.signInInteractive(interactiveCallback);

        verify(interactiveCallback).onInteractiveUrl(mockedInteractiveUrl);
        assertEquals(expectedSignInSuccess.getAccessToken(), signInSuccess.getAccessToken());
    }

    @Test
    void signInWithLaterPollSuccess() throws AcrolinxException, InterruptedException
    {
        final String scenario = "laterPollSuccess";
        final String SIGNED_IN_STATE = "SIGNED_IN_STATE";

        mockGetResponseInScenario(mockedPollPath, mockedSignInPollMoreResponse, scenario, Scenario.STARTED,
                SIGNED_IN_STATE);
        mockSuccessResponseInScenario(mockedPollPath, expectedSignInSuccess, scenario, SIGNED_IN_STATE);

        SignInSuccess signInSuccess = acrolinxEndpoint.signInInteractive(interactiveCallback);

        verify(interactiveCallback).onInteractiveUrl(mockedInteractiveUrl);
        assertEquals(expectedSignInSuccess.getAccessToken().getAccessTokenAsString(),
                signInSuccess.getAccessToken().getAccessTokenAsString());
    }
}
