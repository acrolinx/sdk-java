/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_SSO_TOKEN;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_USERNAME;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static com.acrolinx.client.sdk.testutils.TestConstants.DEVELOPMENT_SIGNATURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.SignInSuccess;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;

public class SignInSsoTest extends IntegrationTestBase
{
    @Test
    public void testSignInWithSSO() throws AcrolinxException
    {
        assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_SSO_TOKEN != null);

        SignInSuccess signInSuccess = endpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN, ACROLINX_API_USERNAME);
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }

    @Test
    public void testSignInWithSSOIgnoreCase() throws AcrolinxException
    {
        assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_SSO_TOKEN != null);
        assumeFalse(endpoint.getPlatformInformation().getServer().getVersion().equals("2020.05.23126"));

        SignInSuccess signInSuccess = endpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN, ACROLINX_API_USERNAME.toUpperCase());
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }

    @Test
    public void testSignInWithSSOIgnoreCase2() throws AcrolinxException
    {
        assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_SSO_TOKEN != null);
        assumeFalse(endpoint.getPlatformInformation().getServer().getVersion().equals("2020.05.23126"));

        SignInSuccess signInSuccess = endpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN, ACROLINX_API_USERNAME.toLowerCase());
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }

    @Test(expected = AcrolinxException.class)
    public void testSignInWithSsoThrowsException() throws AcrolinxException
    {
        endpoint.signInWithSSO("invalidGenericToken", "invalidUserName");
    }

    @Test
    public void ssoWithUserAndSdkUrl() throws AcrolinxException, URISyntaxException
    {
        assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_SSO_TOKEN != null);

        URI realAcrolinxURL = new URI(ACROLINX_URL);
        URI userFacingAcrolinxURL = new URI("https://www.acrolinx.com/proxy");

        endpoint = new AcrolinxEndpoint(realAcrolinxURL, userFacingAcrolinxURL, DEVELOPMENT_SIGNATURE, "1.2.3.4", "en");

        SignInSuccess signInSuccess = endpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN, ACROLINX_API_USERNAME);
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }

    @Test
    public void ssoWithUserAndSdkUrl2() throws AcrolinxException, URISyntaxException
    {
        assumeTrue(ACROLINX_API_USERNAME != null && ACROLINX_API_SSO_TOKEN != null);

        URI realAcrolinxURL = new URI(ACROLINX_URL);
        URI userFacingAcrolinxURL = new URI("https://www.acrolinx.com/proxy/");

        endpoint = new AcrolinxEndpoint(realAcrolinxURL, userFacingAcrolinxURL, DEVELOPMENT_SIGNATURE, "1.2.3.4", "en");

        SignInSuccess signInSuccess = endpoint.signInWithSSO(ACROLINX_API_SSO_TOKEN, ACROLINX_API_USERNAME);
        assertEquals(ACROLINX_API_USERNAME, signInSuccess.getUser().getUsername());
    }
}
