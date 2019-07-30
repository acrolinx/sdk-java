/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.exceptions;

public class SignInException extends AcrolinxException {
    public SignInException(String message) {
        super(message);
    }

    public SignInException(Throwable cause) {
        super(cause);
    }
}
