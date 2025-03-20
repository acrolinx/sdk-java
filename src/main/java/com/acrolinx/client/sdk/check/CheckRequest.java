/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

public class CheckRequest {
  CheckRequest(
      String content,
      ContentEncoding contentEncoding,
      CheckOptions checkOptions,
      DocumentDescriptorRequest document,
      ExternalContent externalContent) {
    this.content = content;
    this.contentEncoding = contentEncoding;
    this.checkOptions = checkOptions;
    this.document = document;
    this.externalContent = externalContent;
  }

  public ExternalContent getExternalContent() {
    return externalContent;
  }

  public static CheckRequestBuilder ofDocumentContent(String content) {
    return new CheckRequestBuilder(content);
  }

  public static CheckRequestBuilder ofDocument(Document document) throws AcrolinxException {
    return new CheckRequestBuilder(document);
  }

  private final String content;
  private final ContentEncoding contentEncoding;
  private final CheckOptions checkOptions;
  private final DocumentDescriptorRequest document;
  private final ExternalContent externalContent;

  public String getContent() {
    return content;
  }

  public ContentEncoding getContentEncoding() {
    return contentEncoding;
  }

  public CheckOptions getCheckOptions() {
    return checkOptions;
  }

  public DocumentDescriptorRequest getDocument() {
    return document;
  }

  public enum ContentEncoding {
    none,
    base64,
  }
}
