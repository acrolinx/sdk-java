/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.internal;

import java.io.Serializable;

import javax.annotation.Nullable;

public class ErrorResponse
{
    public final AcrolinxServiceError error;

    public ErrorResponse(AcrolinxServiceError error)
    {
        this.error = error;
    }

    public static class AcrolinxServiceError implements Serializable
    {
        private final String type;
        private final String title;
        private final String detail;
        private final int status;

        @Nullable
        private final String reference;

        public AcrolinxServiceError(String type, String title, String detail, int status, @Nullable String reference)
        {
            this.type = type;
            this.title = title;
            this.detail = detail;
            this.status = status;
            this.reference = reference;
        }

        public String getType()
        {
            return type;
        }

        public String getTitle()
        {
            return title;
        }

        public String getDetail()
        {
            return detail;
        }

        public int getStatus()
        {
            return status;
        }

        @Nullable
        public String getReference()
        {
            return reference;
        }

        @Override
        public String toString()
        {
            return "AcrolinxServiceError{" + "type='" + type + '\'' + ", title='" + title + '\'' + ", detail='" + detail
                    + '\'' + ", status=" + status + ", reference='" + reference + '\'' + '}';
        }
    }
}
