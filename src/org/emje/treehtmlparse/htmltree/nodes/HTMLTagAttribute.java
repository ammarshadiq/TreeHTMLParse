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

import javax.swing.Icon;
import org.jdesktop.application.ResourceMap;
import org.emje.treehtmlparse.TreeHTMLParseView;

public class HTMLTagAttribute {

    private String AttributeName;
    private String AttributeValue;
    private Icon icon;

    public HTMLTagAttribute(String AttributeName, String AttributeValue){
        this.AttributeName = AttributeName;
        this.AttributeValue = AttributeValue;
        ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).getContext().getResourceMap(TreeHTMLParseView.class);
        this.icon = resourceMap.getIcon("HTMLComponentItems.HTMLTagAttribute");
    }

    /**
     * @return the AttributeName
     */
    public String getAttributeName() {
        return AttributeName;
    }

    /**
     * @param AttributeName the AttributeName to set
     */
    public void setAttributeName(String AttributeName) {
        this.AttributeName = AttributeName;
    }

    /**
     * @return the AttributeValue
     */
    public String getAttributeValue() {
        return AttributeValue;
    }

    /**
     * @param AttributeValue the AttributeValue to set
     */
    public void setAttributeValue(String AttributeValue) {
        this.AttributeValue = AttributeValue;
    }

    /**
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    public String toString(){
        return AttributeName;
    }
}
