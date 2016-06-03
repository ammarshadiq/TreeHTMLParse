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

import java.util.Enumeration;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import org.emje.treehtmlparse.htmltree.nodes.HTMLComment;
import org.emje.treehtmlparse.htmltree.nodes.HTMLParsingError;
import org.emje.treehtmlparse.htmltree.HTMLTreeNode;
import org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag;
import org.emje.treehtmlparse.htmltree.nodes.HTMLTag;
import org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute;
import org.emje.treehtmlparse.htmltree.nodes.HTMLText;
import org.emje.treehtmlparse.htmltree.nodes.HTMLURL;

/**
 * HTML parsing proceeds by calling a callback for
 * each and every piece of the HTML do*****ent.  This
 * simple callback class simply prints an indented
 * structural listing of the HTML data.
 */

public class HTMLParseLister extends HTMLEditorKit.ParserCallback{
    private HTMLTreeNode rootNode;
    private HTMLTreeNode CurrentNode;

    public HTMLParseLister(String URL){
        this.rootNode = new HTMLTreeNode(new HTMLURL(URL));
        this.CurrentNode = rootNode;
    }

    @Override
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        HTMLTreeNode newNode = new HTMLTreeNode(new HTMLTag(t, a));
        this.CurrentNode.add(newNode);
        this.CurrentNode = (HTMLTreeNode) newNode;

        // for each attribute value, attribute name and its value
        Enumeration object = a.getAttributeNames();
        while(object.hasMoreElements()){
            Object o = object.nextElement();
            HTMLTagAttribute HTMLTagAttribute = new HTMLTagAttribute(o.toString(),a.getAttribute(o).toString());
            HTMLTreeNode newAttributeNode = new HTMLTreeNode(HTMLTagAttribute);
            this.CurrentNode.add(newAttributeNode);
        }

    }

    @Override
    public void handleEndTag(HTML.Tag t, int pos) {
            this.CurrentNode = (HTMLTreeNode) CurrentNode.getParent();
    }

    @Override
    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        HTMLTreeNode newNode = new HTMLTreeNode(new HTMLSimpleTag(t, a));

        // for each attribute value, attribute name and its value
        Enumeration object = a.getAttributeNames();
        while(object.hasMoreElements()){
            Object o = object.nextElement();
            HTMLTagAttribute HTMLTagAttribute = new HTMLTagAttribute(o.toString(),a.getAttribute(o).toString());
            HTMLTreeNode newAttributeNode = new HTMLTreeNode(HTMLTagAttribute);
            newNode.add(newAttributeNode);
        }
        this.CurrentNode.add(newNode);
    }

    @Override
    public void handleText(char[] data, int pos) {
        this.CurrentNode.add(new HTMLTreeNode(new HTMLText(new String(data))));
    }

    @Override
    public void handleComment(char[] data, int pos) {
//        if(data.length != 0)
//            this.CurrentNode.add(new HTMLTreeNode(new HTMLComment(new String(data))));
    }

    @Override
    public void handleError(String errorMsg, int pos){
//        this.CurrentNode.add(new HTMLTreeNode(new HTMLParsingError(errorMsg)));
    }

    /**
     * @return the rootNode
     */
    public HTMLTreeNode getRootNode() {
        return rootNode;
    }

    /**
     * @param rootNode the rootNode to set
     */
    public void setRootNode(HTMLTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public void setXMLHeader(String XMLHeader){
        HTMLText A = new HTMLText(XMLHeader);
        A.setObjectName("XML Header");
        HTMLTreeNode node = new HTMLTreeNode(A);
        this.rootNode.add(node);
    }

    public void setDocumentType(String documentHeader){
        HTMLText A = new HTMLText(documentHeader);
        A.setObjectName("Document Header");
        HTMLTreeNode node = new HTMLTreeNode(A);
        this.rootNode.add(node);
    }
}