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

import java.io.File;
import java.util.Enumeration;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode;
import org.jdesktop.application.ResourceMap;

public class ConfigAlteredSourceApl {

    private static ResourceMap rm = org.jdesktop.application.Application.
                    getInstance(org.emje.treehtmlparse.TreeHTMLParseApp.class).
                    getContext().
                    getResourceMap(org.emje.treehtmlparse.TreeHTMLParseView.class);

    /**
     * Remove and replace strings defined in {@codew XPathTreeConfigNode config} from web page source code.
     * @param source : web page source code
     * @param config : XPathTreeConfigNode config
     * @return altered web page source code by remove and replace strings from the specified config.
     */
    public static String alterSource(String source, XPathTreeConfigNode config){

        String alteredSource = source;
        // replace words that included on AlteredSourceStrings configuration
        // replaced strings section
        for (int i = 0; i < config.getReplacedDataTableModel().getRowCount(); i++) {
            CharSequence csText = (String) config.getReplacedDataTableModel().getValueAt(i, 0);
            CharSequence csWith = (String) config.getReplacedDataTableModel().getValueAt(i, 1);
            alteredSource = alteredSource.replace(csText, csWith);
        }
        // removed strings section
        Enumeration listEn = config.getRemovedDataListModel().elements();
        while (listEn.hasMoreElements()) {
            String s = (String) listEn.nextElement();
            alteredSource = alteredSource.replaceAll(s, "");
        }

        // changing the document type to local, so it could be parsed fast
        File currentWorkingDir = new File(ConfigAlteredSourceApl.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String docTypeURL = currentWorkingDir.getParent()+"/"+rm.getString("doctype.transitional.url");
        String docTypeLocal = new File(currentWorkingDir.getParent()+"/"+rm.getString("doctype.transitional.local")).getAbsolutePath();
        alteredSource = alteredSource.replace( docTypeURL, docTypeLocal);

        return alteredSource;
    }
}
