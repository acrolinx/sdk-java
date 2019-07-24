package com.acrolinx.client.sdk.internal;

import com.acrolinx.client.sdk.SignInSuccess;
import com.google.gson.JsonElement;
import io.gsonfire.TypeSelector;

/**
 * "Sealed" class
 */
public abstract class SignInResponse {
    private SignInResponse() {
    }

    static final TypeSelector<SignInResponse> TYPE_SELECTOR = new TypeSelector<SignInResponse>() {
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
        private Double interactiveLinkTimeout;

        public SignInLinksData(Double interactiveLinkTimeout) {
            this.interactiveLinkTimeout = interactiveLinkTimeout;
        }

        public Double getInteractiveLinkTimeout() {
            return interactiveLinkTimeout;
        }
    }

    public static class SignInLinksInternal {
        private String interactive;
        private String poll;

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


