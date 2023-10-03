/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import io.gsonfire.TypeSelector;

/** "Sealed" class */
public abstract class CheckPollResponse {
  public static final TypeSelector<CheckPollResponse> TYPE_SELECTOR =
      jsonElement -> {
        boolean isSuccess = jsonElement.getAsJsonObject().has("data");
        return isSuccess ? CheckPollResponse.Success.class : Progress.class;
      };

  CheckPollResponse() {}

  public static class Success extends CheckPollResponse {
    public final CheckResult data;

    public Success(CheckResult data) {
      this.data = data;
    }
  }

  public static class Progress extends CheckPollResponse {
    public final com.acrolinx.client.sdk.Progress progress;

    public Progress(com.acrolinx.client.sdk.Progress progress) {
      this.progress = progress;
    }
  }
}
