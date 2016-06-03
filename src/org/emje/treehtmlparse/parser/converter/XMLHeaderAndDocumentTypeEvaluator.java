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

package org.emje.treehtmlparse.parser.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This Class is used as XML Header And DocumentType evaluator for SourceString. It,s extract XML Header and Document Type, resulting :<br>
 * <ul>
 *  <li>The lines of XML Header and Document Type in {@code ArrayList} object by executing {@code generateLinesOfIt()} method</li>
 *  <li>XML Header String as a single line of String</li>
 *  <li>Document Type String as a single line of String</li>
 * </ul>
 */
public class XMLHeaderAndDocumentTypeEvaluator {

    private String sourceString;
    private ArrayList<String> linesOfIt;
    private String documentType = "";
    private String XMLHeader = "";

    /**
      * This Class is used as XML Header And DocumentType evaluator for SourceString. It,s extract XML Header and Document Type, resulting :<br>
    * <ul>
    *  <li>The lines of XML Header and Document Type in {@code ArrayList} object by executing {@code generateLinesOfIt()} method</li>
    *  <li>XML Header String as a single line of String</li>
    *  <li>Document Type String as a single line of String</li>
    * </ul>
     * @param sourceString
     */
    public XMLHeaderAndDocumentTypeEvaluator(String sourceString) {
        this.sourceString = sourceString;
    }

    /**
     * Generating:<br>
     * <ul>
     *  <li>The lines of XML Header and Document Type in {@code ArrayList} object,<br>
     *  could be acquired by {@code getLinesOfIt()} method</li>
     *  <li>XML Header String as a single line of String,<br>
     *  could be acquired by {@code getXMLHeader()} method</li>
     *  <li>Document Type String as a single line of String,<br>
     *  could be acquired by {@code getDocumentType()} method</li>
     * </ul>
     */
    public void generateLinesOfIt() {
        try {
            BufferedReader br = new BufferedReader(new StringReader(sourceString));
            String line = null;
            linesOfIt = new ArrayList<String>();
            boolean isXMLHeaderSet = false;
            boolean isDocumentTypeSet = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("<?xml") && line.contains(">")) {
                    XMLHeader = line.trim();
                    linesOfIt.add(line);
                    isXMLHeaderSet = true;
                } else if (line.startsWith("<?xml") && !line.contains(">")) {
                    XMLHeader = line.trim();
                    linesOfIt.add(line);
                    while ((line = br.readLine()) != null) {
                        if (line.contains(">")) {
                            linesOfIt.add(line);
                            line = line.split(">")[0].trim();
                            XMLHeader = XMLHeader + " " + line + ">";
                            isXMLHeaderSet = true;
                            break;
                        } else {
                            XMLHeader = XMLHeader + " " + line.trim();
                            linesOfIt.add(line);
                        }
                    }
                }

                if (line.startsWith("<!DOCTYPE") && line.contains(">")) {
                    documentType = line.trim();
                    linesOfIt.add(line);
                    isDocumentTypeSet = true;
                } else if (line.startsWith("<!DOCTYPE") && !line.contains(">")) {
                    documentType = line.trim();
                    linesOfIt.add(line);
                    while ((line = br.readLine()) != null) {
                        if (line.contains(">")) {
                            linesOfIt.add(line);
                            line = line.split(">")[0].trim();
                            documentType = documentType + " " + line + ">";
                            isDocumentTypeSet = true;
                            break;
                        } else {
                            documentType = documentType + " " + line.trim();
                            linesOfIt.add(line);
                        }
                    }
                }
                if (isXMLHeaderSet && isDocumentTypeSet) {
                    break;
                }
            }
        } 
        catch (IOException ex) {
            Logger.getLogger(XMLHeaderAndDocumentTypeEvaluator.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError Reading Cleaned Source Header", "Error Reading Cleaned Source Header", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * @return XML Header String as a single line of String
     */
    public String getXMLHeader() {
        return XMLHeader;
    }

    /**
     *
     * @return Document Type String as a single line of String
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     *
     * @return The lines of XML Header and Document Type in {@code ArrayList} object by executing {@code generateLinesOfIt()} method
     */
    public ArrayList<String> getLinesOfIt() {
        return linesOfIt;
    }
}
