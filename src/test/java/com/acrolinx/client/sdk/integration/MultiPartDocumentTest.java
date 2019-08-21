/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */
package com.acrolinx.client.sdk.integration;

import com.acrolinx.client.sdk.check.MultiPartAcrolinxDocument;
import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import com.acrolinx.client.sdk.integration.common.IntegrationTestBase;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class MultiPartDocumentTest extends IntegrationTestBase {

    @Test
    public void testMultiPartDocumentCreation() throws ParserConfigurationException, AcrolinxException {
        MultiPartAcrolinxDocument multiPartAcrolinxDocument = new MultiPartAcrolinxDocument("sample.xml", "test");
        multiPartAcrolinxDocument.addDocumentPart("element", "This text contains errorss", null);
        String content = multiPartAcrolinxDocument.getContent();

        assertNotNull(content);
    }

    @Test
    public void testMultiPartDocumentCreationWithAttributes() throws ParserConfigurationException, AcrolinxException {
        MultiPartAcrolinxDocument multiPartAcrolinxDocument = new MultiPartAcrolinxDocument("sample.xml", "test");
        Map<String, String> attributes = new HashMap<>();
        attributes.put("attr", "val");
        attributes.put("attr2", "val2");

        multiPartAcrolinxDocument.addDocumentPart("element", "This text contains errorss", attributes);
        String content = multiPartAcrolinxDocument.getContent();

        assertNotNull(content);
        assertTrue(content.contains("val2"));
        assertTrue(content.contains("val"));
    }

    @Test
    public void testDoctypeIsSetCorrectly() throws ParserConfigurationException, AcrolinxException {
        MultiPartAcrolinxDocument multiPartAcrolinxDocument =
                new MultiPartAcrolinxDocument("test",
                        "acroldocument",
                        "-//Acrolinx/DTD Acrolinx Integration v2//EN",
                        "https://acrolinx,com/dtd/acrolinx.dtd");


        multiPartAcrolinxDocument.addDocumentPart("element", "This text contains errorss", null);
        String content = multiPartAcrolinxDocument.getContent();

        assertNotNull(content);
    }


}
