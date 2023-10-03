/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import org.junit.jupiter.api.Test;

class ContentAnalysisDashboardTest extends IntegrationTestBase {
  @Test
  void testFetchingContentAnalysisDasboard() throws AcrolinxException {
    String contentAnalysisDashboardLink =
        acrolinxEndpoint.getContentAnalysisDashboard(ACROLINX_API_TOKEN, "1");

    assertFalse(contentAnalysisDashboardLink.isEmpty());
  }
}
