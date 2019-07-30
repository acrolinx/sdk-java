/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.internal;

public class SuccessResponse<T> {
    public final T data;

    public SuccessResponse(T data) {
        this.data = data;
    }
}
