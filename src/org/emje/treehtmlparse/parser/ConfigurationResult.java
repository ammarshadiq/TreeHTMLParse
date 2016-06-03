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

import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;
import org.emje.treehtmlparse.xpathtree.XPathTreeConfigInitialNodes;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeFieldNode;
import org.w3c.dom.NodeList;

/**
 * Creating DefaultMuttableTreeNode from XPath tree and HtmlParser for displaying the result from applying each XPath Tree node to the web page source code.
 */
public class ConfigurationResult extends DefaultMutableTreeNode {

    private HtmlParse parser;

    public ConfigurationResult(HtmlParse parser){
        this.parser = parser;
    }

    public DefaultMutableTreeNode getResult(XPathTreeConfigInitialNodes tree){
        DefaultMutableTreeNode rootNode = null;
        DefaultMutableTreeNode currentFieldNode = null;
        // Get xPathTree Structure and data
        Enumeration en = tree.preorderEnumeration();
        while (en.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
            String objectName = node.getUserObject().getClass().getName();            

            // if config
            if (objectName.matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode")) {
                XPathTreeConfigNode conf = (XPathTreeConfigNode) node.getUserObject();
                rootNode = new DefaultMutableTreeNode(conf.getName());

            // if field
            } else if (objectName.matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeFieldNode")) {
                XPathTreeFieldNode field = (XPathTreeFieldNode) node.getUserObject();
                currentFieldNode = new DefaultMutableTreeNode(field.getFieldName());
                StringBuilder sb = new StringBuilder();
                Enumeration enu = node.preorderEnumeration();

                if (field.getFieldType().equals(XPathTreeFieldNode.CONTINUOUS_TEXT)) {
                    while (enu.hasMoreElements()) {
                        DefaultMutableTreeNode n = (DefaultMutableTreeNode) enu.nextElement();
                        if (n.getUserObject().getClass().getName().
                                matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode")){
                            XPathTreeEntryNode entry = (XPathTreeEntryNode) n.getUserObject();
                            sb.append(evaluateXPath(parser, entry.getxPathExpression(), entry.getExpressionType(),
                                    field.getFieldType(), field.getEntryDelimiter()));
                        }
                    }
                    if(sb.length() != 0){
                        sb.delete(sb.length()-field.getEntryDelimiter().length(), sb.length()); // remove the last delimiter
                        currentFieldNode.add(new DefaultMutableTreeNode(sb.toString().trim()));
                    }
                } else if (field.getFieldType().equals(XPathTreeFieldNode.SEGMENTED_TEXT)) {
                    while (enu.hasMoreElements()) {
                        DefaultMutableTreeNode n = (DefaultMutableTreeNode) enu.nextElement();
                        if (n.getUserObject().getClass().getName().
                                matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode")){
                            XPathTreeEntryNode entry = (XPathTreeEntryNode) n.getUserObject();
                            String res = evaluateXPath(parser, entry.getxPathExpression(), entry.getExpressionType(),
                                    field.getFieldType(), field.getEntryDelimiter());
                            currentFieldNode.add(new DefaultMutableTreeNode(res));
                        }
                    }
                } else if (field.getFieldType().equals(XPathTreeFieldNode.OUTLINKS)) {
                    sb.append("<html><table border='1'>");
                    while (enu.hasMoreElements()) {
                        DefaultMutableTreeNode n = (DefaultMutableTreeNode) enu.nextElement();
                        if (n.getUserObject().getClass().getName().
                                matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode")) {
                            XPathTreeEntryNode entry = (XPathTreeEntryNode) n.getUserObject();
                            sb.append(evaluateXPath(parser, entry.getxPathExpression(), entry.getExpressionType(),
                                    field.getFieldType(), field.getEntryDelimiter()));
                        }
                    }
                    sb.append("</table></html>");
                    currentFieldNode.add(new DefaultMutableTreeNode(sb.toString()));
                }
                rootNode.add(currentFieldNode);
            } //if entry IGNORE
        }
        return rootNode;
    }
    
