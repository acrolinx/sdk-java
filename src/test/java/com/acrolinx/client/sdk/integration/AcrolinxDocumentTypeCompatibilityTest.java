/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static com.acrolinx.client.sdk.integration.common.CommonTestSetup.ACROLINX_API_TOKEN;
import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;

public class AcrolinxDocumentTypeCompatibilityTest extends IntegrationTestBase
{

    @Test
    public void testSimpleValidFile() throws AcrolinxException
    {
        boolean documentTypeCheckable = endpoint.isDocumentTypeCheckable(".xml", ACROLINX_API_TOKEN);
        assertEquals(documentTypeCheckable, true);
    }

    @Test
    public void testPath() throws AcrolinxException
    {
        boolean documentTypeCheckable = endpoint.isDocumentTypeCheckable("https://acrolinx.com/index.html",
                ACROLINX_API_TOKEN);
        assertEquals(documentTypeCheckable, true);
    }

    @Test
    public void testSimpleInvalid() throws AcrolinxException
    {
        boolean documentTypeCheckable = endpoint.isDocumentTypeCheckable(UUID.randomUUID().toString(),
                ACROLINX_API_TOKEN);
        assertEquals(documentTypeCheckable, false);
    }
}
