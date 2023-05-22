/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.client.sdk.platform;

public class Capabilities
{
    private CheckingCapabilities checking;
    private Document document;

    public CheckingCapabilities getCheckingCapabilities()
    {
        return checking;
    }

    public Document getDocument()
    {
        return this.document;
    }
}
