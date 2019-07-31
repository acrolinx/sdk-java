/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.ContentAnalysisDashboard;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.platform.Link;
import org.apache.commons.lang3.StringUtils;
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
        ContentAnalysisDashboard dashboard = endpoint.getLinkToContentAnalysisDashboard(ACROLINX_API_TOKEN, "1");


        for (Link link : dashboard.getLinks()) {
            assertTrue("Link with access token", link != null);
            assertTrue("Link presence", !StringUtils.isEmpty(link.getLink()));
            assertTrue("LinkType", !StringUtils.isEmpty(link.getLinkType()));
        }
    }
}
