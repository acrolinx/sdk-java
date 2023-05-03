/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.internal;

import java.util.concurrent.Callable;

public class JsonResponse implements Callable<String>
{
    private final String jsonString;

    public JsonResponse(String jsonString)
    {
        this.jsonString = jsonString;
    }

    @Override
    public String call()
    {
        return jsonString;
    }
}
