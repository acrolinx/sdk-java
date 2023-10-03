/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.platform;

public class Server {
  private final String version;
  private final String name;

  public Server(String version, String name) {
    this.version = version;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }
}
