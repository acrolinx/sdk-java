/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.wiremock;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockSuccessResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.acrolinx.client.sdk.PlatformInformation;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.platform.Server;
import com.acrolinx.client.sdk.wiremock.common.MockedTestBase;
import com.google.common.collect.Lists;

public class GetPlatformInformationMockedTest extends MockedTestBase
{
    @Test
    public void testFetchingPlatformInformationSucces() throws AcrolinxException, InterruptedException
    {
        PlatformInformation expectedPlatformInformation = new PlatformInformation(new Server("2018.12", "Old Server"),
                Lists.newArrayList("en"));
        mockSuccessResponse("", expectedPlatformInformation);

        PlatformInformation platformInformation = endpoint.getPlatformInformation();

        assertNotNull(platformInformation);

        assertEquals(expectedPlatformInformation.getServer().getName(), platformInformation.getServer().getName());
        assertEquals(expectedPlatformInformation.getServer().getVersion(),
                platformInformation.getServer().getVersion());
        assertEquals(expectedPlatformInformation.getLocales(), platformInformation.getLocales());
    }

    @Test(expected = Exception.class)
    public void testFetchingPlatformInformationFailure() throws AcrolinxException, InterruptedException
    {
        mockSuccessResponse("", "Wrong Result");
        endpoint.getPlatformInformation();
    }
}
