/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.wiremock.common;

import static com.acrolinx.client.sdk.testutils.TestConstants.DEVELOPMENT_SIGNATURE;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.internal.JsonUtils;
import com.acrolinx.client.sdk.internal.SuccessResponse;
import java.net.URI;
import java.net.URISyntaxException;

public final class WireMockUtils {
  public static final int PLATFORM_PORT_MOCKED = 8089;
  private static final String ACROLINX_URL = "http://localhost:" + PLATFORM_PORT_MOCKED;
  private static final String API_PATH_PREFIX = "/api/v1/";

  public static AcrolinxEndpoint createTestAcrolinxEndpoint() throws URISyntaxException {
    return new AcrolinxEndpoint(new URI(ACROLINX_URL), DEVELOPMENT_SIGNATURE, "1.2.3.4", "en");
  }

  public static <T> void mockSuccessResponse(String path, T data) {
    mockSuccessResponseWithDelay(path, data, 0);
  }

  public static void httpClientMockTimeOut(String path) {
    httpClientMockTimeOutwithDelay(path, 5_000);
  }

  private static void httpClientMockTimeOutwithDelay(String path, int delayMs) {
    stubFor(
        get(urlEqualTo(path))
            .willReturn(
                aResponse()
                    .withStatus(408)
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        "{\n"
                            + "\"statusCode\": 408,\n"
                            + "\"description\": \"Request Timed Out\"\n"
                            + "}")
                    .withFixedDelay(delayMs)));
  }

  public static void httpClientMockNotFoundResponse(String path) {
    httpClientMockNotFoundResponsewithDelay(path, 0);
  }

  private static void httpClientMockNotFoundResponsewithDelay(String path, int delayMs) {
    stubFor(
        get(urlEqualTo(path))
            .willReturn(
                aResponse()
                    .withStatus(404)
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        "{\n" + "\"statusCode\": 404,\n" + "\"description\": \"Not Found\"\n" + "}")
                    .withFixedDelay(delayMs)));
  }

  private static <T> void mockSuccessResponseWithDelay(String path, T data, int delayMs) {
    stubFor(
        get(urlEqualTo(API_PATH_PREFIX + path))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(JsonUtils.toJson(new SuccessResponse<>(data)))
                    .withFixedDelay(delayMs)));
  }

  public static <T> void mockPostResponse(String path, T response) {
    stubFor(
        post(urlEqualTo(API_PATH_PREFIX + path)).willReturn(okJson(JsonUtils.toJson(response))));
  }

  public static <T> void mockSuccessResponseInScenario(
      String path, T successData, String scenario, String requiredState) {
    stubFor(
        get(urlEqualTo(API_PATH_PREFIX + path))
            .inScenario(scenario)
            .whenScenarioStateIs(requiredState)
            .willReturn(okJson(JsonUtils.toJson(new SuccessResponse<>(successData)))));
  }

  public static <T> void mockGetResponseInScenario(
      String path, T response, String scenario, String requiredState, String nextState) {
    stubFor(
        get(urlEqualTo(API_PATH_PREFIX + path))
            .inScenario(scenario)
            .whenScenarioStateIs(requiredState)
            .willSetStateTo(nextState)
            .willReturn(okJson(JsonUtils.toJson(response))));
  }

  public static String mockUrlOfApiPath(String apiPath) {
    return ACROLINX_URL + API_PATH_PREFIX + apiPath;
  }

  private WireMockUtils() {
    throw new IllegalStateException();
  }
}
