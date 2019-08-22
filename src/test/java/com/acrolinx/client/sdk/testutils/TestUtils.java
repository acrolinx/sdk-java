/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.testutils;

import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class TestUtils
{
    public static String readResource(String resourcePath)
    {
        try {
            return Resources.toString(Resources.getResource(resourcePath), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
