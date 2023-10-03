/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.testutils;

import io.github.cdimascio.dotenv.Dotenv;

public final class TestConstants {
  private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
  public static final String DEVELOPMENT_SIGNATURE = dotenv.get("ACROLINX_DEV_SIGNATURE");

  private TestConstants() {
    throw new IllegalStateException();
  }
}
