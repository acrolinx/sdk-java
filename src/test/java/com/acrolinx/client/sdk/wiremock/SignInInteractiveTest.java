/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.wiremock;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.acrolinx.client.sdk.InteractiveCallback;
import com.acrolinx.client.sdk.Progress;
import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.User;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.internal.SignInPollResponse;
import com.acrolinx.client.sdk.internal.SignInResponse;
import com.acrolinx.client.sdk.platform.configuration.Integration;
import com.acrolinx.client.sdk.wiremock.common.MockedTestBase;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class SignInInteractiveTest extends MockedTestBase
{
    static final String mockedInteractiveUrl = mockUrlOfApiPath("auth/sign-ins/interactive");
    static final String mockedPollPath = "auth/sign-ins/poll";
    static String mockedPollUrl = mockUrlOfApiPath(mockedPollPath);

    @Mock
    private InteractiveCallback interactiveCallback;

    SignInSuccess expectedSignInSuccess = new SignInSuccess("dummyAccessToken", new User("userId", "username"),
            new Integration(Maps.<String, String> newHashMap()),
            SignInSuccess.AuthorizationType.ACROLINX_SIGN_IN.toString());

    SignInPollResponse.Progress mockedSignInPollMoreResponse = new SignInPollResponse.Progress(
            new Progress(1.0, null, null));

    SignInResponse.SignInLinks mockedSignInLinksResponse = new SignInResponse.SignInLinks(
            new SignInResponse.SignInLinksInternal(mockedInteractiveUrl, mockedPollUrl),
            new SignInResponse.SignInLinksData(10000.0));

    @Before
    public void beforeTest()
    {
        mockPostResponse("auth/sign-ins", mockedSignInLinksResponse);
    }

    @Test
    public void signInWithImmediatePollSuccess() throws AcrolinxException, InterruptedException
    {
        mockSuccessResponse(mockedPollPath, expectedSignInSuccess);

        SignInSuccess signInSuccess = endpoint.signInInteractive(interactiveCallback);

        verify(interactiveCallback).onInteractiveUrl(mockedInteractiveUrl);
        assertEquals(expectedSignInSuccess.getAccessToken(), signInSuccess.getAccessToken());
    }

    @Test
    public void signInWithLaterPollSuccess() throws AcrolinxException, InterruptedException
    {
        final String scenario = "laterPollSuccess";
        final String SIGNED_IN_STATE = "SIGNED_IN_STATE";

        mockGetResponseInScenario(mockedPollPath, mockedSignInPollMoreResponse, scenario, Scenario.STARTED,
                SIGNED_IN_STATE);
        mockSuccessResponseInScenario(mockedPollPath, expectedSignInSuccess, scenario, SIGNED_IN_STATE);

        SignInSuccess signInSuccess = endpoint.signInInteractive(interactiveCallback);

        verify(interactiveCallback).onInteractiveUrl(mockedInteractiveUrl);
        assertEquals(expectedSignInSuccess.getAccessToken().getAccessToken(),
                signInSuccess.getAccessToken().getAccessToken());
    }
}
