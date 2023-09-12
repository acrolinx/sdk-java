/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import javax.annotation.Nullable;

public class CheckRequest
{
    CheckRequest(String content, @Nullable ContentEncoding contentEncoding, @Nullable CheckOptions checkOptions,
            @Nullable DocumentDescriptorRequest document, ExternalContent externalContent)
    {
        this.content = content;
        this.contentEncoding = contentEncoding;
        this.checkOptions = checkOptions;
        this.document = document;
        this.externalContent = externalContent;
    }

    public ExternalContent getExternalContent()
    {
        return externalContent;
    }

    public static CheckRequestBuilder ofDocumentContent(String content)
    {
        return new CheckRequestBuilder(content);
    }

    public static CheckRequestBuilder ofDocument(Document document) throws AcrolinxException
    {
        return new CheckRequestBuilder(document);
    }

    private final String content;

    @Nullable
    private final ContentEncoding contentEncoding;

    @Nullable
    private final CheckOptions checkOptions;

    @Nullable
    private final DocumentDescriptorRequest document;

    @Nullable
    private final ExternalContent externalContent;

    public String getContent()
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

    public enum ContentEncoding
    {
        none, base64,
    }
}
