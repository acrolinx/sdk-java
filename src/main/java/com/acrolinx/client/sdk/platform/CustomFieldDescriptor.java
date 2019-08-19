/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.platform;

import java.util.List;

public class CustomFieldDescriptor {
    private String key;
    private String displayName;
    private String inputType;
    private String type;
    private List<String> possibleValues;

    public CustomFieldDescriptor(String key, String displayName, String inputType, String type, List<String> possibleValues) {
        this.key = key;
        this.displayName = displayName;
        this.inputType = inputType;
        this.type = type;
        this.possibleValues = possibleValues;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getInputType() {
        return inputType;
    }

    public String getType() {
        return type;
    }

    public List<String> getPossibleValues() {
        return possibleValues;
    }

}
