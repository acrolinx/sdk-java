/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.internal;

public class SuccessResponse<T> {
  public final T data;

  public SuccessResponse(T data) {
    this.data = data;
  }
}
