/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

public class CheckResponse {
  public static class CheckResponseData {
    private final String id;

    public CheckResponseData(String id) {
      this.id = id;
    }

    public String getId() {
      return id;
    }
  }

  public static class CheckResponseLinks {
    private final String result;
    private final String cancel;

    public CheckResponseLinks(String result, String cancel) {
      this.result = result;
      this.cancel = cancel;
    }

    public String getResult() {
      return result;
    }

    public String getCancel() {
      return cancel;
    }
  }

  private final CheckResponseData data;
  private final CheckResponseLinks links;

  public CheckResponse(CheckResponseData data, CheckResponseLinks links) {
    this.data = data;
    this.links = links;
  }

  public CheckResponseData getData() {
    return data;
  }

  public CheckResponseLinks getLinks() {
    return links;
  }
}
