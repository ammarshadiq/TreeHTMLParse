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

package org.emje.treehtmlparse.parser.converter.html2xhtml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import org.jdesktop.application.ResourceMap;
import org.openide.util.Exceptions;
import org.emje.treehtmlparse.htmltree.HTMLTreeNode;
import org.emje.treehtmlparse.parser.HTMLParseLister;
import org.emje.treehtmlparse.parser.converter.ConfigAlteredSourceApl;
import org.emje.treehtmlparse.parser.converter.PageConverter;
import org.emje.treehtmlparse.parser.converter.XMLHeaderAndDocumentTypeEvaluator;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode;

public class Html2XhtmlConverter implements PageConverter {

    private HTMLTreeNode rootNode;
    private String sourceString;
    
    private ResourceMap rm = org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class);

    public void parse(String URL, XPathTreeConfigNode config) {
        try {
            // if it's XMLHeaderAndDocumentType Web URL
            if (URL.indexOf("://") > 0) {
                //System.out.println("URL Base: "+url.getHost());
                HttpURLConnection conn = (HttpURLConnection) new URL(URL).openConnection();
                // some site will give XMLHeaderAndDocumentType return in the form of Mobile page
                // therefore we need to add custom request property
                // here i use Ubuntu 9.10 Karmic Koala Firefox User Agent
//                conn.setRequestProperty("User-Agent", "Mozilla");
                // reconnect it to apply new HTTP header request
                conn.disconnect();
                conn.connect();
                Object content = conn.getContent();
                File newFile = File.createTempFile("TreeHTMLParse", ".tmp.raw.html");
                newFile.deleteOnExit();
                if (content instanceof InputStream) {
                    // write content to File for easyness
                    writeWebPageHTMLcode((InputStream) content, newFile);
                }
                process(newFile.getAbsolutePath(), config, URL);

            } // if it's XMLHeaderAndDocumentType File
            else {
                process(URL, config, URL);
            }
        } catch (Exception ex) {
            Logger.getLogger(Html2XhtmlConverter.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError parsing URL\n" + URL, "Error parsing URL", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void process(String tempRawPath, XPathTreeConfigNode config, String URL) {
        String cleanedSource = runHtml2Xhtml(tempRawPath);

        cleanedSource = ConfigAlteredSourceApl.alterSource(cleanedSource, config);

        Reader r = null;
        try {
            // this are for xpath to works (the source of the web page)
            this.sourceString = cleanedSource;

            // getting the header and document type, for GUI, see below.
            XMLHeaderAndDocumentTypeEvaluator XMLHeaderAndDocumentType = new XMLHeaderAndDocumentTypeEvaluator(cleanedSource);
            XMLHeaderAndDocumentType.generateLinesOfIt();

            // this are for the GUI. Document type and header have to be removed to work properly on parsing
            // and presenting it in a form of swing tree, the header and document type is added later as a
            // XML and DOCTYPE header (if there is any) making the program behave awkwardly, so we have to remove it
            // for example, most of the web page I've parsed and cleaned contains :
            // <?xml version="1.0" standalone="yes"?>
            // this make sense, since html2xhtml produced it.
            // these lines of code removes it.
            ArrayList<String> linesToDelete = XMLHeaderAndDocumentType.getLinesOfIt();
            for (int i = 0; i < linesToDelete.size(); i++) {
                cleanedSource = cleanedSource.replace(linesToDelete.get(i).trim(), "");
            }

            //read the result
            r = new StringReader(cleanedSource);

            HTMLEditorKit.Parser parser;
            parser = new ParserDelegator();
            HTMLParseLister lister = new HTMLParseLister(URL); //creating tree
            if (!XMLHeaderAndDocumentType.getXMLHeader().isEmpty()) {
                lister.setXMLHeader(XMLHeaderAndDocumentType.getXMLHeader()); // adding XML Header
            }
            if (!XMLHeaderAndDocumentType.getDocumentType().isEmpty()) {
                lister.setDocumentType(XMLHeaderAndDocumentType.getDocumentType()); // addign Document Type
            }
            parser.parse(r, lister, true);

            this.rootNode = lister.getRootNode();
            generateChildsXPathNames(lister.getRootNode());

            r.close();
        } catch (Exception ex){
            Logger.getLogger(Html2XhtmlConverter.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError creating Document Tree\n" + URL, "Error creating Document Tree", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void process(InputStream is, XPathTreeConfigNode config, String URL) {
        try {
            File newFile = File.createTempFile("TreeHTMLParse", ".tmp.raw.html");
            writeWebPageHTMLcode(is, newFile);
            process(newFile.getAbsolutePath(), config, URL);
        } catch (IOException ex) {
            Logger.getLogger(Html2XhtmlConverter.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError creating temporary raw Document \n" + URL, "Parser : IO Error", JOptionPane.ERROR_MESSAGE);
            Exceptions.printStackTrace(ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="XPathName Generator">
    /**
     * Generates and add xPathName to {@code xPathNames} with : <br/>
     * <ul>
     *  <li>{@code tagName}</li>
     *  <li>{@code tagName} with [index] </li>
     * </ul>
     */
    public void generateChildsXPathNames(HTMLTreeNode node) {
        if (node.isLeaf()) {
            executeGenerateXPathNames(node);
        } else {
            executeGenerateXPathNames(node);

            // recursively explore all the node
            Enumeration en = node.children();
            while (en.hasMoreElements()) {
                generateChildsXPathNames((HTMLTreeNode) en.nextElement());
            }
        }
    }

    /**
     * Generates and add xPathName to {@code xPathNames} with : <br/>
     * <ul>
     *  <li>{@code tagName}</li>
     *  <li>{@code tagName} with [index] </li>
     * </ul>
     */
    public void generateXPathNames(HTMLTreeNode node, String tagName) {
        if (node.getParent() != null) {
            HTMLTreeNode checkNodeparent = (HTMLTreeNode) node.getParent();
            int counter;
            // check what tag names parent have
            if (checkNodeparent != null) {
                // if this tag name is already on parent
                if (checkNodeparent.isChildTagNameCounterMapContains(tagName)) {
                    // get the counter value
                    counter = checkNodeparent.getChildTagNameCounter(tagName);
                    // update the counter on parent node with a new value
                    checkNodeparent.updateChildTagNameCounter(tagName, counter + 1);
                    // add the entry on this object xPathNames
                    node.addXPathName(tagName + "[" + counter + "]");
                } // if this tag name is not on parent yet
                else {
                    checkNodeparent.getChildTagNameCounter().put(tagName, 1);
                    node.addXPathName(tagName + "[1]");
                }
            }
            node.addXPathName(tagName);
        }
    }

    private void executeGenerateXPathNames(HTMLTreeNode node) {
        String namaObjectPilihan = node.getUserObject().getClass().getName();
        if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLTag")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLTag HTMLTag = (org.emje.treehtmlparse.htmltree.nodes.HTMLTag) node.getUserObject();
            generateXPathNames(node, HTMLTag.getTagName());
            if (!HTMLTag.getPreferedAttribute().isEmpty()) {
                node.addXPathName(HTMLTag.getTagName() + "[@" + HTMLTag.getPreferedAttribute() + "]");
            }
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag HTMLSimpleTag = (org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag) node.getUserObject();
            generateXPathNames(node, HTMLSimpleTag.getTagName());
            if (!HTMLSimpleTag.getPreferedAttribute().isEmpty()) {
                node.addXPathName(HTMLSimpleTag.getTagName() + "[@" + HTMLSimpleTag.getPreferedAttribute() + "]");
            }
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLText")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLText HTMLText = (org.emje.treehtmlparse.htmltree.nodes.HTMLText) node.getUserObject();
            node.addXPathName("text()");
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute HTMLTagAttribute = (org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute) node.getUserObject();
            node.addXPathName("@" + HTMLTagAttribute.getAttributeName());
        }
    }// </editor-fold>

    private String runHtml2Xhtml(String tempRawPath) {
        org.emje.treehtmlparse.TreeHTMLParseView.appLogger.log(Level.INFO, "starting html2xhtml");
        String cleanedSource = null;
        try {
            // there are a different type of html2xhtml on linux and window, there is no one porting it to java yet.
            // currently i only certain it only works on ubuntu linux.
            String osType = System.getProperty("os.name");
            Process p = null;
            File currentWorkingDir = new File(Html2XhtmlConverter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            if (osType.toLowerCase().contains("linux")) {
                p = Runtime.getRuntime().exec(new File(currentWorkingDir.getParent()+"/"+rm.getString("html2xhtml.linux")).getAbsolutePath() + " " + tempRawPath);
            } else if (osType.toLowerCase().contains("windows")) {
                p = Runtime.getRuntime().exec(new File(currentWorkingDir.getParent()+"/"+rm.getString("html2xhtml.windows")).getAbsolutePath() + " " + tempRawPath);
            }

            // build string out of the process standard output
            BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = is.readLine()) != null) {
                sb.append(line).append("\n");
            }
            sb.delete(sb.length() - 1, sb.length()); // delete the last newline
            cleanedSource = sb.toString();

            try {
                // wait for the html2xhtml program to finish before proceeding
                // soo we don't have blank result file.
                p.waitFor();
            } catch (InterruptedException ex) {
                Logger.getLogger(Html2XhtmlConverter.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex + "\nError Running html2xhtml", "Error Running html2xhtml", JOptionPane.ERROR_MESSAGE);
            }
            org.emje.treehtmlparse.TreeHTMLParseView.appLogger.log(Level.INFO, "html2xhtml finished");
            p.destroy();
        } catch (IOException ex) {
            Logger.getLogger(Html2XhtmlConverter.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError Running html2xhtml", "Error Running html2xhtml", JOptionPane.ERROR_MESSAGE);
        }

        return cleanedSource;
    }

    /**
     * Write inputStream to File
     * @param content the input stream
     * @param writeTo the file to write inputstream content to
     */
    public void writeWebPageHTMLcode(InputStream content, File writeTo) {
        Reader r = null;
        BufferedReader br = null;
        try {
            r = new InputStreamReader(content);
            br = new BufferedReader(r);
            BufferedWriter bw = new BufferedWriter(new FileWriter(writeTo));
            String line = null;
            while ((line = br.readLine()) != null) {
                bw.write(line + "\n");
            }
            bw.close();
            br.close();
            r.close();
        } catch (IOException ex) {
            Logger.getLogger(Html2XhtmlConverter.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError Writing Source to :" + writeTo, "Error Running html2xhtml", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getSourceString() {
        return this.sourceString;
    }

    /**
     * @return the rootNode
     */
    public HTMLTreeNode getRootNode() {
        return rootNode;
    }
}
