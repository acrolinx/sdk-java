package com.acrolinx.client.sdk.wiremock;

import com.acrolinx.client.sdk.PlatformInformation;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.platform.Server;
import com.acrolinx.client.sdk.wiremock.common.MockedTestBase;
import com.google.common.collect.Lists;
import org.junit.Test;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.mockSuccessResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class GetPlatformInformationMockedTest extends MockedTestBase {
    @Test
    public void testFetchingPlatformInformationSucces() throws AcrolinxException {
        PlatformInformation expectedPlatformInformation = new PlatformInformation(new Server("2018.12", "Old Server"), Lists.newArrayList("en"));
        mockSuccessResponse("", expectedPlatformInformation);

        PlatformInformation platformInformation = endpoint.getPlatformInformation();

        assertNotNull(platformInformation);

        assertEquals(expectedPlatformInformation.getServer().getName(), platformInformation.getServer().getName());
        assertEquals(expectedPlatformInformation.getServer().getVersion(), platformInformation.getServer().getVersion());
        assertEquals(expectedPlatformInformation.getLocales(), platformInformation.getLocales());
    }

    // TODO: Specify our Exception strategy
    @Test(expected = Exception.class)
    public void testFetchingPlatformInformationFailure() throws AcrolinxException {
        mockSuccessResponse("", "Wrong Result");
        endpoint.getPlatformInformation();
    }
}
