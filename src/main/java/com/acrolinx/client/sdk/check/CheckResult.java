package com.acrolinx.client.sdk.check;

public class CheckResult {
    private final String id;
    private final Quality quality;

    public CheckResult(String id, Quality quality) {
        this.id = id;
        this.quality = quality;
    }

    public String getId() {
        return id;
    }

    public Quality getQuality() {
        return quality;
    }
}
