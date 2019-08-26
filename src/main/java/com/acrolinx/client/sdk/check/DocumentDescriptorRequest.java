/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import java.util.ArrayList;
import java.util.List;

class DocumentDescriptorRequest
{
    private final String reference;
    private List<CustomField> customFields;

    DocumentDescriptorRequest(String reference, List<CustomField> customFields)
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
