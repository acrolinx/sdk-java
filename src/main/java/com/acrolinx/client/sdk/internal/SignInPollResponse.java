/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.internal;

import com.acrolinx.client.sdk.SignInSuccess;
import io.gsonfire.TypeSelector;

/** "Sealed" class */
public abstract class SignInPollResponse {
  SignInPollResponse() {}

  static final TypeSelector<SignInPollResponse> TYPE_SELECTOR =
      jsonElement -> {
        boolean isSuccess = jsonElement.getAsJsonObject().has("data");
        return isSuccess ? SignInPollResponse.Success.class : Progress.class;
      };

  public static class Success extends SignInPollResponse {
    public final SignInSuccess data;

    public Success(SignInSuccess data) {
      this.data = data;
    }
  }

  public static class Progress extends SignInPollResponse {
    public final com.acrolinx.client.sdk.Progress progress;

    public Progress(com.acrolinx.client.sdk.Progress progress) {
      this.progress = progress;
    }
  }
}
