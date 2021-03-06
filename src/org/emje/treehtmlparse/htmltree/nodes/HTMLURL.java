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

import java.net.URL;
import javax.swing.Icon;
import org.emje.treehtmlparse.htmltree.HTMLTreeNode;

public class HTMLURL extends HTMLTreeNode{
    private URL URL;
    private String URLString;
    private String hostName;
    private URL favIconLocation;
    private Icon favIcon;
    private Icon icon;

    public HTMLURL(String URLString){
        this.URLString = URLString;
        this.icon = org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class).
                    getIcon("HTMLComponentItems.HTMLURL");
    }

    public void setURL(URL URL){
        this.URL = URL;
    }

    public URL getURL(){
        return URL;
    }

    /**
     * @return the URLString
     */
    public String getURLString() {
        return URLString;
    }

    /**
     * @param URLString the URLString to set
     */
    public void setURLString(String URLString) {
        this.URLString = URLString;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the favIconLocation
     */
    public URL getFavIconLocation() {
        return favIconLocation;
    }

    /**
     * @param favIconLocation the favIconLocation to set
     */
    public void setFavIconLocation(URL favIconLocation) {
        this.favIconLocation = favIconLocation;
    }

    /**
     * @return the favIcon
     */
    public Icon getFavIcon() {
        return favIcon;
    }

    /**
     * @param favIcon the favIcon to set
     */
    public void setFavIcon(Icon favIcon) {
        this.favIcon = favIcon;
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
        return URLString;
    }

}
