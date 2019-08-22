/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;

public class SimpleDocument implements Document
{

    private String content;

    public SimpleDocument(String content)
    {
        this.content = content;
    }

    @Override
    public String getContent() throws AcrolinxException
    {
        return this.content;
    }

}
