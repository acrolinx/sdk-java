package com.acrolinx.client.sdk;

import com.acrolinx.client.sdk.platform.configuration.Integration;

public class SignInSuccess {
    private AccessToken accessToken;
    private User user;
    private Integration integration;
    private String authorizationType;

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public User getUser() {
        return user;
    }

    public Integration getIntegration() {
        return integration;
    }

    public String getAuthorizationType() {
        return authorizationType;
    }
}
