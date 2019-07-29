package com.acrolinx.client.sdk.exceptions;

public class AcrolinxException extends Exception {
    public AcrolinxException(String message) {
        super(message);
    }

    public AcrolinxException(Throwable cause) {
        super(cause);
    }
}
