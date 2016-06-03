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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class HTMLTreeNode extends DefaultMutableTreeNode {
    protected String ObjectName = null;
    protected ArrayList<String> xPathNames = new ArrayList<String>();
    protected boolean isXPathNamesIsGenerated = false;
    protected HashMap<String, Integer> childTagNameCounterMap = new HashMap<String, Integer>();
    private String contentStrings = "";
    private int indentSize = 0;
    private String indentString = "";

    public HTMLTreeNode() {
        this(null);
    }

    public HTMLTreeNode(Object userObject) {
        this(userObject, true);
    }

    public HTMLTreeNode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }

    /**
     * Returns the code representation for this node. 
     * It's exploring all of this node contents by executing {@code exploreContent} function
     * if this node is an {@code HTMLTagAttribute} node type, it only returns Attribute Value.
     *
     * @return the String representing HTML code for this node and it's children's. Returns AttributeValue for a TagAttribute Node type
     */
    public String getContentStrings(){
        // if this node is an TagAttribute, only returns the Attribute Value
        if(this.getUserObject().getClass().getName().matches("org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute")){
                org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute HTMLTagAttribute = (org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute) this.getUserObject();
                return  HTMLTagAttribute.getAttributeValue();
        }
        else if (this.contentStrings.isEmpty()){
            this.contentStrings = exploreContent(this);
            return this.contentStrings;
        }
        else{
            return this.contentStrings;
        }
    }

    /**
     * Returns the code representation for this {@code rootNode}.
     * It's recursively explore all its children for retrieving the String representation by executing {@code getHTMLComponentContents} function
     *
     * @return HTML Code representation for rootNode and all of its children.
     */
    private String exploreContent(TreeNode rootNode){
        HTMLTreeNode node = (HTMLTreeNode) rootNode;
        // if this node is a leaf node (node that doesn't have any children)
        if(rootNode.isLeaf()){
            // get contentString, update it
            this.contentStrings += getHTMLComponentContents(node);
        }
        else{
            // get contentString, update it
            this.contentStrings += getHTMLComponentContents(node);

            // recursively explore all the node
            Enumeration en = rootNode.children();
            while(en.hasMoreElements()){
                exploreContent((TreeNode) en.nextElement());
            }
            //add an end tag
            this.contentStrings += getEndTag(node);
        }
        return contentStrings;
    }

    /**
     * Returns the code representation for this {@code retval} node Component. the returned string
     * is added by indent string for clean and sleek code representation.
     *
     * @return HTML Code representation for {@code retval} node.
     */
    private String getHTMLComponentContents(TreeNode retval){
        HTMLTreeNode node = (HTMLTreeNode) retval;
        if(node.getUserObject().getClass().getName().matches("org.emje.treehtmlparse.htmltree.nodes.HTMLText")){
            org.emje.treehtmlparse.htmltree.nodes.HTMLText HTMLText = (org.emje.treehtmlparse.htmltree.nodes.HTMLText) node.getUserObject();
            return indentString + HTMLText.getText() + "\n";
        }
        else if(node.getUserObject().getClass().getName().matches("org.emje.treehtmlparse.htmltree.nodes.HTMLTag")){
            org.emje.treehtmlparse.htmltree.nodes.HTMLTag HTMLTag = (org.emje.treehtmlparse.htmltree.nodes.HTMLTag) node.getUserObject();
            String temp = indentString + HTMLTag.getHTMLTagRepresentation()+"\n";

            indent();
            pIndent();
            return temp;
        }
        else if(node.getUserObject().getClass().getName().matches("org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag")){
            org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag HTMLSimpleTag = (org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag) node.getUserObject();
            return indentString + HTMLSimpleTag.getHTMLTagRepresentation()+"\n";
        }
        else if(node.getUserObject().getClass().getName().matches("org.emje.treehtmlparse.htmltree.nodes.HTMLComment")){
            org.emje.treehtmlparse.htmltree.nodes.HTMLComment HTMLComment = (org.emje.treehtmlparse.htmltree.nodes.HTMLComment) node.getUserObject();
            String comment = HTMLComment.getText().replaceAll("\n", "\n"+indentString);
            return indentString + comment +"\n";
        }
        else{
            return "";
        }
    }

    /**
     *
     * @return enclosing tag for {@code retval} node component.
     */
    private String getEndTag(TreeNode retval){
        HTMLTreeNode node = (HTMLTreeNode) retval;
        // only for TAG Component
        if(node.getUserObject().getClass().getName().matches("org.emje.treehtmlparse.htmltree.nodes.HTMLTag")){
            org.emje.treehtmlparse.htmltree.nodes.HTMLTag HTMLTag = (org.emje.treehtmlparse.htmltree.nodes.HTMLTag) node.getUserObject();
            unIndent();
            pIndent();
            return indentString + HTMLTag.getEnclosingTag() + "\n";
        }
        return "";
    }

    public String getCheckedContentString(){

        return null;
    }

    // If you want to change "isSelected" by CellEditor,
    /*
    public void setUserObject(Object obj) { if (obj instanceof Boolean) {
        * setSelected(((Boolean)obj).booleanValue()); } else {
        * super.setUserObject(obj); } }
        */
    // <editor-fold defaultstate="collapsed" desc="Update Indent Function">
    private void indent() {
        indentSize += 1;
    }
    
    private void unIndent() {
        indentSize -= 1; if (indentSize < 0) indentSize = 0;
    }

    private void pIndent() {
        indentString = "";
            for(int i = 0; i < indentSize; i++) indentString = indentString+"   ";
    }// </editor-fold>

    public HashMap<String, Integer> getChildTagNameCounter(){
        return this.childTagNameCounterMap;
    }

    public boolean isChildTagNameCounterMapContains(String tagName){
        if(childTagNameCounterMap.containsKey(tagName))
            return true;
        else
            return false;
    }

    public int getChildTagNameCounter(String tagName){
        return this.childTagNameCounterMap.get(tagName);
    }

    public void updateChildTagNameCounter (String tagName, Integer newValue){
        this.childTagNameCounterMap.remove(tagName);
        this.childTagNameCounterMap.put(tagName, newValue);
    }

    public void addXPathName(String xPathName){
        this.xPathNames.add(xPathName);
    }

    public ArrayList<String> getXPathNames(){
        return this.xPathNames;
    }

    public ArrayList<String> getXpathLocations(){
        if(!this.xPathNames.isEmpty()){
            ArrayList<String> pathList = new ArrayList<String>();
            ArrayList<String> tempList;
            TreeNode[] nodes = this.getPath();
            for(int i = 1; i < nodes.length; i++) {
                tempList = new ArrayList<String>();
                HTMLTreeNode a = (HTMLTreeNode) nodes[i];
                ArrayList<String> b = a.getXPathNames();
                for (String x : b) {
                    if(pathList.isEmpty()){
                        tempList.add("/"+x);
                    }else{
                        for (String y : pathList) {
                            tempList.add(y+"/"+x);
                        }
                    }
                }
                pathList = tempList;
            }
            return pathList;
        }
        return null;
    }

    public void setObjectName(String name){
        this.ObjectName = name;
    }
}
