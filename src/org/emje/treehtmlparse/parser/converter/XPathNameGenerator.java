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

package org.emje.treehtmlparse.parser.converter;

import java.util.Enumeration;
import org.emje.treehtmlparse.htmltree.HTMLTreeNode;

public class XPathNameGenerator {

    // <editor-fold defaultstate="collapsed" desc="XPathName Generator">
    /**
     * Generates and add xPathName to {@code xPathNames} with : <br/>
     * <ul>
     *  <li>{@code tagName}</li>
     *  <li>{@code tagName} with [index] </li>
     * </ul>
     */
    public void generateChildsXPathNames(HTMLTreeNode node) {
        if (node.isLeaf()) {
            executeGenerateXPathNames(node);
        } else {
            executeGenerateXPathNames(node);

            // recursively explore all the node
            Enumeration en = node.children();
            while (en.hasMoreElements()) {
                generateChildsXPathNames((HTMLTreeNode) en.nextElement());
            }
        }
    }

    /**
     * Generates and add xPathName to {@code xPathNames} with : <br/>
     * <ul>
     *  <li>{@code tagName}</li>
     *  <li>{@code tagName} with [index] </li>
     * </ul>
     */
    public void generateXPathNames(HTMLTreeNode node, String tagName) {
        if (node.getParent() != null) {
            HTMLTreeNode checkNodeparent = (HTMLTreeNode) node.getParent();
            int counter;
            // check what tag names parent have
            if (checkNodeparent != null) {
                // if this tag name is already on parent
                if (checkNodeparent.isChildTagNameCounterMapContains(tagName)) {
                    // get the counter value
                    counter = checkNodeparent.getChildTagNameCounter(tagName);
                    // update the counter on parent node with XMLHeaderAndDocumentType new value
                    checkNodeparent.updateChildTagNameCounter(tagName, counter + 1);
                    // add the entry on this object xPathNames
                    node.addXPathName(tagName + "[" + counter + "]");
                } // if this tag name is not on parent yet
                else {
                    checkNodeparent.getChildTagNameCounter().put(tagName, 1);
                    node.addXPathName(tagName + "[1]");
                }
            }
            node.addXPathName(tagName);
        }
    }

    /**
     * Generates and add xPathName to {@code HTMLTreeNode}
     */
    private void executeGenerateXPathNames(HTMLTreeNode node) {
        String namaObjectPilihan = node.getUserObject().getClass().getName();
        if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLTag")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLTag HTMLTag = (org.emje.treehtmlparse.htmltree.nodes.HTMLTag) node.getUserObject();
            generateXPathNames(node, HTMLTag.getTagName());
            if (!HTMLTag.getPreferedAttribute().isEmpty()) {
                node.addXPathName(HTMLTag.getTagName() + "[@" + HTMLTag.getPreferedAttribute() + "]");
            }
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag HTMLSimpleTag = (org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag) node.getUserObject();
            generateXPathNames(node, HTMLSimpleTag.getTagName());
            if (!HTMLSimpleTag.getPreferedAttribute().isEmpty()) {
                node.addXPathName(HTMLSimpleTag.getTagName() + "[@" + HTMLSimpleTag.getPreferedAttribute() + "]");
            }
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLText")) {
            node.addXPathName("text()");
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute HTMLTagAttribute = (org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute) node.getUserObject();
            node.addXPathName("@" + HTMLTagAttribute.getAttributeName());
        }
    }// </editor-fold>
}
