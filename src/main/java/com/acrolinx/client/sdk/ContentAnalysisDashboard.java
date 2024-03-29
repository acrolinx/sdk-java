/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.platform.Link;
import java.util.List;

public class ContentAnalysisDashboard {
  private final List<Link> links;

  public ContentAnalysisDashboard(List<Link> links) {
    this.links = links;
  }

  public List<Link> getLinks() {
    return links;
  }
}
