package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.acrolinx.client.sdk.PlatformInformation;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.Arrays;

import static com.acrolinx.client.sdk.integration.CommonTestSetup.ACROLINX_URL;
import static com.acrolinx.client.sdk.integration.CommonTestSetup.createTestAcrolinxEndpoint;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class GetPlatformInformationTest {
    AcrolinxEndpoint endpoint;

    @Before
    public void beforeTest() throws URISyntaxException {
        assumeTrue(ACROLINX_URL != null);
        endpoint = createTestAcrolinxEndpoint();
    }

    @Test
    public void testFetchingPlatformInformation() throws AcrolinxException {
        PlatformInformation platformInformation = endpoint.getPlatformInformation();

        assertNotNull(platformInformation);

        assertEquals("Acrolinx Core Platform", platformInformation.getServer().getName());
        assertEquals(Arrays.asList("en", "fr", "de", "ja", "pt", "sv", "zh"), platformInformation.getLocales());

        String version = platformInformation.getServer().getVersion();
        assertTrue("Server version set", !version.isEmpty());
        assertTrue("Server version starts with 2019", version.startsWith("2019"));
    }
}
