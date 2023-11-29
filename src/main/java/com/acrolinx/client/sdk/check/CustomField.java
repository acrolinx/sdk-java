/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

public class CustomField {
  private final String key;
  private final String value;

  public CustomField(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
