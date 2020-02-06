/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.exceptions;

public class AcrolinxException extends Exception
{
    private static final long serialVersionUID = 1066823292340363033L;

    public AcrolinxException(String message)
    {
        super(message);
    }

    public AcrolinxException(Throwable cause)
    {
        super(cause);
    }
}
