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

package org.emje.treehtmlparse.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JOptionPane;

public final class XTCPFileParser {

    private File configFile;
    private File sourceFile;

    public XTCPFileParser(File fileName) {
        try {
            if (configFile != null) configFile.delete();
            if (sourceFile != null) sourceFile.delete();
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(new FileInputStream(fileName));

            zipentry = zipinputstream.getNextEntry();
            while (zipentry != null) {
                //for each entry to be extracted
                String entryName = zipentry.getName();

                int n;
                FileOutputStream fileoutputstream;
                File newFile = null;

                if (entryName.equals("source.xhtml")) {
                    newFile = File.createTempFile("TreeHTMLParse", ".tmp.raw.html");
                    newFile.deleteOnExit();
                } else if (entryName.equals("configuration.xml")) {
                    newFile = File.createTempFile("configuration", ".xml");
                    newFile.deleteOnExit();
                }
                newFile.deleteOnExit();

                if (newFile.isDirectory()) {
                    break;
                }

                fileoutputstream = new FileOutputStream(newFile);

                while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                    fileoutputstream.write(buf, 0, n);
                }

                fileoutputstream.close();
                zipinputstream.closeEntry();
                if (entryName.equals("source.xhtml")) {
                    this.sourceFile = newFile;
                } else if (entryName.equals("configuration.xml")) {
                    this.configFile = newFile;
                }

                zipentry = zipinputstream.getNextEntry();

            }//while

            zipinputstream.close();
        } catch (Exception ex) {
            Logger.getLogger(XTCPFileParser.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError Parsing xtcp ZIP File", "Error Parsing xtcp ZIP File", JOptionPane.ERROR_MESSAGE);
        }
    }

    public File getConfigFile() {
        return configFile;
    }

    public File getSourceFile() {
        return sourceFile;
    }
}
