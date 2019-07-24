package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.platform.configuration.Integration;

public class SignInSuccess {
    private String accessToken;
    private User user;
    private Integration integration;
    private String authorizedUsing;

    public AccessToken getAccessToken() {
        return new AccessToken(accessToken);
    }

    public User getUser() {
        return user;
    }

    public Integration getIntegration() {
        return integration;
    }

    public String getAuthorizationType() {
        return authorizedUsing;
    }
}
