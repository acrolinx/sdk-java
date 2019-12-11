/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.acrolinx.client.sdk.PlatformInformation;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;

public class GetPlatformInformationTest extends IntegrationTestBase {
    @Test
    public void testFetchingPlatformInformation() throws AcrolinxException, InterruptedException {
        PlatformInformation platformInformation = endpoint.getPlatformInformation();

        assertNotNull(platformInformation);

        assertEquals("Acrolinx Core Platform", platformInformation.getServer().getName());
        assertEquals(Arrays.asList("en", "fr", "de", "ja", "pt", "sv", "zh"), platformInformation.getLocales());

        final String version = platformInformation.getServer().getVersion();
        assertTrue("Server version set", !version.isEmpty());
        final int year = GregorianCalendar.getInstance().get(GregorianCalendar.YEAR);
        assertTrue("Server version starts with " + year + " or " + (year + 1),
                version.startsWith("" + year) || version.startsWith("" + (year + 1)));
    }
}
