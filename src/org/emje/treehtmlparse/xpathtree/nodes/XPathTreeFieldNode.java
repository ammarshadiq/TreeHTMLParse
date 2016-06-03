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

public class XPathTreeFieldNode extends Field {
    private Icon icon = null;

    public XPathTreeFieldNode(String fieldName) {
        super(fieldName, CONTINUOUS_TEXT, "");
        Icon icn;
        if (fieldName.equalsIgnoreCase("title")||
                fieldName.equalsIgnoreCase("content")){
            icn = org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.Field.Nutch");
        } else {
            icn = org.jdesktop.application.Application.getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.Field");
        }
        this.icon = icn;
    }

    public XPathTreeFieldNode(String fieldName, String fieldType) {
        super(fieldName, fieldType, "");
        Icon icn;
        if (fieldName.equalsIgnoreCase("title")||
                fieldName.equalsIgnoreCase("content")||
                fieldType.equalsIgnoreCase(Field.OUTLINKS)){
            icn = org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.Field.Nutch");
        } else {
            icn = org.jdesktop.application.Application.getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.Field");
        }
        this.icon = icn;
    }

    public XPathTreeFieldNode(String fieldName, String fieldType, String entryDelimiter){
        super(fieldName, fieldType, entryDelimiter);
        Icon icn;
        if (fieldName.equalsIgnoreCase("title")||
                fieldName.equalsIgnoreCase("content")||
                fieldType.equalsIgnoreCase(Field.OUTLINKS)){
            icn = org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.Field.Nutch");
        } else {
            icn = org.jdesktop.application.Application.getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.Field");
        }
        this.icon = icn;
    }

    public XPathTreeFieldNode(String fieldName, String fieldType, String entryDelimiter, Icon icon){
        super(fieldName, fieldType, entryDelimiter);
        this.icon = icon;
    }

    public javax.swing.Icon getIcon() {
       return icon;
    }

    public void setIcon(javax.swing.Icon icon) {
        this.icon = icon;
    }
}
