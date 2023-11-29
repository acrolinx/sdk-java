/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

public class CheckCancelledResponse {
  public static class Data {
    private final String id;

    public Data(String id) {
      this.id = id;
    }

    public String getId() {
      return id;
    }
  }

  private final Data data;

  public CheckCancelledResponse(Data data) {
    this.data = data;
  }

  public Data getData() {
    return data;
  }
}
