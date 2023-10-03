/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.platform.configuration.Integration;

public class SignInSuccess {
  public enum AuthorizationType {
    ACROLINX_SSO,
    ACROLINX_SIGN_IN,
    ACROLINX_TOKEN,
  }

  private final String accessToken;
  private final User user;
  private final Integration integration;
  private final String authorizedUsing;

  public SignInSuccess(
      String accessToken, User user, Integration integration, String authorizedUsing) {
    this.accessToken = accessToken;
    this.user = user;
    this.integration = integration;
    this.authorizedUsing = authorizedUsing;
  }

  public AccessToken getAccessToken() {
    return new AccessToken(accessToken);
  }

  public User getUser() {
    return user;
  }

  public Integration getIntegration() {
    return integration;
  }

  /**
   * @return A String value corresponding to {@link AuthorizationType} or something extended in
   *     future API versions
   */
  public String getAuthorizationType() {
    return authorizedUsing;
  }
}
