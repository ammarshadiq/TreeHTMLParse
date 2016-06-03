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

package org.emje.treehtmlparse.xpathtree;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * treehtmlparse.xpathtree.XPathTreeCellRenderer
 */
public class XPathTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        setIcon(getCustomIcon(value));
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        setIcon(getCustomIcon(value));

        return this;
    }
    private Icon getCustomIcon(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        String namaObjectPilihan = node.getUserObject().getClass().getName();
        if (namaObjectPilihan.matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode")) {
            org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode Entry = (org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode) node.getUserObject();
            return ((ImageIcon) Entry.getIcon());
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeFieldNode")) {
            org.emje.treehtmlparse.xpathtree.nodes.XPathTreeFieldNode Field = (org.emje.treehtmlparse.xpathtree.nodes.XPathTreeFieldNode) node.getUserObject();
            return ((ImageIcon) Field.getIcon());
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode")) {
            org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode Config = (org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode) node.getUserObject();
            return ((ImageIcon) Config.getIcon());
        }else{
            Icon icon = null;
            if (value.toString().equalsIgnoreCase("Add new Field")){
                icon = org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.ListAddBlue");
            }
            else if(value.toString().equalsIgnoreCase("Add new Entry")){
                icon = org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("XPathComponentItems.ListAddGreen");
            }
            return icon;
        }
    }

}
