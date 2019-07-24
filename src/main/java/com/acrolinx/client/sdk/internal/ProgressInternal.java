package com.acrolinx.client.sdk.internal;

public class ProgressInternal {
    Double retryAfter;
    Double percent;
    String message;

    public ProgressInternal(Double retryAfter, Double percent, String message) {
        this.retryAfter = retryAfter;
        this.percent = percent;
        this.message = message;
    }

    public Double getPercent() {
        return percent;
    }

    public String getMessage() {
        return message;
    }
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
        return Math.round(this.getRetryAfter() * 1000.0);
    }
}
