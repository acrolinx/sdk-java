/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_SSO_TOKEN;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_USERNAME;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static com.acrolinx.client.sdk.testutils.TestConstants.DEVELOPMENT_SIGNATURE;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.PLATFORM_PORT_MOCKED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = PLATFORM_PORT_MOCKED)
class SignInSsoTest extends IntegrationTestBase
{
    @Test
    void testSignInWithSSO() throws AcrolinxException
    {
        assertNotNull(ACROLINX_API_USERNAME);
        assertNotNull(ACROLINX_API_SSO_TOKEN);

        SignInSuccess signInSuccess = acrolinxEndpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN, ACROLINX_API_USERNAME);
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }

    @Test
    void testSignInWithSSOIgnoreCase() throws AcrolinxException
    {
        assertNotNull(ACROLINX_API_USERNAME);
        assertNotNull(ACROLINX_API_SSO_TOKEN);

        assertNotEquals("2020.04.22154", acrolinxEndpoint.getPlatformInformation().getServer().getVersion());

        SignInSuccess signInSuccess = acrolinxEndpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN,
                ACROLINX_API_USERNAME.toUpperCase());
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }

    @Test
    void testSignInWithSSOIgnoreCase2() throws AcrolinxException
    {
        assertNotNull(ACROLINX_API_USERNAME);
        assertNotNull(ACROLINX_API_SSO_TOKEN);

        assertNotEquals("2020.04.22154", acrolinxEndpoint.getPlatformInformation().getServer().getVersion());

        SignInSuccess signInSuccess = acrolinxEndpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN,
                ACROLINX_API_USERNAME.toLowerCase());
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }

    @Test
    void testSignInWithSsoThrowsException()
    {
        Assertions.assertThrows(AcrolinxException.class,
                () -> acrolinxEndpoint.signInWithSSO("invalidGenericToken", "invalidUserName"));
    }

    @Test
    void ssoWithUserAndSdkUrl() throws AcrolinxException, URISyntaxException
    {
        assertNotNull(ACROLINX_API_USERNAME);
        assertNotNull(ACROLINX_API_SSO_TOKEN);

        URI realAcrolinxURL = new URI(ACROLINX_URL);
        URI userFacingAcrolinxURL = new URI("https://www.acrolinx.com/proxy");

        acrolinxEndpoint = new AcrolinxEndpoint(realAcrolinxURL, userFacingAcrolinxURL, DEVELOPMENT_SIGNATURE,
                "1.2.3.4", "en");

        SignInSuccess signInSuccess = acrolinxEndpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN, ACROLINX_API_USERNAME);
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }

    @Test
    void ssoWithUserAndSdkUrl2() throws AcrolinxException, URISyntaxException
    {
        assertNotNull(ACROLINX_API_USERNAME);
        assertNotNull(ACROLINX_API_SSO_TOKEN);

        URI realAcrolinxURL = new URI(ACROLINX_URL);
        URI userFacingAcrolinxURL = new URI("https://www.acrolinx.com/proxy/");

        acrolinxEndpoint = new AcrolinxEndpoint(realAcrolinxURL, userFacingAcrolinxURL, DEVELOPMENT_SIGNATURE,
                "1.2.3.4", "en");

        SignInSuccess signInSuccess = acrolinxEndpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN, ACROLINX_API_USERNAME);
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }
}
