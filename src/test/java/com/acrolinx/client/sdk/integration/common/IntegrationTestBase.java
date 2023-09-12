/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.integration.common;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.createTestAcrolinxEndpoint;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class IntegrationTestBase
{
    protected AcrolinxEndpoint acrolinxEndpoint;

    @BeforeEach
    void beforeEachBase() throws URISyntaxException
    {
        acrolinxEndpoint = createTestAcrolinxEndpoint();
    }

    @AfterEach
    void afterEachBase() throws IOException
    {
        acrolinxEndpoint.close();
    }
}
