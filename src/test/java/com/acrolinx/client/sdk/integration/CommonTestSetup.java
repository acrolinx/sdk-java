package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.AccessToken;
import com.acrolinx.client.sdk.AcrolinxEndpoint;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.URISyntaxException;

import static com.acrolinx.client.sdk.testutils.TestConstants.DEVELOPMENT_SIGNATURE;

public class CommonTestSetup {
    static Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    public static final String ACROLINX_URL = dotenv.get("ACROLINX_URL");

    static String ACROLINX_API_SSO_TOKEN = dotenv.get("ACROLINX_API_SSO_TOKEN");
    static String ACROLINX_API_USERNAME = dotenv.get("ACROLINX_API_USERNAME");
    static String ACROLINX_API_TOKEN_STRING = dotenv.get("ACROLINX_API_TOKEN");
    static AccessToken ACROLINX_API_TOKEN = ACROLINX_API_TOKEN_STRING != null ? new AccessToken(ACROLINX_API_TOKEN_STRING) : null;

    public static AcrolinxEndpoint createTestAcrolinxEndpoint() throws URISyntaxException {
        return new AcrolinxEndpoint(new URI(ACROLINX_URL), DEVELOPMENT_SIGNATURE, "1.2.3.4", "en");
    }

}
