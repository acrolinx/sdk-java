/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.internal;

import com.acrolinx.client.sdk.exceptions.AcrolinxServiceException;

public class ErrorResponse {
    public final AcrolinxServiceException error;

    public ErrorResponse(AcrolinxServiceException error) {
        this.error = error;
    }
}
