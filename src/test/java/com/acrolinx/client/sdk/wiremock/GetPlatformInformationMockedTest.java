/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.wiremock;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.PLATFORM_PORT_MOCKED;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockSuccessResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URISyntaxException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.PlatformInformation;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.platform.Server;
import com.acrolinx.client.sdk.wiremock.common.WireMockUtils;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.common.collect.Lists;
import com.google.gson.JsonSyntaxException;

@WireMockTest(httpPort = PLATFORM_PORT_MOCKED)
class GetPlatformInformationMockedTest
{
    @Test
    void testFetchingPlatformInformationSucces() throws AcrolinxException, URISyntaxException
    {
        PlatformInformation expectedPlatformInformation = new PlatformInformation(new Server("2018.12", "Old Server"),
                Lists.newArrayList("en"));
        mockSuccessResponse("", expectedPlatformInformation);

        PlatformInformation platformInformation = WireMockUtils.createTestAcrolinxEndpoint().getPlatformInformation();

        assertEquals(expectedPlatformInformation.getServer().getName(), platformInformation.getServer().getName());
        assertEquals(expectedPlatformInformation.getServer().getVersion(),
                platformInformation.getServer().getVersion());
        assertEquals(expectedPlatformInformation.getLocales(), platformInformation.getLocales());
    }

    @Test
    void testFetchingPlatformInformationFailure() throws URISyntaxException
    {
        mockSuccessResponse("", "Wrong Result");

        AcrolinxEndpoint acrolinxEndpoint = WireMockUtils.createTestAcrolinxEndpoint();
        Assertions.assertThrows(JsonSyntaxException.class, () -> acrolinxEndpoint.getPlatformInformation());
    }
}
