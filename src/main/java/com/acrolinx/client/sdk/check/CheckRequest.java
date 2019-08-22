/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

import javax.annotation.Nullable;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

public class CheckRequest
{
    public static CheckRequestBuilder ofDocumentContent(Document document) throws AcrolinxException
    {
        return new CheckRequestBuilder(document.getContent());
    }

    public static CheckRequestBuilder ofDocumentContent(String content)
    {
        return new CheckRequestBuilder(content);
    }

    public enum ContentEncoding
    {
        none, base64,
    }

    private final String content;

    @Nullable
    private final ContentEncoding contentEncoding;

    @Nullable
    private final CheckOptions checkOptions;

    @Nullable
    private final DocumentDescriptorRequest document;

    CheckRequest(String content, @Nullable ContentEncoding contentEncoding, @Nullable CheckOptions checkOptions,
            @Nullable DocumentDescriptorRequest document)
    {
        this.content = content;
        this.contentEncoding = contentEncoding;
        this.checkOptions = checkOptions;
        this.document = document;
    }

    public String getContent() throws AcrolinxException
    {
        return content;
    }

    @Nullable
    public ContentEncoding getContentEncoding()
    {
        return contentEncoding;
    }

    @Nullable
    public CheckOptions getCheckOptions()
    {
        return checkOptions;
    }

    @Nullable
    public DocumentDescriptorRequest getDocument()
    {
        return document;
    }

    public enum ContentEncoding {
        none,
        base64,
    }
}
