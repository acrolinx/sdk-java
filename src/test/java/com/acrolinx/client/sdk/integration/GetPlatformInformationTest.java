/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.acrolinx.client.sdk.PlatformInformation;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import java.util.Arrays;
import java.util.Calendar;
import org.junit.jupiter.api.Test;

class GetPlatformInformationTest extends IntegrationTestBase {
  @Test
  void testFetchingPlatformInformation() throws AcrolinxException {
    PlatformInformation platformInformation = acrolinxEndpoint.getPlatformInformation();

    assertEquals(
        Arrays.asList("en", "fr", "de", "ja", "pt", "sv", "zh"), platformInformation.getLocales());

    final String version = platformInformation.getServer().getVersion();
    assertFalse(version.isEmpty());
    final int year = Calendar.getInstance().get(Calendar.YEAR);
    assertTrue(
        version.startsWith("" + year)
            || version.startsWith("" + (year + 1))
            || version.startsWith("" + (year - 1)));
  }
}
