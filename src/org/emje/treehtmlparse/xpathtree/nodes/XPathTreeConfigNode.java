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

public class XPathTreeConfigNode extends Config {

    private Icon icon = null;
    
    public XPathTreeConfigNode(String name) {
        this(
            name,
            null,
            null,
            "tagsoup",
            org.jdesktop.application.Application.
                getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                getContext().
                getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                getIcon("XPathComponentItems.Config")
        );
    }

    public XPathTreeConfigNode(String name, String hostName) {
        this(
            name,
            hostName,
            null,
            "tagsoup",
            org.jdesktop.application.Application.
                getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                getContext().
                getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                getIcon("XPathComponentItems.Config")
        );
    }

    public XPathTreeConfigNode(String name, String sourceURL, String hostName) {
        this(
            name,
            hostName,
            sourceURL,
            "tagsoup",
            org.jdesktop.application.Application.
                getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                getContext().
                getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                getIcon("XPathComponentItems.Config")
        );
    }

    public XPathTreeConfigNode(String name, String sourceURL, String hostName, String parser) {
        this(
            name,
            hostName,
            sourceURL,
            parser,
            org.jdesktop.application.Application.
                getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                getContext().
                getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                getIcon("XPathComponentItems.Config")
        );
    }

    public XPathTreeConfigNode(String name, String hostName, String sourceURL, String parser, Icon icon) {
        super(name,hostName,sourceURL, parser);
        this.icon = icon;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
    
    @Override
    public XPathTreeConfigNode clone(){
        XPathTreeConfigNode c = new XPathTreeConfigNode(this.name);
        c.setSourceURL(this.sourceURL);
        c.setHostName(this.hostName);
        c.setParser(this.parser);

        // Remove List Data
        Object[] removeDatalistModel = new Object[this.removedDataListModel.getSize()];
        this.removedDataListModel.copyInto(removeDatalistModel);

        // put the values
        for (int i = 0; i < removeDatalistModel.length; i++) {
            c.removedDataListModel.addElement(removeDatalistModel[i]);
        }

        //Replace Table Data
        Object[][] replaceDataTableModelDataVector = new Object[replacedDataTableModel.getRowCount()][replacedDataTableModel.getColumnCount()];
        for (int i = 0; i < replacedDataTableModel.getRowCount(); i++) {
            for (int j = 0; j < replacedDataTableModel.getColumnCount(); j++) {
                replaceDataTableModelDataVector[i][j] = replacedDataTableModel.getValueAt(i, j);
            }
        }

        // Replace Table Column
        Object[] replaceDataTableModelcolumnIdentifier = new Object[replacedDataTableModel.getColumnCount()];
        for (int k = 0; k < replacedDataTableModel.getColumnCount(); k++) {
            replaceDataTableModelcolumnIdentifier[k] = replacedDataTableModel.getColumnName(k);
        }

        // put the values
        c.replacedDataTableModel.setDataVector(replaceDataTableModelDataVector, replaceDataTableModelcolumnIdentifier);

        // Accepted URL
        Object[] acceptedURLlistModel = new Object[this.acceptedURLListModel.getSize()];
        this.acceptedURLListModel.copyInto(acceptedURLlistModel);
        // put the values
        for (int i = 0; i < acceptedURLlistModel.length; i++) {
            c.acceptedURLListModel.addElement(acceptedURLlistModel[i]);
        }

        // Skipped URL
        Object[] skippedURLlistModel = new Object[this.skippedURLListModel.getSize()];
        this.skippedURLListModel.copyInto(skippedURLlistModel);
        // put the values
        for (int i = 0; i < skippedURLlistModel.length; i++) {
            c.skippedURLListModel.addElement(skippedURLlistModel[i]);
        }        
        
        c.icon = this.icon;
        
        return c;
    }
}
