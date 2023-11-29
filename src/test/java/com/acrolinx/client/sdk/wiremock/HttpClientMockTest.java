/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.wiremock;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.PLATFORM_PORT_MOCKED;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.httpClientMockNotFoundResponse;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.httpClientMockTimeOut;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.http.AcrolinxResponse;
import com.acrolinx.client.sdk.http.ApacheHttpClient;
import com.acrolinx.client.sdk.http.HttpMethod;
import com.acrolinx.client.sdk.wiremock.common.WireMockUtils;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = PLATFORM_PORT_MOCKED)
class HttpClientMockTest {
  private static final String ACROLINX_URL =
      "http://localhost:" + WireMockUtils.PLATFORM_PORT_MOCKED;

  @Test
  void test404HttpClient() throws URISyntaxException, IOException, AcrolinxException {
    String api = "/api/v1/";
    httpClientMockNotFoundResponse(api);
    ApacheHttpClient apacheHttpClient = new ApacheHttpClient();
    AcrolinxResponse acrolinxResponse =
        apacheHttpClient.fetch(
            new URI(ACROLINX_URL + api), HttpMethod.GET, Collections.emptyMap(), null);

    assertEquals(404, acrolinxResponse.getStatus());
  }

  @Test
  void testTimeout() throws URISyntaxException, IOException, AcrolinxException {
    String api = "/api/v1/";
    httpClientMockTimeOut(api);
    ApacheHttpClient apacheHttpClient = new ApacheHttpClient();
    AcrolinxResponse acrolinxResponse =
        apacheHttpClient.fetch(
            new URI(ACROLINX_URL + api), HttpMethod.GET, Collections.emptyMap(), null);

    assertEquals(408, acrolinxResponse.getStatus());
  }
}
