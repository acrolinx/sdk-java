/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.acrolinx.client.sdk.PlatformInformation;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;

public class GetPlatformInformationTest extends IntegrationTestBase
{
    @Test
    public void testFetchingPlatformInformation() throws AcrolinxException, InterruptedException
    {
        PlatformInformation platformInformation = endpoint.getPlatformInformation();

        assertNotNull(platformInformation);

        assertEquals("Acrolinx Core Platform", platformInformation.getServer().getName());
        assertEquals(Arrays.asList("en", "fr", "de", "ja", "pt", "sv", "zh"), platformInformation.getLocales());

        String version = platformInformation.getServer().getVersion();
        assertTrue("Server version set", !version.isEmpty());
        assertTrue("Server version starts with 2019", version.startsWith("2019"));
    }
}
