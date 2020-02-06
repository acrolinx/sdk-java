/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.exceptions;

public class SignInException extends AcrolinxException
{
    private static final long serialVersionUID = -2827713529999357330L;

    public SignInException(String message)
    {
        super(message);
    }

    public SignInException(Throwable cause)
    {
        super(cause);
    }
}
