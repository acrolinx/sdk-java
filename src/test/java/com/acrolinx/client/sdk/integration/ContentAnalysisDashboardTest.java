/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import org.junit.Before;
import org.junit.Test;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class ContentAnalysisDashboardTest extends IntegrationTestBase {

    @Before
    public void beforeTest() {
        assumeTrue(ACROLINX_API_TOKEN != null);
    }

    @Test
    public void testFetchingContentAnalysisDasboard() throws AcrolinxException {
        String contentAnalysisDashboardLink = endpoint.getContentAnalysisDashboard(ACROLINX_API_TOKEN, "1");

        assertTrue(contentAnalysisDashboardLink != null);
        assertTrue(!contentAnalysisDashboardLink.isEmpty());
    }
}
