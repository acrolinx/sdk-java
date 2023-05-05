/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.testutils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.google.common.io.Resources;

public final class TestUtils
{
    public static String readResource(String resourcePath) throws IOException
    {
        return Resources.toString(Resources.getResource(resourcePath), StandardCharsets.UTF_8);
    }

    public static String readResourceAsBase64(String resourcePath) throws IOException
    {
        byte[] content = Resources.toByteArray(Resources.getResource(resourcePath));
        return Base64.getEncoder().encodeToString(content);
    }

    private TestUtils()
    {
        throw new IllegalStateException();
    }
}
