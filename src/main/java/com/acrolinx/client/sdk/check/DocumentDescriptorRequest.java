/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.check;

public class DocumentDescriptorRequest {
    private final String reference;

    public DocumentDescriptorRequest(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }
}
