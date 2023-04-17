/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;

class AcrolinxDocumentTypeCompatibilityTest extends IntegrationTestBase
{
    @Test
    void testSimpleValidFile() throws AcrolinxException
    {
        boolean documentTypeCheckable = acrolinxEndpoint.isDocumentTypeCheckable(".xml", ACROLINX_API_TOKEN);
        assertTrue(documentTypeCheckable);
    }

    @Test
    void testPath() throws AcrolinxException
    {
        boolean documentTypeCheckable = acrolinxEndpoint.isDocumentTypeCheckable("https://acrolinx.com/index.html",
                ACROLINX_API_TOKEN);
        assertTrue(documentTypeCheckable);
    }

    @Test
    void testSimpleInvalid() throws AcrolinxException
    {
        boolean documentTypeCheckable = acrolinxEndpoint.isDocumentTypeCheckable(UUID.randomUUID().toString(),
                ACROLINX_API_TOKEN);
        assertFalse(documentTypeCheckable);
    }
}
