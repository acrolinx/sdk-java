package com.acrolinx.client.sdk.internal;

public interface JsonDeserializer<T> {
    T deserialize(String jsonString);
}
