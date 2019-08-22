/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

import javax.annotation.Nullable;

public class CheckRequestBuilder {
    private String content;
    private CheckRequest.ContentEncoding contentEncoding;
    private CheckOptions checkOptions;
    private DocumentDescriptorRequest document;

    CheckRequestBuilder(Document document) throws AcrolinxException {
        this.content = document.getContent();
    }

    CheckRequestBuilder(String content) {
        this.content = content;
    }

    public static MultiPartDocumentBuilder getBuilder(String rootElement, @Nullable String publicId, @Nullable String systemId) throws AcrolinxException {
        return new MultiPartDocumentBuilder(rootElement, publicId, systemId);
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