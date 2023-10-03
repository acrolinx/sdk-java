/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.platform;

public class Link {
  private final String linkType;
  private final String link;

  public Link(String linkType, String link) {
    this.linkType = linkType;
    this.link = link;
  }

  public String getLinkType() {
    return linkType;
  }

  public String getLink() {
    return link;
  }
}
