/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.acrolinx.client.sdk.check.CheckRequest;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;

class CheckRequestTest extends IntegrationTestBase
{
    @Test
    void testCreateCheckRequest()
    {
        assertNotNull(CheckRequest.ofDocumentContent("This text contains no error.").build());
    }
}
