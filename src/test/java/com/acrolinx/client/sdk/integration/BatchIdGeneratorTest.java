/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import com.acrolinx.client.sdk.utils.BatchCheckIdGenerator;

class BatchIdGeneratorTest extends IntegrationTestBase
{
    @Test
    void testBatchIdGeneratorEmpty()
    {
        assertTrue(BatchCheckIdGenerator.getId("").contains("javaSDK"));
    }

    @Test
    void testIntegrationNameContainsSpaces()
    {
        assertFalse(BatchCheckIdGenerator.getId("acrolinx for java").contains(" "));
    }

    @Test
    void testBatchIdGenerationSimple()
    {
        String batchid = BatchCheckIdGenerator.getId("cli");
        assertTrue(batchid.contains("cli"));
        assertTrue(batchid.contains("gen"));
    }
}
