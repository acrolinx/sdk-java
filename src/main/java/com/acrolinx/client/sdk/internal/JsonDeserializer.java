/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.internal;

public interface JsonDeserializer<T>
{
    T deserialize(String jsonString);
}
