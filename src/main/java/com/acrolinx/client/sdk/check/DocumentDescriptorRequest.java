/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import java.util.List;

public class DocumentDescriptorRequest
{
    private final String reference;
    private final List<CustomField> customFields;

    public DocumentDescriptorRequest(String reference, List<CustomField> customFields)
    {
        this.reference = reference;
        this.customFields = customFields;
    }

    public String getReference()
    {
        return reference;
    }

    public List<CustomField> getCustomFields()
    {
        return customFields;
    }
}
