/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.exceptions;

import com.acrolinx.client.sdk.http.HttpMethod;
import com.acrolinx.client.sdk.internal.ErrorResponse;
import java.io.Serializable;
import java.net.URI;
import javax.annotation.Nullable;

/** An exception that is thrown by the Acrolinx web service. */
public class AcrolinxServiceException extends AcrolinxException {
  private static final long serialVersionUID = -3369203242171104652L;

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
    return "AcrolinxServiceException{" + "error=" + error + ", request=" + request + '}';
  }

  public static class HttpRequest implements Serializable {
    private static final long serialVersionUID = -2800135231507144829L;

    private final URI uri;
    private final HttpMethod method;

    public HttpRequest(URI url, HttpMethod method) {
      this.uri = url;
      this.method = method;
    }

    public URI getUrl() {
      return uri;
    }

    public HttpMethod getMethod() {
      return method;
    }

    @Override
    public String toString() {
      return "HttpRequest{" + "url=" + uri + ", method=" + method + '}';
    }
  }
}
