package com.acrolinx.client.sdk.internal;

public class ProgressInternal {
    Double retryAfter;
    Double percent;
    Double message;

    /**
     * In seconds
     */
    public Double getRetryAfter() {
        return retryAfter;
    }

    /**
     * In milliseconds
     */
    public long getRetryAfterMs() {
        return Math.round(retryAfter * 1000.0);
    }
}
