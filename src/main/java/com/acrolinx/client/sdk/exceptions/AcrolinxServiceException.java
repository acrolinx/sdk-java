/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.exceptions;

import javax.annotation.Nullable;

/**
 * An exception that is thrown by the Acrolinx web service.
 */
public class AcrolinxServiceException extends AcrolinxRuntimeException {
    public enum Type {
        auth
    }
    private final String type;
    private final String title;
    private final String detail;
    private final int status;

    @Nullable
    private final String reference;

    public AcrolinxServiceException(String type, String title, String detail, int status,
                                    @Nullable String reference) {
        super(title);
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.status = status;
        this.reference = reference;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public int getStatus() {
        return status;
    }

    @Nullable
    public String getReference() {
        return reference;
    }
}
