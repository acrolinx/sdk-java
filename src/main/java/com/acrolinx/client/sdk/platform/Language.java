package com.acrolinx.client.sdk.platform;

public class Language {
    private String id;
    private String displayName;

    public Language(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }
}
