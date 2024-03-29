/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.internal;

import com.acrolinx.client.sdk.SignInSuccess;
import io.gsonfire.TypeSelector;

/** "Sealed" class */
public abstract class SignInResponse {
  SignInResponse() {}

  static final TypeSelector<SignInResponse> TYPE_SELECTOR =
      jsonElement -> {
        boolean isSuccess =
            jsonElement.getAsJsonObject().get("data").getAsJsonObject().has("accessToken");
        return isSuccess ? SignInResponse.Success.class : SignInResponse.SignInLinks.class;
      };

  public static class Success extends SignInResponse {
    public final SignInSuccess data;

    public Success(SignInSuccess data) {
      this.data = data;
    }
  }

  public static class SignInLinks extends SignInResponse {
    public final SignInLinksInternal links;
    public final SignInLinksData data;

    public SignInLinks(SignInLinksInternal links, SignInLinksData data) {
      this.links = links;
      this.data = data;
    }
  }

  public static class SignInLinksData {
    private final Double interactiveLinkTimeout;

    public SignInLinksData(Double interactiveLinkTimeout) {
      this.interactiveLinkTimeout = interactiveLinkTimeout;
    }

    public Double getInteractiveLinkTimeout() {
      return interactiveLinkTimeout;
    }
  }

  public static class SignInLinksInternal {
    private final String interactive;
    private final String poll;

    public SignInLinksInternal(String interactive, String poll) {
      this.interactive = interactive;
      this.poll = poll;
    }

    public String getInteractive() {
      return interactive;
    }

    public String getPoll() {
      return poll;
    }
  }
}
