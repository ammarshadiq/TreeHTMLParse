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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.emje.treehtmlparse.xpathtree.XPathTreeConfigInitialNodes;
import org.emje.treehtmlparse.xpathtree.nodes.Config;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeFieldNode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * This class load the xml config file.
 * adapted from source created by Rida Benjelloun (rida.benjelloun@doculibre.com)
 */
public class ConfigFileParser {

    private String configURL;
    private String sourceURL;
    private Document dom;
    private XPathTreeConfigInitialNodes configTreeNode;
    private Config conf;

    public ConfigFileParser() {
        // do nothing
    }

    public ConfigFileParser(String configURL) {
        this.configURL = configURL;
    }

    public void parse() {
        //parse the xml file and get the dom object
        parseXmlFile();
        if(dom != null){
            parseDocument();
        }
    }

    private void parseXmlFile() {
        //get the factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false); // ignore namespace
        factory.setIgnoringElementContentWhitespace(true);

        try {
            //Using factory get an instance of document builder
            DocumentBuilder db = factory.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse(this.configURL);
        } catch (Exception ex) {
            Logger.getLogger(ConfigFileParser.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError creating DOM Document", "Error creating DOM Document", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void parseXmlFile(String configURL) {
        this.configURL = configURL;
        //parse the xml file and get the dom object
        parseXmlFile();
    }

    /**
     * I take a xml element and the tag name, look for the tag and get
     * the text content
     * i.e for <employee><name>John</name></employee> xml snippet if
     * the Element points to employee node and tagName is name I will return John
     * @param ele
     * @param tagName
     * @return
     */
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    }

    /**
     * Calls getTextValue and returns a int value
     * @param ele
     * @param tagName
     * @return
     */
    private int getIntValue(Element ele, String tagName) {
        //in production application you would catch the exception
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    /**
     * Calls getTextValue and returns a double value
     * @param ele
     * @param tagName
     * @return
     */
    private double getDoubleValue(Element ele, String tagName) {
        //in production application you would catch the exception
        return Double.parseDouble(getTextValue(ele, tagName));
    }

    public XPathTreeConfigInitialNodes getConfigTreeNode() {
        return configTreeNode;
    }

    public void setConfigTreeNode(XPathTreeConfigInitialNodes configTreeNode) {
        this.configTreeNode = configTreeNode;
    }

    private void parseDocument() {
        //get the root <config> element
        Element configElement = dom.getDocumentElement();
        XPathTreeConfigNode c = new XPathTreeConfigNode(getTextValue(configElement, "name"),
                getTextValue(configElement, "source"),
                getTextValue(configElement, "host"),
                getTextValue(configElement, "parser"));
        this.conf = c;
        this.configTreeNode = new XPathTreeConfigInitialNodes(c);
        this.setSourceURL(getTextValue(configElement, "source"));

        //get nodelist of <replace> elements
        NodeList replaceNL = configElement.getElementsByTagName("replace");
        if (replaceNL != null && replaceNL.getLength() > 0) {
            int colNum = c.getReplacedDataTableModel().getColumnCount();
            Object[] replaceDataTableModelcolumnIdentifiers = new Object[colNum];
            for (int k = 0; k < colNum; k++) {
                replaceDataTableModelcolumnIdentifiers[k] = c.getReplacedDataTableModel().getColumnName(k);
            }
            Object[][] replaceDataTableModelDataVector = new Object[replaceNL.getLength()][2];
            for (int i = 0; i < replaceNL.getLength(); i++) {
                Element replaceElement = (Element) replaceNL.item(i);
                // edit replace configuration on XPathTreeConfigNode class
                replaceDataTableModelDataVector[i][0] = replaceElement.getAttribute("text");
                replaceDataTableModelDataVector[i][1] = replaceElement.getAttribute("with");
            }
            c.getReplacedDataTableModel().setDataVector(replaceDataTableModelDataVector, replaceDataTableModelcolumnIdentifiers);
        }
        //get nodelist of <remove> elements
        NodeList removeNL = configElement.getElementsByTagName("remove");
        if (removeNL != null && removeNL.getLength() > 0) {
            // edit remove configuration on XPathTreeConfigNode class
            c.getRemovedDataListModel().clear();
            for (int i = 0; i < removeNL.getLength(); i++) {
                Element removeElement = (Element) removeNL.item(i);
                c.getRemovedDataListModel().addElement(removeElement.getAttribute("text"));
            }
        }

        //get nodelist of <accepturl> elements
        NodeList acceptURLNL = configElement.getElementsByTagName("accepturl");
        if (acceptURLNL != null && acceptURLNL.getLength() > 0) {
            // edit remove configuration on XPathTreeConfigNode class
            c.getAcceptedURLListModel().clear();
            for (int i = 0; i < acceptURLNL.getLength(); i++) {
                Element acceptURLElement = (Element) acceptURLNL.item(i);
                c.getAcceptedURLListModel().addElement(acceptURLElement.getAttribute("value"));
            }
        }
        //get nodelist of <skipurl> elements
        NodeList skipURLNL = configElement.getElementsByTagName("skipurl");
        if (skipURLNL != null && skipURLNL.getLength() > 0) {
            // edit remove configuration on XPathTreeConfigNode class
            c.getSkippedURLListModel().clear();
            for (int i = 0; i < skipURLNL.getLength(); i++) {
                Element skipURLElement = (Element) skipURLNL.item(i);
                c.getSkippedURLListModel().addElement(skipURLElement.getAttribute("value"));
            }
        }

        //get the <field> elements
        NodeList fieldNL = configElement.getElementsByTagName("field");

        if (fieldNL != null && fieldNL.getLength() > 0) {
            DefaultMutableTreeNode fieldTreeNode = null;
            for (int i = 0; i < fieldNL.getLength(); i++) {
                Element fieldElement = (Element) fieldNL.item(i);

                XPathTreeFieldNode f;
                if (fieldElement.getAttribute("type").equalsIgnoreCase(XPathTreeFieldNode.CONTINUOUS_TEXT)){
                    f = new XPathTreeFieldNode(fieldElement.getAttribute("name"),
                        fieldElement.getAttribute("type"),
                        fieldElement.getAttribute("delimiter"));
                } else {
                    f = new XPathTreeFieldNode(fieldElement.getAttribute("name"),
                        fieldElement.getAttribute("type"));
                }

                fieldTreeNode = new DefaultMutableTreeNode(f);
                configTreeNode.add(fieldTreeNode);
                // get nodelist of <entry> elements
                NodeList entryNL = fieldElement.getElementsByTagName("entry");
                if (entryNL != null && entryNL.getLength() > 0) {
                    DefaultMutableTreeNode entryTreeNode = null;
                    for (int j = 0; j < entryNL.getLength(); j++) {
                        Element entryElement = (Element) entryNL.item(j);

                        XPathTreeEntryNode e = new XPathTreeEntryNode(entryElement.getAttribute("value"), entryElement.getAttribute("type"));

                        entryTreeNode = new DefaultMutableTreeNode(e);
                        fieldTreeNode.add(entryTreeNode);
                    }
                }
                if(fieldTreeNode != null) fieldTreeNode.add(new DefaultMutableTreeNode("Add new Entry"));
            }
            if(configTreeNode != null) configTreeNode.add(new DefaultMutableTreeNode("Add new Field"));
        }
    }

    public String getConfigURL() {
        return configURL;
    }

    public void setConfigURL(String configURL) {
        this.configURL = configURL;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public Config getConf() {
        return conf;
    }

    public void setConf(Config conf) {
        this.conf = conf;
    }
}