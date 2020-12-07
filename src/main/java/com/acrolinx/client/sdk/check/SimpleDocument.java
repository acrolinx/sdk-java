/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import javax.annotation.Nullable;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

public class SimpleDocument implements Document
{

    private String content;
    private ExternalContent externalContent;

    public SimpleDocument(String content)
    {
        this(content, null);
    }

    public SimpleDocument(String content, @Nullable ExternalContent externalContent)
    {
        this.content = content;
        this.externalContent = externalContent == null ? new ExternalContentBuilder().build() : externalContent;
    }

    @Override
    public String getContent() throws AcrolinxException
    {
        return this.content;
    }

    @Override
    public ExternalContent getExternalContent()
    {
        return externalContent;
    }

}
