/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.wiremock.common;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.internal.JsonUtils;
import com.acrolinx.client.sdk.internal.SuccessResponse;

import java.net.URI;
import java.net.URISyntaxException;

import static com.acrolinx.client.sdk.testutils.TestConstants.DEVELOPMENT_SIGNATURE;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockUtils {
    public static final int PLATFORM_PORT_MOCKED = 8089;
    public static final String acrolinxUrl = "http://localhost:" + PLATFORM_PORT_MOCKED;
    public static final String API_PATH_PREFIX = "/api/v1/";

    public static AcrolinxEndpoint createTestAcrolinxEndpointMocked() throws URISyntaxException {
        return new AcrolinxEndpoint(new URI(acrolinxUrl), DEVELOPMENT_SIGNATURE, "1.2.3.4", "en");
    }

    public static <T> void mockSuccessResponse(String path, T data) {
        mockSuccessResponseWithDelay(path, data, 0);
    }

    public static <T> void mockSuccessResponseWithDelay(String path, T data, int delayMs) {
        stubFor(get(urlEqualTo(API_PATH_PREFIX + path))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(JsonUtils.toJson(new SuccessResponse(data)))
                        .withFixedDelay(delayMs)));
    }

    public static <T> void mockPostSuccessResponseWithDelay(String path, T data, int delayMs) {
        stubFor(post(urlEqualTo(API_PATH_PREFIX + path))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(JsonUtils.toJson(new SuccessResponse(data)))
                        .withFixedDelay(delayMs)));
    }

    public static <T> void mockPostResponse(String path, T response) {
        stubFor(post(urlEqualTo(API_PATH_PREFIX + path)).willReturn(okJson(JsonUtils.toJson(response))));
    }

    public static <T> void mockSuccessResponseInScenario(String path, T successData, String scenario, String requiredState) {
        stubFor(get(urlEqualTo(API_PATH_PREFIX + path))
                .inScenario(scenario)
                .whenScenarioStateIs(requiredState)
                .willReturn(okJson(JsonUtils.toJson(new SuccessResponse(successData)))));
    }

    public static <T> void mockGetResponseInScenario(String path, T response, String scenario, String requiredState, String nextState) {
        stubFor(get(urlEqualTo(API_PATH_PREFIX + path))
                .inScenario(scenario)
                .whenScenarioStateIs(requiredState)
                .willSetStateTo(nextState)
                .willReturn(okJson(JsonUtils.toJson(response))));
    }

    public static String mockUrlOfApiPath(String apiPath) {
        return acrolinxUrl + API_PATH_PREFIX + apiPath;
    }
}
