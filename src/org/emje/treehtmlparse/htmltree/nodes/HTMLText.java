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
package org.emje.treehtmlparse.htmltree.nodes;

import org.emje.treehtmlparse.htmltree.HTMLTreeNode;

public class HTMLText extends HTMLTreeNode{
    private String text;
    private javax.swing.Icon icon;

    public HTMLText(String text){
        this(
                text,
                org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("HTMLComponentItems.HTMLText"));
    }

    public HTMLText(String text, javax.swing.Icon icon){
        this.text = text;
        this.icon = icon;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
//        return replaceAwkwardChar(text);
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
   
    /**
     * @return the icon
     */
    public javax.swing.Icon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(javax.swing.Icon icon) {
        this.icon = icon;
    }

    @Override
    public String toString(){
        if(super.ObjectName != null)
            return super.ObjectName;
        else
            return "text";
    }

}
