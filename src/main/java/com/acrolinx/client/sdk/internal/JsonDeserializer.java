/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.internal;

public interface JsonDeserializer<T>
{
    T deserialize(String jsonString);
}
