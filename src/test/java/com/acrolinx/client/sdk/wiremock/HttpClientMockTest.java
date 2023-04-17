/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.wiremock;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.PLATFORM_PORT_MOCKED;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.httpClientMockNotFoundResponse;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.httpClientMockTimeOut;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.http.AcrolinxResponse;
import com.acrolinx.client.sdk.http.ApacheHttpClient;
import com.acrolinx.client.sdk.http.HttpMethod;
import com.acrolinx.client.sdk.wiremock.common.MockedTestBase;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

@WireMockTest(httpPort = PLATFORM_PORT_MOCKED)
class HttpClientMockTest extends MockedTestBase
{
    private static final int PLATFORM_PORT_MOCKED = 8089;
    private static final String acrolinxUrl = "http://localhost:" + PLATFORM_PORT_MOCKED;

    @Test
    void test404HttpClient() throws URISyntaxException, IOException, AcrolinxException
    {
        String api = "/api/v1/";
        httpClientMockNotFoundResponse(api);
        HashMap<String, String> headersHashMap = new HashMap<>();
        ApacheHttpClient apacheHttpClient = new ApacheHttpClient();
        AcrolinxResponse acrolinxResponse = apacheHttpClient.fetch(new URI(acrolinxUrl + api), HttpMethod.GET,
                headersHashMap, null);

        assertEquals(404, acrolinxResponse.getStatus());
    }

    @Test
    void testTimeout() throws URISyntaxException, IOException, AcrolinxException
    {
        String api = "/api/v1/";
        httpClientMockTimeOut(api);
        HashMap<String, String> headersHashMap = new HashMap<>();
        ApacheHttpClient apacheHttpClient = new ApacheHttpClient();
        AcrolinxResponse acrolinxResponse = apacheHttpClient.fetch(new URI(acrolinxUrl + api), HttpMethod.GET,
                headersHashMap, null);

        assertEquals(408, acrolinxResponse.getStatus());
    }
}
