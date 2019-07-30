/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.platform;

public class GuidanceProfile {
    private String id;
    private String displayName;
    private Language language;

    public GuidanceProfile(String id, String displayName, Language language) {
        this.id = id;
        this.displayName = displayName;
        this.language = language;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Language getLanguage() {
        return language;
    }
}
