/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import com.google.gson.JsonElement;

import io.gsonfire.TypeSelector;

/**
 * "Sealed" class
 */
public abstract class CheckPollResponse
{
    public static final TypeSelector<CheckPollResponse> TYPE_SELECTOR = new TypeSelector<CheckPollResponse>() {
        @Override
        public Class<? extends CheckPollResponse> getClassForElement(JsonElement readElement)
        {
            Boolean isSuccess = readElement.getAsJsonObject().has("data");
            return isSuccess ? CheckPollResponse.Success.class : Progress.class;
        }
    };

    private CheckPollResponse()
    {
    }

    public static class Success extends CheckPollResponse
    {
        public final CheckResult data;

        public Success(CheckResult data)
        {
            this.data = data;
        }
    }

    public static class Progress extends CheckPollResponse
    {
        public final com.acrolinx.client.sdk.Progress progress;

        public Progress(com.acrolinx.client.sdk.Progress progress)
        {
            this.progress = progress;
        }
    }
}
