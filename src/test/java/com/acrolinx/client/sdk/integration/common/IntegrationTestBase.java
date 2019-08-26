/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration.common;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.createTestAcrolinxEndpoint;
import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;

import com.acrolinx.client.sdk.AcrolinxEndpoint;

public class IntegrationTestBase
{
    protected AcrolinxEndpoint endpoint;

    @Before
    public void beforeTestBase() throws URISyntaxException, IOException
    {
        assumeTrue(ACROLINX_URL != null);
        endpoint = createTestAcrolinxEndpoint();

    }

    @After
    public void afterTestBase() throws IOException
    {
        endpoint.close();
    }
}
