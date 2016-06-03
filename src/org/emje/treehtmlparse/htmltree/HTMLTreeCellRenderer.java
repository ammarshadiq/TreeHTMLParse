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
package org.emje.treehtmlparse.htmltree;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class HTMLTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        setIcon(getCustomIcon(value));
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        setIcon(getCustomIcon(value));

        return this;
    }

    private ImageIcon getCustomIcon(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        String objectName = node.getUserObject().getClass().getName();
        if (objectName.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLTag")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLTag HTMLTag = (org.emje.treehtmlparse.htmltree.nodes.HTMLTag) node.getUserObject();
            return ((ImageIcon) HTMLTag.getIcon());
        } else if (objectName.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag HTMLSimpleTag = (org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag) node.getUserObject();
            return ((ImageIcon) HTMLSimpleTag.getIcon());
        } else if (objectName.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLText")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLText HTMLText = (org.emje.treehtmlparse.htmltree.nodes.HTMLText) node.getUserObject();
            return ((ImageIcon) HTMLText.getIcon());
        } else if (objectName.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLComment")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLComment HTMLComment = (org.emje.treehtmlparse.htmltree.nodes.HTMLComment) node.getUserObject();
            return ((ImageIcon) HTMLComment.getIcon());
        } else if (objectName.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLParsingError")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLParsingError HTMLParsingError = (org.emje.treehtmlparse.htmltree.nodes.HTMLParsingError) node.getUserObject();
            return ((ImageIcon) HTMLParsingError.getIcon());
        } else if (objectName.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute HTMLTagAttribute = (org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute) node.getUserObject();
            return ((ImageIcon) HTMLTagAttribute.getIcon());
        } else if (objectName.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLURL")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLURL HTMLURL = (org.emje.treehtmlparse.htmltree.nodes.HTMLURL) node.getUserObject();
            return ((ImageIcon) HTMLURL.getIcon());
        }

        return null;
    }
}
