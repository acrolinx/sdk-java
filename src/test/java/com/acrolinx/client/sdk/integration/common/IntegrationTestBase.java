package com.acrolinx.client.sdk.integration.common;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import org.junit.Before;

import java.net.URISyntaxException;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_URL;
import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.createTestAcrolinxEndpoint;
import static org.junit.Assume.assumeTrue;

public class IntegrationTestBase {
    protected AcrolinxEndpoint endpoint;

    @Before
    public void beforeTestBase() throws URISyntaxException {
        assumeTrue(ACROLINX_URL != null);
        endpoint = createTestAcrolinxEndpoint();
    }
}
