package com.acrolinx.client.sdk.internal;

import com.acrolinx.client.sdk.SignInSuccess;
import com.google.gson.JsonElement;
import io.gsonfire.TypeSelector;

/**
 * "Sealed" class
 */
public abstract class SignInPollResponse {
    private SignInPollResponse() {
    }

    static TypeSelector<SignInPollResponse> TYPE_SELECTOR = new TypeSelector<SignInPollResponse>() {
        @Override
        public Class<? extends SignInPollResponse> getClassForElement(JsonElement readElement) {
            Boolean isSuccess = readElement.getAsJsonObject().has("data");
            return isSuccess ? SignInPollResponse.Success.class : SignInPollResponse.Progress.class;
        }
    };

    public static class Success extends SignInPollResponse {
        public SignInSuccess data;
    }

    public static class Progress extends SignInPollResponse {
        public ProgressInternal progress;
    }
}
