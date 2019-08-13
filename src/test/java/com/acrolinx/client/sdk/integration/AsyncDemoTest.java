/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.PlatformInformation;
import com.acrolinx.client.sdk.demo.AsyncEndpointDemo;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static com.acrolinx.client.sdk.testutils.TestConstants.DEVELOPMENT_SIGNATURE;
import static org.junit.Assert.assertNotNull;

public class AsyncDemoTest {

    @Test
    public void testFetchingPlatformInformationAsync() throws InterruptedException, ExecutionException, URISyntaxException {
        AsyncEndpointDemo asyncEndpointDemo = new AsyncEndpointDemo(new URI(ACROLINX_URL), DEVELOPMENT_SIGNATURE,
                "1.2.3.4", "en");

        Future<PlatformInformation> platformInformationFuture = asyncEndpointDemo.getPlatformInformation();
        PlatformInformation platformInformation = platformInformationFuture.get();

        assertNotNull(platformInformation);
    }
}
