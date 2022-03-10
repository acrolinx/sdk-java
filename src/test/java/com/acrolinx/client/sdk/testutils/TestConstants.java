/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.testutils;

import io.github.cdimascio.dotenv.Dotenv;

public class TestConstants
{
    static Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    public static final String ACROLINX_DEV_SIGNATURE = dotenv.get("ACROLINX_DEV_SIGNATURE");
    public static final String DEVELOPMENT_SIGNATURE = ACROLINX_DEV_SIGNATURE.isEmpty()
            ? "SW50ZWdyYXRpb25EZXZlbG9wbWVudERlbW9Pbmx5"
            : ACROLINX_DEV_SIGNATURE;
}
