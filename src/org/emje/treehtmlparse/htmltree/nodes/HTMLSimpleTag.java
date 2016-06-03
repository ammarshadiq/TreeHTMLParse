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

import java.util.Enumeration;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.Icon;
import javax.swing.text.SimpleAttributeSet;
import org.emje.treehtmlparse.htmltree.HTMLTreeNode;

public class HTMLSimpleTag extends HTMLTreeNode implements HTMLComponent{
    private javax.swing.text.html.HTML.Tag tag = null;
    private javax.swing.Icon icon = null;
    private javax.swing.text.MutableAttributeSet attributes = null;

    public HTMLSimpleTag(javax.swing.text.html.HTML.Tag tag, javax.swing.text.MutableAttributeSet attributes){
        this(
                tag,
                attributes,
                org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("HTMLComponentItems.HTMLSimpleTag"));
    }

    public HTMLSimpleTag(javax.swing.text.html.HTML.Tag tag, javax.swing.text.MutableAttributeSet attributes, javax.swing.Icon icon){
        this.tag = tag;
        this.attributes = new SimpleAttributeSet(attributes);
        this.icon = icon;
    }

    public String getTagName(){
        return tag.toString();
    }

    public HTMLTagAttribute[] getHTMLTagAttributes(){
        HTMLTagAttribute[] HTMLTagAttributes = new HTMLTagAttribute[this.attributes.getAttributeCount()];
        int counter = 0;
        java.util.Enumeration object = this.attributes.getAttributeNames();
        while(object.hasMoreElements()){
            Object o = object.nextElement();
            HTMLTagAttribute HTMLTagAttribute = new HTMLTagAttribute(o.toString(),this.attributes.getAttribute(o).toString());
            HTMLTagAttributes[counter] = HTMLTagAttribute;
            counter += 1;
        }
        return HTMLTagAttributes;
    }

    public String[] getAttributesNameAndValue(){
        String[] attributeNameAndValues = new String[this.attributes.getAttributeCount()];
        int counter = 0;
        java.util.Enumeration object = this.attributes.getAttributeNames();
        while(object.hasMoreElements()){
            Object o = object.nextElement();
            attributeNameAndValues[counter] = o.toString()+"=\""+this.attributes.getAttribute(o).toString()+"\"";
            counter += 1;
        }
        return attributeNameAndValues;
    }

    public String[] getAttributeNames(){
        String[] attributeNames = new String[this.attributes.getAttributeCount()];
        int counter = 0;
        java.util.Enumeration object = this.attributes.getAttributeNames();
        while(object.hasMoreElements()){
            Object o = object.nextElement();
            attributeNames[counter] = o.toString();
            counter += 1;
        }
        return attributeNames;
    }

    public boolean isContainAttribute(String attributeName){
        java.util.Enumeration object = this.attributes.getAttributeNames();
        while(object.hasMoreElements()){
            Object o = object.nextElement();
            if(attributeName.equalsIgnoreCase(o.toString())){
                return true;
            }
        }
        return false;
    }

    public boolean isContainAttribute(String attributeName, String attributeValue){
        java.util.Enumeration object = this.attributes.getAttributeNames();
        while(object.hasMoreElements()){
            Object o = object.nextElement();
            if(attributeName.equalsIgnoreCase(o.toString()) && attributeValue.equalsIgnoreCase(this.attributes.getAttribute(o).toString())){
                return true;
            }
        }
        return false;
    }

    public String[] getAttributeValues(){
        String[] attributeValues = new String[this.attributes.getAttributeCount()];
        int counter = 0;
        java.util.Enumeration object = this.attributes.getAttributeNames();
        while(object.hasMoreElements()){
            Object o = object.nextElement();
            attributeValues[counter] = this.attributes.getAttribute(o).toString();
            counter += 1;
        }
        return attributeValues;
    }

    public String getHTMLTagRepresentation(){
        String HTMLTagRepresantion = "<"+ tag.toString();
        java.util.Enumeration object = this.attributes.getAttributeNames();
        while(object.hasMoreElements()){
            Object o = object.nextElement();
            HTMLTagRepresantion += " "+o.toString()+"=\""+this.attributes.getAttribute(o).toString()+"\"";
//            HTMLTagRepresantion += " "+o.toString()+"=\""+replaceIllegalChar(this.attributes.getAttribute(o).toString())+"\"";
        }
        HTMLTagRepresantion += "/>";
        return HTMLTagRepresantion;
    }

    public int getAttributeCount(){
        return this.attributes.getAttributeCount();
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public MutableAttributeSet getAttributes() {
        return attributes;
    }

    public void setAttributes(MutableAttributeSet attributes) {
        this.attributes = attributes;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getPreferedAttribute(){
        String preferedAttribute = "";
        if(tag.toString().equalsIgnoreCase("meta")){
            Enumeration object = attributes.getAttributeNames();
            while(object.hasMoreElements()){
                Object o = object.nextElement();
                if(o.toString().equalsIgnoreCase("name")){
                    preferedAttribute = "name='"+attributes.getAttribute(o).toString()+"'";
                    break;
                }
                else if(o.toString().equalsIgnoreCase("http-equiv")){
                    preferedAttribute = "http-equiv='"+attributes.getAttribute(o).toString()+"'";
                    break;
                }
            }
        }
        else if(tag.toString().equalsIgnoreCase("link")){
            Enumeration object = attributes.getAttributeNames();
            while(object.hasMoreElements()){
                Object o = object.nextElement();
                if(o.toString().equalsIgnoreCase("rel")){
                    preferedAttribute = "rel='"+attributes.getAttribute(o).toString()+"'";
                    break;
                }
            }
        }
        return preferedAttribute;
    }

//    public void createXPathNames(){
//        this.generateXPathNames(this.tag.toString());
//        this.addXPathName("[@"+this.getPreferedAttribute()+"]");
//    }

    @Override
    public String toString(){
        String tagName = tag.toString();
        if (!this.getPreferedAttribute().isEmpty()) {
            return "<html>" + tagName + "<font color=gray> " + this.getPreferedAttribute() + "</font>" + "</html>";
        } else {
            return tagName;
        }
//        String tagName = tag.toString();
//        if(tag.toString().equalsIgnoreCase("meta")){
//            Enumeration object = attributes.getAttributeNames();
//            while(object.hasMoreElements()){
//                Object o = object.nextElement();
//                if(o.toString().equalsIgnoreCase("name")){
//                    tagName += " <font color=gray> name="+attributes.getAttribute(o).toString() +"</font>";
//                    break;
//                }
//                else if(o.toString().equalsIgnoreCase("http-equiv")){
//                    tagName += " <font color=gray> http-equiv="+attributes.getAttribute(o).toString() +"</font>";
//                    break;
//                }
//            }
//        }
//        if(tag.toString().equalsIgnoreCase("link")){
//            Enumeration object = attributes.getAttributeNames();
//            while(object.hasMoreElements()){
//                Object o = object.nextElement();
//                if(o.toString().equalsIgnoreCase("rel")){
//                    tagName += " <font color=gray> rel="+attributes.getAttribute(o).toString() +"</font>";
//                    break;
//                }
//            }
//        }
//        return "<html>"+ tagName + "</html>";
    }
}
