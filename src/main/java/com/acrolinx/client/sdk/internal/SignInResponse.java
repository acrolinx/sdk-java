package com.acrolinx.client.sdk.internal;

import com.acrolinx.client.sdk.SignInSuccess;
import com.google.gson.JsonElement;
import io.gsonfire.TypeSelector;

abstract public class SignInResponse {
    static TypeSelector<SignInResponse> TYPE_SELECTOR = new TypeSelector<SignInResponse>() {
        @Override
        public Class<? extends SignInResponse> getClassForElement(JsonElement readElement) {
            Boolean isSuccess = readElement.getAsJsonObject().get("data").getAsJsonObject().has("accessToken");
            return isSuccess ? SignInResponse.Success.class : SignInResponse.SignInLinks.class;
        }
    };

    public static class Success extends SignInResponse {
        public SignInSuccess data;
    }

    public static class SignInLinks extends SignInResponse {
        public SignInLinksInternal links;
        public SignInLinksData data;
    }

    public static class SignInLinksData {
        public Double interactiveLinkTimeout;
    }

    public static class SignInLinksInternal {
        public String interactive;
        public String poll;
    }
}


