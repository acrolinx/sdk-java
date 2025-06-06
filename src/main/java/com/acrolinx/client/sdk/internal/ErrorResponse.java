/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.internal;

import java.io.Serializable;

public class ErrorResponse {
  public final AcrolinxServiceError error;

  public ErrorResponse(AcrolinxServiceError error) {
    this.error = error;
  }

  public static class AcrolinxServiceError implements Serializable {
    private static final long serialVersionUID = 3601811551143045429L;

    private final String type;
    private final String title;
    private final String detail;
    private final int status;
    private final String reference;

    public AcrolinxServiceError(
        String type, String title, String detail, int status, String reference) {
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

    public String getReference() {
      return reference;
    }

    @Override
    public String toString() {
      return "AcrolinxServiceError{"
          + "type='"
          + type
          + '\''
          + ", title='"
          + title
          + '\''
          + ", detail='"
          + detail
          + '\''
          + ", status="
          + status
          + ", reference='"
          + reference
          + '\''
          + '}';
    }
  }
}
