/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.exceptions;

public class AcrolinxRuntimeException extends RuntimeException {
    public AcrolinxRuntimeException(String message) {
        super(message);
    }

    public AcrolinxRuntimeException(Throwable cause) {
        super(cause);
    }
}
