/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.exceptions;

import com.acrolinx.client.sdk.http.HttpMethod;
import com.acrolinx.client.sdk.internal.ErrorResponse;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.net.URI;

/**
 * An exception that is thrown by the Acrolinx web service.
 */
public class AcrolinxServiceException extends AcrolinxException {
    public enum Type {
        auth
    }

    private final ErrorResponse.AcrolinxServiceError error;
    private final HttpRequest request;

    public AcrolinxServiceException(ErrorResponse.AcrolinxServiceError error, HttpRequest request) {
        super(error.getTitle());
        this.error = error;
        this.request = request;
    }

    public String getType() {
        return error.getType();
    }

    public String getTitle() {
        return error.getTitle();
    }

    public String getDetail() {
        return error.getDetail();
    }

    public int getStatus() {
        return error.getStatus();
    }

    @Nullable
    public String getReference() {
        return error.getReference();
    }

    public HttpRequest getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return "AcrolinxServiceException{" +
                "error=" + error +
                ", request=" + request +
                '}';
    }

    public static class HttpRequest implements Serializable {
        private final URI url;
        private final HttpMethod method;

        public HttpRequest(URI url, HttpMethod method) {
            this.url = url;
            this.method = method;
        }

        public URI getUrl() {
            return url;
        }

        public HttpMethod getMethod() {
            return method;
        }

        @Override
        public String toString() {
            return "HttpRequest{" +
                    "url=" + url +
                    ", method=" + method +
                    '}';
        }
    }
}
