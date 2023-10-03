/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import com.google.gson.Gson;

public class ExternalContentField {
  private final String id;
  private final String content;

  ExternalContentField(String id, String content) {
    this.id = id;
    this.content = content;
  }

  public String getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
