/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import java.util.List;

class DocumentDescriptorRequest
{
    private final String reference;
    private final List<CustomField> customFields;

    DocumentDescriptorRequest(String reference, List<CustomField> customFields)
    {
        this.reference = reference;
        this.customFields = customFields;
    }
}
