/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.check;

import java.util.ArrayList;
import java.util.List;

public class DocumentDescriptorRequest {
    private final String reference;
    private List<CustomField> customFields = null;

    public DocumentDescriptorRequest(String reference) {
        this.reference = reference;
        this.customFields = new ArrayList<>();
    }

    public String getReference() {
        return reference;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomField> customFields) {
        this.customFields.addAll(customFields);
    }

    public void setCustomField(CustomField customField) {
        this.customFields.add(customField);
    }
}
