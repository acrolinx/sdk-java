/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.internal;

public interface JsonDeserializer<T> {
  T deserialize(String jsonString);
}
