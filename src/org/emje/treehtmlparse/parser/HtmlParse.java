/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the  "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Ammar Shadiq
 *
 */

package org.emje.treehtmlparse.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.jdesktop.application.ResourceMap;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.emje.treehtmlparse.htmltree.HTMLTreeNode;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode;
import org.emje.treehtmlparse.parser.converter.html2xhtml.Html2XhtmlConverter;
import org.emje.treehtmlparse.parser.converter.tagsoup.TagsoupConverter;
import org.emje.treehtmlparse.parser.converter.PageConverter;
import org.xml.sax.SAXException;

public final class HtmlParse extends HTMLTreeNode implements Runnable {
    private HTMLTreeNode rootNode;
    private Document sourceDocument;
    private XPath xPath;
    private String sourceString;

    ResourceMap rm = org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class);

    public HtmlParse(){
        // do nothing
    }
    
    public HtmlParse(String URL, XPathTreeConfigNode config){
        
        // try to set source and host name on config node.
        try {
            URL url = new URL(URL);
            config.setSourceURL(URL);
            config.setHostName(url.getHost());
        } catch (MalformedURLException ex) {/*ignore*/}

        PageConverter pc;
        if(config.getParser().equalsIgnoreCase("html2xhtml")){
            pc = new Html2XhtmlConverter();
        } else if (config.getParser().equalsIgnoreCase("tagsoup")) {
            pc = new Html2XhtmlConverter();
        } else {
            pc = new Html2XhtmlConverter();
        }
        
        org.emje.treehtmlparse.TreeHTMLParseView.appLogger.log(Level.INFO, "Restructurizing web page souce code and Generating GUI instance - started");
        pc.parse(URL, config);
        this.rootNode = pc.getRootNode();
        this.sourceString = pc.getSourceString();
        //System.out.println(this.sourceString);
        org.emje.treehtmlparse.TreeHTMLParseView.appLogger.log(Level.INFO, "Restructurizing web page souce code and Generating GUI instance - fisnished");
        try {
            org.emje.treehtmlparse.TreeHTMLParseView.appLogger.log(Level.INFO, "Parsing XML Document - started");
            this.sourceDocument = xmlDocumentBuilder(this.sourceString);
            org.emje.treehtmlparse.TreeHTMLParseView.appLogger.log(Level.INFO, "Parsing XML Document - finished");
        } catch (Exception ex) {
            Logger.getLogger(HtmlParse.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError Parsing XML Document", "Parsing XML Document Error", JOptionPane.ERROR_MESSAGE);
        }

        this.xPath = xPathBuilder();
    }

    public Object evaluateXPathExpression(String expression, QName returnType) {
        try {
            XPathExpression xPathExpression = xPath.compile(expression);
            return xPathExpression.evaluate(sourceDocument, returnType);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(HtmlParse.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError compiling expression: \n" + expression, "XPath Compiling Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public Document xmlDocumentBuilder(String cleanedSource) throws ParserConfigurationException, SAXException, IOException{
        // build the xpath
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // set Name Space Aware = false, to prevent user have to type namespace to each xpath expression segment
        // disable the features for speed
        factory.setIgnoringElementContentWhitespace(true);
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        factory.setFeature("http://xml.org/sax/features/namespaces", false);
        factory.setFeature("http://xml.org/sax/features/validation", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        DocumentBuilder builder = factory.newDocumentBuilder();

        builder.setErrorHandler(null);
        builder.setEntityResolver(null);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(cleanedSource.getBytes());

        return builder.parse(bais);
    }

    public XPath xPathBuilder(){
        return XPathFactory.newInstance().newXPath();
    }

    public String getSourceString() {
        return this.sourceString;
    }

    /**
     * @return the rootNode
     */
    public HTMLTreeNode getRootNode() {
        return rootNode;
    }

    public XPath getxPath() {
        return xPath;
    }

    public void setxPath(XPath xPath) {
        this.xPath = xPath;
    }

    public Document getXmlDocument() {
        return sourceDocument;
    }

    public void setXmlDocument(Document xmlDocument) {
        this.sourceDocument = xmlDocument;
    }

  public void run() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}