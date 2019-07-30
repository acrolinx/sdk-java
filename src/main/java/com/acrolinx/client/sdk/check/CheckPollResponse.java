package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.internal.ProgressInternal;
import com.google.gson.JsonElement;
import io.gsonfire.TypeSelector;

/**
 * "Sealed" class
 */
public abstract class CheckPollResponse {
    public static final TypeSelector<CheckPollResponse> TYPE_SELECTOR = new TypeSelector<CheckPollResponse>() {
        @Override
        public Class<? extends CheckPollResponse> getClassForElement(JsonElement readElement) {
            Boolean isSuccess = readElement.getAsJsonObject().has("data");
            return isSuccess ? CheckPollResponse.Success.class : CheckPollResponse.Progress.class;
        }
    };

    private CheckPollResponse() {
    }

    public static class Success extends CheckPollResponse {
        public final CheckResult data;

        public Success(CheckResult data) {
            this.data = data;
        }
    }

    public static class Progress extends CheckPollResponse {
        public final ProgressInternal progress;

        public Progress(ProgressInternal progress) {
            this.progress = progress;
        }
    }
}
