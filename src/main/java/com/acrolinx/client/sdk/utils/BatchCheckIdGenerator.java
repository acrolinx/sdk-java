/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.utils;

import java.util.UUID;

public final class BatchCheckIdGenerator {
  private BatchCheckIdGenerator() {
    throw new IllegalStateException();
  }

  public static String getId(String integrationShortName) {
    String uuid = UUID.randomUUID().toString();
    String name = "javaSDK";

    if (integrationShortName != null && !integrationShortName.isEmpty()) {
      name = integrationShortName.trim().replace(" ", "-");
    }

    return "gen." + name + "." + uuid;
  }
}
