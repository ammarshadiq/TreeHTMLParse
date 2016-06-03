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

package org.emje.treehtmlparse.xpathtree.nodes;

import javax.swing.Icon;

public class XPathTreeEntryNode extends Entry {
    private Icon icon = null;

    public XPathTreeEntryNode() {
        this(
            "New Entry",
            "xpath.string",
            org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.Entry")
                    );
    }

    public XPathTreeEntryNode(String xPathExpression, String expressionType) {
        super(xPathExpression, expressionType);
        Icon icn;
        if (expressionType.equalsIgnoreCase(Entry.XPATH_STRING_ENTRY)){
            icn = org.jdesktop.application.Application.getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.entry-xstring");
        } else if (expressionType.equalsIgnoreCase(Entry.XPATH_NORMALIZED_STRING_ENTRY)){
            icn = org.jdesktop.application.Application.getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.entry-xstring-normalized");
        } else if (expressionType.equalsIgnoreCase(Entry.XPATH_NODE_ENTRY)){
            icn = org.jdesktop.application.Application.getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.entry-xnode");
        } else if (expressionType.equalsIgnoreCase(Entry.XPATH_NODESET_ENTRY)){
            icn = org.jdesktop.application.Application.getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.entry-xnodeset");
        } else if (expressionType.equalsIgnoreCase(Entry.XPATH_NUMBER_ENTRY)){
            icn = org.jdesktop.application.Application.getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.entry-xnumber");
        } else if (expressionType.equalsIgnoreCase(Entry.XPATH_BOOLEAN_ENTRY)){
            icn = org.jdesktop.application.Application.getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.entry-xboolean");
        } else if (expressionType.equalsIgnoreCase(Entry.STRING_ENTRY)){
            icn = org.jdesktop.application.Application.getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.entry-string");
        } else {
            icn = org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.Entry");
        }
        this.icon = icn;
    }

    public XPathTreeEntryNode(String xPathExpression, String expressionType, Icon icon) {
        super(xPathExpression, expressionType);
        this.icon = icon;
    }

    public javax.swing.Icon getIcon() {
       return icon;
    }

    public void setIcon(javax.swing.Icon icon) {
        this.icon = icon;
    }
}
