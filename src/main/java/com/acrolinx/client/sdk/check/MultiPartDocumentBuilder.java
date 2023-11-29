/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.client.sdk.check;

import com.acrolinx.client.sdk.exceptions.AcrolinxException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class MultiPartDocumentBuilder {
  private static final Logger logger = LoggerFactory.getLogger(MultiPartDocumentBuilder.class);

  private final org.w3c.dom.Document document;
  private final Element root;

  public MultiPartDocumentBuilder(
      String rootElement, @Nullable String publicId, @Nullable String systemId)
      throws AcrolinxException {
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    documentFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    documentFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    DocumentBuilder documentBuilder;
    try {
      documentBuilder = documentFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      logger.debug("Unable to create Document Builder Factory.");
      throw new AcrolinxException(e);
    }

    this.document = documentBuilder.newDocument();

    if (publicId != null || systemId != null) {
      logger.debug("Doctype Public Id: {}", publicId);
      logger.debug("Doctype system Id: {}", systemId);

      DOMImplementation implementation = this.document.getImplementation();
      DocumentType documentType =
          implementation.createDocumentType(rootElement, publicId, systemId);
      this.document.appendChild(documentType);
    }

    this.root = this.document.createElement(rootElement);
    this.document.appendChild(this.root);
  }

  public MultiPartDocumentBuilder(String rootElement) throws AcrolinxException {
    this(rootElement, null, null);
  }

  public MultiPartDocumentBuilder addDocumentPart(
      String partName, String content, @Nullable Map<String, String> attributes) {
    Element element = this.document.createElement(partName);

    if (attributes != null) {
      logger.debug("Adding attributes to node");

      for (Map.Entry<String, String> entry : attributes.entrySet()) {
        Attr attribute = this.document.createAttribute(entry.getKey());
        attribute.setValue(entry.getValue());
        element.setAttributeNode(attribute);
      }
    }

    Text textNode = this.document.createTextNode(content);
    element.appendChild(textNode);
    this.root.appendChild(element);

    return this;
  }

  public MultiPartDocumentBuilder addDocumentNode(String xml, @Nullable String encoding)
      throws AcrolinxException {
    Element node;
    try {
      logger.debug("Encoding specified? {}", encoding == null ? "Not specified" : encoding);
      final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
      node =
          documentBuilderFactory
              .newDocumentBuilder()
              .parse(new ByteArrayInputStream(xml.getBytes(encoding == null ? "UTF-8" : encoding)))
              .getDocumentElement();
    } catch (SAXException | IOException | ParserConfigurationException e) {
      logger.debug("Parsing node content failed.");
      throw new AcrolinxException(e);
    }

    Node importedNode = this.document.importNode(node, true);
    this.root.appendChild(importedNode);

    return this;
  }

  public Document build() throws AcrolinxException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    try {
      logger.debug("Applying transformation to XML.");
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

      DocumentType doctype = this.document.getDoctype();

      if (doctype != null) {
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
      }

      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(this.document), new StreamResult(writer));
      return new SimpleDocument(writer.getBuffer().toString());
    } catch (TransformerException e) {
      logger.debug("Creating XML string from document failed.");
      throw new AcrolinxException(e);
    }
  }
}
