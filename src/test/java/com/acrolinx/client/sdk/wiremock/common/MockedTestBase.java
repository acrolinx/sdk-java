/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.wiremock.common;

import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.PLATFORM_PORT_MOCKED;
import static com.acrolinx.client.sdk.wiremock.common.WireMockUtils.createTestAcrolinxEndpointMocked;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Rule;

import com.acrolinx.client.sdk.AcrolinxEndpoint;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class MockedTestBase
{
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(PLATFORM_PORT_MOCKED);

    protected AcrolinxEndpoint endpoint;

    @Before
    public void beforeTestBase() throws URISyntaxException
    {
        endpoint = createTestAcrolinxEndpointMocked();
        assertTrue(wireMockRule.isRunning());
    }
}