/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import javax.annotation.Nullable;

public class SimpleDocument implements Document {
  private final String content;
  private final ExternalContent externalContent;

  public SimpleDocument(String content) {
    this(content, null);
  }

  public SimpleDocument(String content, @Nullable ExternalContent externalContent) {
    this.content = content;
    this.externalContent =
        externalContent == null ? new ExternalContentBuilder().build() : externalContent;
  }

  @Override
  public String getContent() {
    return this.content;
  }

  @Override
  public ExternalContent getExternalContent() {
    return externalContent;
  }
}