    private String evaluateXPath(HtmlParse parser, String expression,
            String expressionType, String fieldType, String delimiter) {

        String result = "";

        // Evaluate XPath Expression with different returnType
        String XPathExpression = expression;
        QName returnType = null;
        Object xPathResult = null;
        Object xPathResultOutlinkAnchor = null;
        // <editor-fold defaultstate="collapsed" desc="Switch XPath Expression Return Type">
        if(expressionType.equals("xpath.string")){
            returnType = XPathConstants.STRING;
            xPathResult = parser.evaluateXPathExpression(XPathExpression, returnType);
        }
        else if(expressionType.equals("xpath.node")){
            returnType = XPathConstants.NODE;
            xPathResult = parser.evaluateXPathExpression(XPathExpression, returnType);
        }
        else if(expressionType.equals("xpath.nodeset")){
            returnType = XPathConstants.NODESET;
            xPathResult = parser.evaluateXPathExpression(XPathExpression, returnType);
        }
        else if(expressionType.equals("xpath.number")){
            returnType = XPathConstants.NUMBER;
            xPathResult = parser.evaluateXPathExpression(XPathExpression, returnType);
        }
        else if(expressionType.equals("xpath.boolean")){
            returnType = XPathConstants.BOOLEAN;
            xPathResult = parser.evaluateXPathExpression(XPathExpression, returnType);
        }
        else if(expressionType.equals("xpath.string.normalized")){
            returnType = XPathConstants.STRING;
            xPathResult = parser.evaluateXPathExpression("normalize-space(" + XPathExpression + ")", returnType);
        }
        else if(expressionType.equals("xpath.outlink.nodeset")){
            returnType = XPathConstants.NODESET;
                if (XPathExpression.endsWith("/@href")) {
                    XPathExpression = XPathExpression.substring(0, XPathExpression.length() - 6);
                }
                xPathResult = parser.evaluateXPathExpression(XPathExpression + "/@href", returnType);
                xPathResultOutlinkAnchor = parser.evaluateXPathExpression(XPathExpression + "/text()", returnType);
        }
        else if(expressionType.equals("plain.string")){
            xPathResult = expression;
        } // </editor-fold>
        if (xPathResult != null) {
            // <editor-fold defaultstate="collapsed" desc="XPATH_OUTLINK_NODESET_ENTRY">
            if (expressionType.equals(XPathTreeEntryNode.XPATH_OUTLINK_NODESET_ENTRY)) {
                NodeList list = (NodeList) xPathResult;

                ArrayList<String> urlArrayList = new ArrayList<String>();
                ArrayList<String> anchorArrayList = new ArrayList<String>();

                // fill data
                if (xPathResultOutlinkAnchor != null) {
                    NodeList anchorList = (NodeList) xPathResultOutlinkAnchor;
                    for (int i = 0; i < list.getLength(); i++) {
                        String value = list.item(i).getNodeValue();
                        String anchorValue = "  ";
                        if (i < anchorList.getLength()) {
                            anchorValue = anchorList.item(i).getNodeValue().isEmpty() ? anchorList.item(i).getNodeValue() : "";

                        }
                        if (!value.matches("\\s+")) {
                            value = list.item(i).getNodeValue().replaceAll("\\s+", " "); // normalize space
                            if (!anchorValue.matches("\\s+")) {
                                anchorValue = anchorList.item(i).getNodeValue().replaceAll("\\s+", " "); // normalize space
                                urlArrayList.add(value);
                                anchorArrayList.add(anchorValue);
                            } else {
                                urlArrayList.add(value);
                                anchorArrayList.add("");
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < list.getLength(); i++) {
                        String value = list.item(i).getNodeValue();
                        if (!value.matches("\\s+")) {
                            value = list.item(i).getNodeValue().replaceAll("\\s+", " "); // normalize space
                            urlArrayList.add(value);
                            anchorArrayList.add("");
                        }
                    }
                }
                StringBuilder esbe = new StringBuilder();
                for (int k = 0; k < urlArrayList.size(); k++) {
                    esbe.append("<tr><td>").append((String) urlArrayList.get(k)).append("</td><td>").append((String) anchorArrayList.get(k)).append("</td></tr>");
                }
                result = esbe.toString();
            }// </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="XPATH_NODESET_ENTRY">
            else if (expressionType.equals(XPathTreeEntryNode.XPATH_NODESET_ENTRY)) {
                NodeList list = (NodeList) xPathResult;
                ArrayList<String> urlArrayList = new ArrayList<String>();

                // fill data
                for (int i = 0; i < list.getLength(); i++) {
                    String value = list.item(i).getNodeValue();
                    if (value != null && !value.matches("\\s+")) {
                        value = list.item(i).getNodeValue().replaceAll("\\s+", " "); // normalize space
                        urlArrayList.add(value);
                    }
                }
                StringBuilder esbe = new StringBuilder();

                if(fieldType.equals(XPathTreeFieldNode.CONTINUOUS_TEXT)){
                    for (int k = 0; k < urlArrayList.size(); k++) {
                        esbe.append((String) urlArrayList.get(k)).append(" ");
                        // TODO : Create a dilimiter handler for nodeset
                        //esbe.append((String) urlArrayList.get(k)).append(delimiter);
                    }
                    System.out.println("esbe length: "+esbe.length());
                    if(esbe.length()>0){
                        esbe.deleteCharAt(esbe.length()-1).append(delimiter);
                    }
                } else if (fieldType.equals(XPathTreeFieldNode.SEGMENTED_TEXT)){
                    esbe.append("<html><table border='1'>");
                    for (int k = 0; k < urlArrayList.size(); k++) {
                        esbe.append("<tr><td>").append((String) urlArrayList.get(k)).append("</td></tr>");
                    }
                    esbe.append("</table></html>");
                }
                result = esbe.toString();
            }// </editor-fold>
            else {
                if (fieldType.equals(XPathTreeFieldNode.CONTINUOUS_TEXT)) {
                    result = xPathResult.toString() + delimiter;
                } else if (fieldType.equals(XPathTreeFieldNode.SEGMENTED_TEXT)) {
                    result = xPathResult.toString();
                }
            }
        }
        return result;
    }
}
