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

import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

public class Config {

    protected String name;
    protected String sourceURL;
    protected String hostName;
    protected String parser;
    protected DefaultListModel removedDataListModel = new DefaultListModel();
    protected DefaultTableModel replacedDataTableModel =
            new DefaultTableModel(new Object[][]{
                {"//<![CDATA[// <![CDATA[", "//<![CDATA["},
                {"/ ]]> //]]>", "//]]>"},
                {"//<![CDATA[//<![CDATA[", "//<![CDATA["},
                {"//]]>//]]>", "//]]>"}
            }, new String[]{"Containing Text", "Replace With"}) {

                Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class
                };

                @Override
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
            };
    protected DefaultListModel acceptedURLListModel = new DefaultListModel();
    protected DefaultListModel skippedURLListModel = new DefaultListModel();

    public Config(String name) {
        this(
            name,
            null,
            null,
            null
        );
    }

    public Config(String name, String hostName) {
        this(
            name,
            hostName,
            null,
            null
        );
    }

    public Config(String name, String hostName, String sourceURL) {
        this(
            name,
            hostName,
            null,
            null
        );
    }

    public Config(String name, String hostName, String sourceURL, String parser) {
        this.name = name;
        this.hostName = hostName;
        this.sourceURL = sourceURL;
        if(parser == null) this.parser = "tagsoup";
        else this.parser = parser;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Get the configuration name
     * @return configuration name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the configuration name
     * @param {@code name} the configuration name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Parser name
     * @return parser name
     */
    public String getParser(){
        return this.parser;
    }

    /**
     * Set the parser name
     * @param {@code parser} the parser name
     */
    public void setParser(String parser) {
        this.parser = parser;
    }

    public DefaultListModel getRemovedDataListModel(){
        return this.removedDataListModel;
    }

    public void setRemovedDataListModel(DefaultListModel dlm){
        this.removedDataListModel = dlm;
    }

    public DefaultListModel getAcceptedURLListModel(){
        return this.acceptedURLListModel;
    }

    public void setAcceptedURLListModel(DefaultListModel dlm){
        this.acceptedURLListModel = dlm;
    }

    public DefaultListModel getSkippedURLListModel(){
        return this.skippedURLListModel;
    }

    public void setSkippedURLListModel(DefaultListModel dlm){
        this.skippedURLListModel = dlm;
    }

    public DefaultTableModel getReplacedDataTableModel(){
        return this.replacedDataTableModel;
    }

    public void setReplacedDataTableModel(DefaultTableModel dtm){
        this.replacedDataTableModel = dtm;
    }

    /**
        * Clone this object
        * @return new object of this object
        */
    @Override
    public Config clone(){
        Config c = new Config(this.name);
        c.setSourceURL(this.sourceURL);
        c.setHostName(this.hostName);
        c.setParser(this.parser);
        
        // Remove List Data
        Object[] removeDatalistModel = new Object[this.removedDataListModel.getSize()];
        this.removedDataListModel.copyInto(removeDatalistModel);
        
        // put the values
        for(int i = 0; i < removeDatalistModel.length; i++){
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
        for(int i = 0; i < acceptedURLlistModel.length; i++){
            c.acceptedURLListModel.addElement(acceptedURLlistModel[i]);
        }
        
        // Skipped URL
        Object[] skippedURLlistModel = new Object[this.skippedURLListModel.getSize()];
        this.skippedURLListModel.copyInto(skippedURLlistModel);
        // put the values
        for(int i = 0; i < skippedURLlistModel.length; i++){
            c.skippedURLListModel.addElement(skippedURLlistModel[i]);
        }
        
        
        return c;
    }
    
    /**
        * This Object to String
        * @return configuration name
        */
    @Override
    public String toString() {
        return this.getName();
    }
    
}
