/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.platform.configuration;

import java.util.Map;

public class Integration {
  private final Map<String, String> properties;

  public Integration(Map<String, String> properties) {
    this.properties = properties;
  }

  public Map<String, String> getProperties() {
    return properties;
  }
}
