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

package org.emje.treehtmlparse.alteredsourcestring;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import org.emje.treehtmlparse.TreeHTMLParseApp;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode;

public class AlteredSourceStringView extends javax.swing.JDialog {

    /** Creates new form AlteredSourceStringView */
    public AlteredSourceStringView(java.awt.Frame parent, boolean modal, XPathTreeConfigNode config) {
        super(parent, modal);
        configBackup = config.clone();
        configEdit = config;
        removedDataListModel = configEdit.getRemovedDataListModel();
        replacedDataTableModel = configEdit.getReplacedDataTableModel();
        initComponents();
    }

    public static XPathTreeConfigNode showDialog(java.awt.Frame parent, boolean modal, XPathTreeConfigNode config) {
        alteredSourceStringView = new AlteredSourceStringView(parent, modal, config);
        alteredSourceStringView.setLocationRelativeTo(parent);
        alteredSourceStringView.setVisible(true);
        return configEdit;
    }

    /** This method is called from within the constructor to
        * initialize the form.
        * WARNING: Do NOT modify this code. The content of this method is
        * always regenerated by the Form Editor.
        */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        removeRemoveButton = new javax.swing.JButton();
        removeAddButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        removeListScrollPane = new javax.swing.JScrollPane();
        removeList = new javax.swing.JList();
        footerSeparator = new javax.swing.JSeparator();
        removeLabel = new javax.swing.JLabel();
        replaceLabel = new javax.swing.JLabel();
        replaceRemoveButton = new javax.swing.JButton();
        replaceRemoveStringSeparator = new javax.swing.JSeparator();
        replaceAddButton = new javax.swing.JButton();
        replaceTableScrollPane = new javax.swing.JScrollPane();
        replaceTable = new javax.swing.JTable();
        replaceEditButton = new javax.swing.JButton();
        removeEditButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(AlteredSourceStringView.class, "AlteredSourceStringView.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(554, 461));
        setName("Form"); // NOI18N
        setResizable(false);

        removeRemoveButton.setText(org.openide.util.NbBundle.getMessage(AlteredSourceStringView.class, "AlteredSourceStringView.removeRemoveButton.text")); // NOI18N
        removeRemoveButton.setName("removeRemoveButton"); // NOI18N
        removeRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRemoveButtonActionPerformed(evt);
            }
        });

        removeAddButton.setText(org.openide.util.NbBundle.getMessage(AlteredSourceStringView.class, "AlteredSourceStringView.removeAddButton.text")); // NOI18N
        removeAddButton.setName("removeAddButton"); // NOI18N
        removeAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAddButtonActionPerformed(evt);
            }
        });

        okButton.setText(org.openide.util.NbBundle.getMessage(AlteredSourceStringView.class, "AlteredSourceStringView.okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(org.openide.util.NbBundle.getMessage(AlteredSourceStringView.class, "AlteredSourceStringView.cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        removeListScrollPane.setName("removeListScrollPane"); // NOI18N

        removeList.setModel(this.removedDataListModel);
        removeList.setName("removeList"); // NOI18N
        removeListScrollPane.setViewportView(removeList);

        footerSeparator.setName("footerSeparator"); // NOI18N

        removeLabel.setText(org.openide.util.NbBundle.getMessage(AlteredSourceStringView.class, "AlteredSourceStringView.removeLabel.text")); // NOI18N
        removeLabel.setName("removeLabel"); // NOI18N

        replaceLabel.setText(org.openide.util.NbBundle.getMessage(AlteredSourceStringView.class, "AlteredSourceStringView.replaceLabel.text")); // NOI18N
        replaceLabel.setName("replaceLabel"); // NOI18N

        replaceRemoveButton.setText(org.openide.util.NbBundle.getMessage(AlteredSourceStringView.class, "AlteredSourceStringView.replaceRemoveButton.text")); // NOI18N
        replaceRemoveButton.setName("replaceRemoveButton"); // NOI18N
        replaceRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replaceRemoveButtonActionPerformed(evt);
            }
        });

        replaceRemoveStringSeparator.setName("replaceRemoveStringSeparator"); // NOI18N

        replaceAddButton.setText(org.openide.util.NbBundle.getMessage(AlteredSourceStringView.class, "AlteredSourceStringView.replaceAddButton.text")); // NOI18N
        replaceAddButton.setName("replaceAddButton"); // NOI18N
        replaceAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replaceAddButtonActionPerformed(evt);
            }
        });

        replaceTableScrollPane.setName("replaceTableScrollPane"); // NOI18N

        replaceTable.setModel(this.replacedDataTableModel);
        replaceTable.setName("replaceTable"); // NOI18N
        replaceTableScrollPane.setViewportView(replaceTable);

        replaceEditButton.setText(org.openide.util.NbBundle.getMessage(AlteredSourceStringView.class, "AlteredSourceStringView.replaceEditButton.text")); // NOI18N
        replaceEditButton.setName("replaceEditButton"); // NOI18N
        replaceEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replaceEditButtonActionPerformed(evt);
            }
        });

        removeEditButton.setText(org.openide.util.NbBundle.getMessage(AlteredSourceStringView.class, "AlteredSourceStringView.removeEditButton.text")); // NOI18N
        removeEditButton.setName("removeEditButton"); // NOI18N
        removeEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeEditButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(replaceTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                            .addComponent(removeListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(replaceAddButton)
                            .addComponent(replaceEditButton)
                            .addComponent(replaceRemoveButton)
                            .addComponent(removeAddButton)
                            .addComponent(removeRemoveButton)
                            .addComponent(removeEditButton)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(replaceLabel)
                    .addComponent(replaceRemoveStringSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                    .addComponent(removeLabel)
                    .addComponent(footerSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton, removeAddButton, removeEditButton, removeRemoveButton, replaceAddButton, replaceEditButton, replaceRemoveButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(replaceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(replaceAddButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(replaceEditButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(replaceRemoveButton))
                    .addComponent(replaceTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(replaceRemoveStringSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(removeAddButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeEditButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeRemoveButton))
                    .addComponent(removeListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(footerSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // present the previous config object for the return value
        configEdit = configBackup;
        alteredSourceStringView.setVisible(false);
        alteredSourceStringView.dispose();
}//GEN-LAST:event_cancelButtonActionPerformed

    private void replaceRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceRemoveButtonActionPerformed
        if (this.replaceTable.getSelectedRow() != -1){
            DefaultTableModel model = (DefaultTableModel) this.replaceTable.getModel();
            model.removeRow(this.replaceTable.getSelectedRow());
        }
    }//GEN-LAST:event_replaceRemoveButtonActionPerformed

    private void replaceAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceAddButtonActionPerformed
        JFrame mainFrame = TreeHTMLParseApp.getApplication().getMainFrame();
        String[] replacedText = AddReplacedStringView.showDialog(mainFrame, true, "Add replaced String");
        if (replacedText != null) {
            DefaultTableModel model = (DefaultTableModel) this.replaceTable.getModel();
            model.addRow(new Object [] {replacedText[0], replacedText[1]});
        }
    }//GEN-LAST:event_replaceAddButtonActionPerformed

    private void replaceEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceEditButtonActionPerformed
        if (this.replaceTable.getSelectedRow() != -1){
            JFrame mainFrame = TreeHTMLParseApp.getApplication().getMainFrame();

            DefaultTableModel model = (DefaultTableModel) this.replaceTable.getModel();
            String containingText = model.getValueAt(this.replaceTable.getSelectedRow(), 0).toString();
            String replaceWith = model.getValueAt(this.replaceTable.getSelectedRow(), 1).toString();

            String[] replacedText = AddReplacedStringView.showDialog(mainFrame, true, "Edit replaced String", containingText, replaceWith);
            if (replacedText != null) {
                model.setValueAt(replacedText[0], this.replaceTable.getSelectedRow(), 0);
                model.setValueAt(replacedText[1], this.replaceTable.getSelectedRow(), 1);
            }
        }
    }//GEN-LAST:event_replaceEditButtonActionPerformed

    private void removeAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAddButtonActionPerformed
        JFrame mainFrame = TreeHTMLParseApp.getApplication().getMainFrame();
        String text = AddRemovedStringView.showDialog(mainFrame, true, "Add removed String", null);
        if(text != null){
            DefaultListModel model = (DefaultListModel) removeList.getModel();
            model.addElement(text);
        }
    }//GEN-LAST:event_removeAddButtonActionPerformed

    private void removeEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeEditButtonActionPerformed
        if (this.removeList.getSelectedIndex() != -1){
            JFrame mainFrame = TreeHTMLParseApp.getApplication().getMainFrame();

            DefaultListModel model = (DefaultListModel) removeList.getModel();
            String text = model.get(this.removeList.getSelectedIndex()).toString();

            text = AddRemovedStringView.showDialog(mainFrame, true, "Edit removed String", text);
            if (text != null) {
                model.setElementAt(text, this.removeList.getSelectedIndex());
            }
        }
    }//GEN-LAST:event_removeEditButtonActionPerformed

    private void removeRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRemoveButtonActionPerformed
        if (this.removeList.getSelectedIndex() != -1){
            DefaultListModel model = (DefaultListModel) removeList.getModel();
            model.remove(this.removeList.getSelectedIndex());
        }
    }//GEN-LAST:event_removeRemoveButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        configEdit.setRemovedDataListModel(removedDataListModel);
        configEdit.setReplacedDataTableModel(replacedDataTableModel);
        alteredSourceStringView.setVisible(false);
        alteredSourceStringView.dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JSeparator footerSeparator;
    private javax.swing.JButton okButton;
    private javax.swing.JButton removeAddButton;
    private javax.swing.JButton removeEditButton;
    private javax.swing.JLabel removeLabel;
    private javax.swing.JList removeList;
    private javax.swing.JScrollPane removeListScrollPane;
    private javax.swing.JButton removeRemoveButton;
    private javax.swing.JButton replaceAddButton;
    private javax.swing.JButton replaceEditButton;
    private javax.swing.JLabel replaceLabel;
    private javax.swing.JButton replaceRemoveButton;
    private javax.swing.JSeparator replaceRemoveStringSeparator;
    private javax.swing.JTable replaceTable;
    private javax.swing.JScrollPane replaceTableScrollPane;
    // End of variables declaration//GEN-END:variables

    private static AlteredSourceStringView alteredSourceStringView;
    
    private static XPathTreeConfigNode configEdit;
    private XPathTreeConfigNode configBackup;
    
    private DefaultListModel removedDataListModel;
    private DefaultTableModel replacedDataTableModel;
}
