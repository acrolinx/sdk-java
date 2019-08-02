package com.acrolinx.client.sdk.platform;

public class ContentFormat {

    private String id;
    private String displayName;

    public ContentFormat(String id, String displayName) {
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
