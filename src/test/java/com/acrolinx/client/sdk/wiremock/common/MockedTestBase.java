/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.wiremock.common;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.createTestAcrolinxEndpointMocked;

import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;

import com.acrolinx.client.sdk.AcrolinxEndpoint;

public abstract class MockedTestBase
{
    protected AcrolinxEndpoint acrolinxEndpoint;

    @BeforeEach
    void beforeTestBase() throws URISyntaxException
    {
        acrolinxEndpoint = createTestAcrolinxEndpointMocked();
    }
}
