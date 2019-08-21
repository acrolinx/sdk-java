/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.utils.BatchCheckIdGenerator;

public class BatchIdGeneratorTest extends IntegrationTestBase
{

    @Test
    public void testBatchIdGeneratorEmpty()
    {
        String batchid = BatchCheckIdGenerator.getId("");
        assertTrue(batchid.contains("javaSDK"));

    }

    @Test
    public void testIntegrationNameContainsSpaces()
    {
        String batchid = BatchCheckIdGenerator.getId("acrolinx for java");
        assertFalse(batchid.contains(" "));
    }

    @Test
    public void testBatchIdGenerationSimple()
    {
        String batchid = BatchCheckIdGenerator.getId("cli");
        assertTrue(batchid.contains("cli"));
        assertTrue(batchid.contains("gen"));
    }

}
