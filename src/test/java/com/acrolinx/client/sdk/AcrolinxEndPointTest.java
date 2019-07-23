/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.acrolinx.client.sdk;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AcrolinxEndPointTest {
    @Test
    public void testFetchingPlatformInformation() {
        try {
            AcrolinxEndpoint endpoint = new AcrolinxEndpoint("clientSignature", "fetch from env",
                    "1.0", "en");

            PlatformInformation platformInformation = endpoint.getPlatformInformation();

            assertNotNull(platformInformation);
            assertNotNull(platformInformation.getData());
            assertNotNull(platformInformation.getLinks());

            String version = platformInformation.getData().getServer().getVersion();
            assertTrue("Server version set", !version.isEmpty());


        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }


    }

}
