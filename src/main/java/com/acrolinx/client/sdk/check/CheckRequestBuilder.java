/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import java.util.ArrayList;
import java.util.List;

public class CheckRequestBuilder {
  private final String content;
  private CheckRequest.ContentEncoding contentEncoding;
  private CheckOptions checkOptions;
  private DocumentDescriptorRequest document;
  private ExternalContent externalContent;
  private String reference;
  private List<CustomField> customFields = new ArrayList<>();

  CheckRequestBuilder(Document document) throws AcrolinxException {
    this.content = document.getContent();
    this.externalContent = document.getExternalContent();
  }

  CheckRequestBuilder(String content) {
    this.content = content;
  }

  public CheckRequestBuilder withContentEncoding(CheckRequest.ContentEncoding contentEncoding) {
    this.contentEncoding = contentEncoding;
    return this;
  }

  public CheckRequestBuilder withCheckOptions(CheckOptions checkOptions) {
    this.checkOptions = checkOptions;
    return this;
  }

  public CheckRequestBuilder withContentReference(String reference) {
    this.reference = reference;
    return this;
  }

  public CheckRequestBuilder withCustomFields(List<CustomField> customFields) {
    this.customFields.addAll(customFields);
    return this;
  }

  public CheckRequestBuilder withCustomField(CustomField customField) {
    this.customFields.add(customField);
    return this;
  }

  public CheckRequest build() {
    if (this.reference != null) {
      this.document = new DocumentDescriptorRequest(this.reference, this.customFields);
    }

    return new CheckRequest(content, contentEncoding, checkOptions, document, this.externalContent);
  }
}
