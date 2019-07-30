/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.check;

public class CheckRequestBuilder {
    private String content;
    private CheckRequest.ContentEncoding contentEncoding;
    private CheckOptions checkOptions;
    private DocumentDescriptorRequest document;

    public CheckRequestBuilder(String content) {
        this.content = content;
    }

    public CheckRequestBuilder setContentEncoding(CheckRequest.ContentEncoding contentEncoding) {
        this.contentEncoding = contentEncoding;
        return this;
    }

    public CheckRequestBuilder setCheckOptions(CheckOptions checkOptions) {
        this.checkOptions = checkOptions;
        return this;
    }

    public CheckRequestBuilder setDocument(DocumentDescriptorRequest document) {
        this.document = document;
        return this;
    }

    public CheckRequest build() {
        return new CheckRequest(content, contentEncoding, checkOptions, document);
    }
}