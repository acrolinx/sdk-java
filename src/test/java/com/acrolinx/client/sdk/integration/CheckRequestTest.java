/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static junit.framework.TestCase.assertNotNull;

import org.junit.Test;

import com.acrolinx.client.sdk.check.CheckRequest;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;

public class CheckRequestTest extends IntegrationTestBase
{

    @Test
    public void testCreateCheckRequest()
    {
        CheckRequest checkRequest = CheckRequest.ofDocumentContent("This text contains no error.").build();
        assertNotNull(checkRequest);
    }
}
