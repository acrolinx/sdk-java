/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.acrolinx.client.sdk.check.CheckRequest;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import org.junit.jupiter.api.Test;

class CheckRequestTest extends IntegrationTestBase {
  @Test
  void testCreateCheckRequest() {
    assertNotNull(CheckRequest.ofDocumentContent("This text contains no error.").build());
  }
}
