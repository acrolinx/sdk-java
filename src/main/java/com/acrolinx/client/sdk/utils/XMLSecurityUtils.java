/**
 * Copyright (c) 2021-present Acrolinx GmbH
 */

package com.acrolinx.client.sdk.utils;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;

public final class XMLSecurityUtils
{
    public static void limitResolutionOfExternalEntities(DocumentBuilderFactory factory)
    {
        // prohibit the use of all protocols by external entities:
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    }

    public static void limitResolutionOfExternalEntities(TransformerFactory factory)
    {
        // to be compliant, prohibit the use of all protocols by external entities:
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
    }

    private XMLSecurityUtils()
    {
        throw new IllegalStateException();
    }
}
