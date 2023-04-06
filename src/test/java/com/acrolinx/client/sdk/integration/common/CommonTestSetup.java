/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration.common;

import static com.acrolinx.client.sdk.testutils.TestConstants.DEVELOPMENT_SIGNATURE;

import java.net.URI;
import java.net.URISyntaxException;

import com.acrolinx.client.sdk.AccessToken;
import com.acrolinx.client.sdk.AcrolinxEndpoint;

import io.github.cdimascio.dotenv.Dotenv;

public final class CommonTestSetup
{
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    public static final String ACROLINX_URL = dotenv.get("ACROLINX_URL");

    public static final String ACROLINX_API_SSO_TOKEN = dotenv.get("ACROLINX_API_SSO_TOKEN");
    public static final String ACROLINX_API_USERNAME = dotenv.get("ACROLINX_API_USERNAME");
    public static final String ACROLINX_API_TOKEN_STRING = dotenv.get("ACROLINX_API_TOKEN");
    public static final AccessToken ACROLINX_API_TOKEN = ACROLINX_API_TOKEN_STRING != null
            ? new AccessToken(ACROLINX_API_TOKEN_STRING)
            : null;

    public static AcrolinxEndpoint createTestAcrolinxEndpoint() throws URISyntaxException
    {
        return new AcrolinxEndpoint(new URI(ACROLINX_URL), DEVELOPMENT_SIGNATURE, "1.2.3.4", "en");
    }
}
