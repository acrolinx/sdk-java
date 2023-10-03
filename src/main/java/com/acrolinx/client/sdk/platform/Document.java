/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.platform;

import java.util.List;

public class Document {
  private final List<CustomFieldDescriptor> customFields;

  public Document(List<CustomFieldDescriptor> customFields) {
    this.customFields = customFields;
  }

  public List<CustomFieldDescriptor> getCustomFields() {
    return customFields;
  }
}
