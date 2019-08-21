/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

public class CheckRequestBuilder {
    private AcrolinxDocument acrolinxDocument;
    private CheckRequest.ContentEncoding contentEncoding;
    private CheckOptions checkOptions;
    private DocumentDescriptorRequest document;

    public CheckRequestBuilder(AcrolinxDocument content) {
        this.acrolinxDocument = content;
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

    public CheckRequest build() throws AcrolinxException {
        String content = acrolinxDocument.getContent();
        return new CheckRequest(content, contentEncoding, checkOptions, document);
    }
}