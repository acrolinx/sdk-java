/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.check;

public class CheckOptions {
    private final String guidanceProfileId;

    public CheckOptions(String guidanceProfileId) {
        this.guidanceProfileId = guidanceProfileId;
    }

    public String getGuidanceProfileId() {
        return guidanceProfileId;
    }
}
