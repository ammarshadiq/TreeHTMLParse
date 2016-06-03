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

package org.emje.treehtmlparse;

import java.net.MalformedURLException;
import java.util.logging.Level;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeFieldNode;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.emje.treehtmlparse.alteredsourcestring.AlteredSourceStringView;
import org.emje.treehtmlparse.xpathtree.panel.XPathFieldItem;
import org.emje.treehtmlparse.xpathtree.panel.XPathConfigItem;
import org.emje.treehtmlparse.xpathtree.panel.XPathEntryItem;
import org.emje.treehtmlparse.parser.HtmlParse;
import org.emje.treehtmlparse.htmltree.HTMLTreeNode;
import org.emje.treehtmlparse.parser.ConfigFileParser;
import org.emje.treehtmlparse.parser.ConfigurationResult;
import org.emje.treehtmlparse.parser.XTCPFileParser;
import org.emje.treehtmlparse.preferences.Preferences;
import org.emje.treehtmlparse.urlfilter.URLFilterView;
import org.emje.treehtmlparse.xpathtree.XPathTreeFieldInitialNodes;
import org.emje.treehtmlparse.xpathtree.XPathTreeConfigInitialNodes;
import org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode;

/**
 * The application's main frame.
 */
public class TreeHTMLParseView extends FrameView {

    public TreeHTMLParseView(SingleFrameApplication app, String[] args) {
        super(app);

        initComponents();

        ResourceMap resourceMap = getResourceMap();
        
        this.getFrame().setIconImage(
              resourceMap.getImageIcon("ApplicationIcon").getImage());
        
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        
        getFrame().setTitle("Tree HTML Parse");
        if (args != null){
            for (int i = 0; i < args.length; i++) {
                if ("-open".equals(args[i])) {
                    targetFile = new File(args[i] + 1);
                    openAction();
                    i++;
                }
                // ignore anything else for now
            }
        }
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = TreeHTMLParseApp.getApplication().getMainFrame();
            aboutBox = new TreeHTMLParseAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        TreeHTMLParseApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    chooser = new JFileChooser();
    mainPanel = new javax.swing.JPanel();
    mainSplitPane = new javax.swing.JSplitPane();
    leftPanel = new javax.swing.JPanel();
    leftSplitPane = new javax.swing.JSplitPane();
    xPathTreePanel = new javax.swing.JPanel();
    xPathTreeScrollPane = new javax.swing.JScrollPane();
    xPathTree = new javax.swing.JTree(xPathTreeModel);
    xPathTreeMoveUpButton = new javax.swing.JButton();
    xPathTreeMoveDownButton = new javax.swing.JButton();
    xPathTreeRemoveButton = new javax.swing.JButton();
    xPathTreeAddButton = new javax.swing.JButton();
    xPathTreeClearButton = new javax.swing.JButton();
    HTMLTreePanel = new javax.swing.JPanel();
    searchButton = new javax.swing.JButton();
    treeScrollPane = new javax.swing.JScrollPane();
    HTMLTree = new javax.swing.JTree(parse.getRootNode());
    searchSourceTextField = new javax.swing.JTextField();
    rightPanel = new javax.swing.JPanel();
    rightSplitPane = new javax.swing.JSplitPane();
    browseViewPanel = new javax.swing.JPanel();
    locationComboBox = new javax.swing.JComboBox();
    locationLabel = new javax.swing.JLabel();
    ComponentViews = new javax.swing.JTabbedPane();
    browseViewScrollPane = new javax.swing.JScrollPane();
    browseViewEditorPane = new javax.swing.JEditorPane();
    sourceViewScrollPane = new javax.swing.JScrollPane();
    sourceViewTextArea = new javax.swing.JTextArea();
    configurationResultPanel = new javax.swing.JPanel();
    configurationResultRefreshButton = new javax.swing.JButton();
    configurationResultTreeScrollPane = new javax.swing.JScrollPane();
    configurationResultTree = new javax.swing.JTree();
    copyLocationValueButton = new javax.swing.JButton();
    openButton = new javax.swing.JButton();
    saveButton = new javax.swing.JButton();
    newButton = new javax.swing.JButton();
    goToAddressButton = new javax.swing.JButton();
    locationBarTextField = new javax.swing.JTextField();
    appHeaderSeparator = new javax.swing.JSeparator();
    menuBar = new javax.swing.JMenuBar();
    javax.swing.JMenu fileMenu = new javax.swing.JMenu();
    newDocumentMenuItem = new javax.swing.JMenuItem();
    openDocumentMenuItem = new javax.swing.JMenuItem();
    saveMenuItem = new javax.swing.JMenuItem();
    saveAsMenuItem = new javax.swing.JMenuItem();
    separator1 = new javax.swing.JPopupMenu.Separator();
    ParseFileMenuItem = new javax.swing.JMenuItem();
    ParseURLMenuItem = new javax.swing.JMenuItem();
    separator2 = new javax.swing.JPopupMenu.Separator();
    javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
    editMenu = new javax.swing.JMenu();
    preferencesMenuItem = new javax.swing.JMenuItem();
    alterSourceMenuItem = new javax.swing.JMenuItem();
    urlFilterMEnuItem = new javax.swing.JMenuItem();
    viewMenu = new javax.swing.JMenu();
    refreshMenuItem = new javax.swing.JMenuItem();
    javax.swing.JMenu helpMenu = new javax.swing.JMenu();
    javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
    statusPanel = new javax.swing.JPanel();
    javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
    statusMessageLabel = new javax.swing.JLabel();
    statusAnimationLabel = new javax.swing.JLabel();
    progressBar = new javax.swing.JProgressBar();

    chooser.setCurrentDirectory(new File("."));
    chooser.addChoosableFileFilter(configFileFilter);
    chooser.addChoosableFileFilter(markupFileFilter);
    chooser.setAcceptAllFileFilterUsed(true);
    mainPanel.setName("mainPanel"); // NOI18N

    mainSplitPane.setBorder(null);
    mainSplitPane.setDividerLocation(200);
    mainSplitPane.setDividerSize(5);
    mainSplitPane.setName("mainSplitPane"); // NOI18N

    leftPanel.setName("leftPanel"); // NOI18N

    leftSplitPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    leftSplitPane.setDividerLocation(300);
    leftSplitPane.setDividerSize(5);
    leftSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    leftSplitPane.setName("leftSplitPane"); // NOI18N

    xPathTreePanel.setBorder(null);
    xPathTreePanel.setName("xPathTreePanel"); // NOI18N

    xPathTreeScrollPane.setName("xPathTreeScrollPane"); // NOI18N

    xPathTree.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    xPathTree.setCellRenderer(new org.emje.treehtmlparse.xpathtree.XPathTreeCellRenderer());
    xPathTree.setName("xPathTree"); // NOI18N
    xPathTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        xPathTreeValueChanged(evt);
      }
    });
    xPathTreeScrollPane.setViewportView(xPathTree);
    xPathTree.setSelectionRow(0);
    entryItem.setXPathTree(xPathTree);
    fieldItem.setXPathTree(xPathTree);
    configItem.setXPathTree(xPathTree);

    xPathTreeMoveUpButton.setBackground(xPathTreePanel.getBackground());
    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(TreeHTMLParseView.class);
    xPathTreeMoveUpButton.setFont(resourceMap.getFont("xPathTreeMoveUpButton.font")); // NOI18N
    xPathTreeMoveUpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/emje/treehtmlparse/resources/xpathcomponentitems/go-up.png"))); // NOI18N
    xPathTreeMoveUpButton.setText(resourceMap.getString("xPathTreeMoveUpButton.text")); // NOI18N
    xPathTreeMoveUpButton.setToolTipText(resourceMap.getString("xPathTreeMoveUpButton.toolTipText")); // NOI18N
    xPathTreeMoveUpButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    xPathTreeMoveUpButton.setIconTextGap(0);
    xPathTreeMoveUpButton.setName("xPathTreeMoveUpButton"); // NOI18N
    xPathTreeMoveUpButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        xPathTreeMoveUpButtonActionPerformed(evt);
      }
    });

    xPathTreeMoveDownButton.setFont(resourceMap.getFont("xPathTreeMoveDownButton.font")); // NOI18N
    xPathTreeMoveDownButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/emje/treehtmlparse/resources/xpathcomponentitems/go-down.png"))); // NOI18N
    xPathTreeMoveDownButton.setText(resourceMap.getString("xPathTreeMoveDownButton.text")); // NOI18N
    xPathTreeMoveDownButton.setToolTipText(resourceMap.getString("xPathTreeMoveDownButton.toolTipText")); // NOI18N
    xPathTreeMoveDownButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    xPathTreeMoveDownButton.setName("xPathTreeMoveDownButton"); // NOI18N
    xPathTreeMoveDownButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        xPathTreeMoveDownButtonActionPerformed(evt);
      }
    });

    xPathTreeRemoveButton.setFont(resourceMap.getFont("xPathTreeRemoveButton.font")); // NOI18N
    xPathTreeRemoveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/emje/treehtmlparse/resources/xpathcomponentitems/list-remove.png"))); // NOI18N
    xPathTreeRemoveButton.setText(resourceMap.getString("xPathTreeRemoveButton.text")); // NOI18N
    xPathTreeRemoveButton.setToolTipText(resourceMap.getString("xPathTreeRemoveButton.toolTipText")); // NOI18N
    xPathTreeRemoveButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    xPathTreeRemoveButton.setName("xPathTreeRemoveButton"); // NOI18N
    xPathTreeRemoveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        xPathTreeRemoveButtonActionPerformed(evt);
      }
    });

    xPathTreeAddButton.setFont(resourceMap.getFont("xPathTreeAddButton.font")); // NOI18N
    xPathTreeAddButton.setIcon(resourceMap.getIcon("xPathTreeAddButton.icon")); // NOI18N
    xPathTreeAddButton.setToolTipText(resourceMap.getString("xPathTreeAddButton.toolTipText")); // NOI18N
    xPathTreeAddButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    xPathTreeAddButton.setName("xPathTreeAddButton"); // NOI18N
    xPathTreeAddButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        xPathTreeAddButtonActionPerformed(evt);
      }
    });

    xPathTreeClearButton.setFont(resourceMap.getFont("xPathTreeClearButton.font")); // NOI18N
    xPathTreeClearButton.setIcon(resourceMap.getIcon("xPathTreeClearButton.icon")); // NOI18N
    xPathTreeClearButton.setToolTipText(resourceMap.getString("xPathTreeClearButton.toolTipText")); // NOI18N
    xPathTreeClearButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    xPathTreeClearButton.setName("xPathTreeClearButton"); // NOI18N
    xPathTreeClearButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        xPathTreeClearButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout xPathTreePanelLayout = new javax.swing.GroupLayout(xPathTreePanel);
    xPathTreePanel.setLayout(xPathTreePanelLayout);
    xPathTreePanelLayout.setHorizontalGroup(
      xPathTreePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(xPathTreePanelLayout.createSequentialGroup()
        .addComponent(xPathTreeMoveUpButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(xPathTreeMoveDownButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(xPathTreeRemoveButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(xPathTreeAddButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
        .addComponent(xPathTreeClearButton))
      .addComponent(xPathTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
    );
    xPathTreePanelLayout.setVerticalGroup(
      xPathTreePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, xPathTreePanelLayout.createSequentialGroup()
        .addComponent(xPathTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(xPathTreePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(xPathTreeMoveUpButton)
          .addComponent(xPathTreeMoveDownButton)
          .addComponent(xPathTreeRemoveButton)
          .addComponent(xPathTreeAddButton)
          .addComponent(xPathTreeClearButton)))
    );

    leftSplitPane.setRightComponent(xPathTreePanel);

    HTMLTreePanel.setBorder(null);
    HTMLTreePanel.setName("HTMLTreePanel"); // NOI18N

    searchButton.setIcon(resourceMap.getIcon("searchButton.icon")); // NOI18N
    searchButton.setText(resourceMap.getString("searchButton.text")); // NOI18N
    searchButton.setToolTipText(resourceMap.getString("searchButton.toolTipText")); // NOI18N
    searchButton.setEnabled(false);
    searchButton.setName("searchButton"); // NOI18N

    treeScrollPane.setName("treeScrollPane"); // NOI18N

    HTMLTree.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    HTMLTree.setCellRenderer(new org.emje.treehtmlparse.htmltree.HTMLTreeCellRenderer());
    HTMLTree.setName("HTMLTree"); // NOI18N
    HTMLTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        HTMLTreeValueChanged(evt);
      }
    });
    treeScrollPane.setViewportView(HTMLTree);

    searchSourceTextField.setText(resourceMap.getString("searchSourceTextField.text")); // NOI18N
    searchSourceTextField.setEnabled(false);
    searchSourceTextField.setName("searchSourceTextField"); // NOI18N

    javax.swing.GroupLayout HTMLTreePanelLayout = new javax.swing.GroupLayout(HTMLTreePanel);
    HTMLTreePanel.setLayout(HTMLTreePanelLayout);
    HTMLTreePanelLayout.setHorizontalGroup(
      HTMLTreePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HTMLTreePanelLayout.createSequentialGroup()
        .addComponent(searchSourceTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(searchButton))
      .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
    );
    HTMLTreePanelLayout.setVerticalGroup(
      HTMLTreePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(HTMLTreePanelLayout.createSequentialGroup()
        .addGroup(HTMLTreePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(searchButton)
          .addComponent(searchSourceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
    );

    HTMLTreePanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {searchButton, searchSourceTextField});

    leftSplitPane.setLeftComponent(HTMLTreePanel);

    javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
    leftPanel.setLayout(leftPanelLayout);
    leftPanelLayout.setHorizontalGroup(
      leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(leftSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
    );
    leftPanelLayout.setVerticalGroup(
      leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(leftSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
    );

    mainSplitPane.setLeftComponent(leftPanel);

    rightPanel.setBorder(null);
    rightPanel.setName("rightPanel"); // NOI18N

    rightSplitPane.setBorder(null);
    rightSplitPane.setDividerLocation(500);
    rightSplitPane.setDividerSize(5);
    rightSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    rightSplitPane.setName("rightSplitPane"); // NOI18N
    rightSplitPane.setDividerLocation(rightSplitPane.getHeight()-configItem.getPreferredSize().height);

    browseViewPanel.setBorder(null);
    browseViewPanel.setName("browseViewPanel"); // NOI18N

    locationComboBox.setEditable(true);
    locationComboBox.setFont(resourceMap.getFont("locationComboBox.font")); // NOI18N
    locationComboBox.setName("locationComboBox"); // NOI18N

    locationLabel.setFont(resourceMap.getFont("locationLabel.font")); // NOI18N
    locationLabel.setText(resourceMap.getString("locationLabel.text")); // NOI18N
    locationLabel.setName("locationLabel"); // NOI18N

    ComponentViews.setBorder(null);
    ComponentViews.setName("ComponentViews"); // NOI18N

    browseViewScrollPane.setName("browseViewScrollPane"); // NOI18N

    browseViewEditorPane.setBorder(null);
    browseViewEditorPane.setContentType(resourceMap.getString("browseViewEditorPane.contentType")); // NOI18N
    browseViewEditorPane.setEditable(false);
    browseViewEditorPane.setName("browseViewEditorPane"); // NOI18N
    browseViewScrollPane.setViewportView(browseViewEditorPane);
    //try {
      //    browseViewEditorPane.setPage("file:///home/shadiq/NetBeansProjects/TreeHTMLParse/detik.html");
      //} catch (IOException ioe) {
      //    browseViewEditorPane.setText("<font size=15 color=red>ERROR!<br></font>"+ioe.getMessage());
      //    Exceptions.printStackTrace(ioe);
      //}

    ComponentViews.addTab(resourceMap.getString("browseViewScrollPane.TabConstraints.tabTitle"), browseViewScrollPane); // NOI18N

    sourceViewScrollPane.setName("sourceViewScrollPane"); // NOI18N

    sourceViewTextArea.setColumns(20);
    sourceViewTextArea.setRows(5);
    sourceViewTextArea.setName("sourceViewTextArea"); // NOI18N
    sourceViewScrollPane.setViewportView(sourceViewTextArea);

    ComponentViews.addTab(resourceMap.getString("sourceViewScrollPane.TabConstraints.tabTitle"), sourceViewScrollPane); // NOI18N

    configurationResultPanel.setName("configurationResultPanel"); // NOI18N

    configurationResultRefreshButton.setText(resourceMap.getString("configurationResultRefreshButton.text")); // NOI18N
    configurationResultRefreshButton.setName("configurationResultRefreshButton"); // NOI18N
    configurationResultRefreshButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        configurationResultRefreshButtonActionPerformed(evt);
      }
    });

    configurationResultTreeScrollPane.setName("configurationResultTreeScrollPane"); // NOI18N

    javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
    configurationResultTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
    configurationResultTree.setName("configurationResultTree"); // NOI18N
    configurationResultTreeScrollPane.setViewportView(configurationResultTree);

    javax.swing.GroupLayout configurationResultPanelLayout = new javax.swing.GroupLayout(configurationResultPanel);
    configurationResultPanel.setLayout(configurationResultPanelLayout);
    configurationResultPanelLayout.setHorizontalGroup(
      configurationResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(configurationResultPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(configurationResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(configurationResultTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
          .addComponent(configurationResultRefreshButton))
        .addContainerGap())
    );
    configurationResultPanelLayout.setVerticalGroup(
      configurationResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(configurationResultPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(configurationResultRefreshButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(configurationResultTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
        .addContainerGap())
    );

    ComponentViews.addTab(resourceMap.getString("configurationResultPanel.TabConstraints.tabTitle"), configurationResultPanel); // NOI18N

    copyLocationValueButton.setIcon(resourceMap.getIcon("copyLocationValueButton.icon")); // NOI18N
    copyLocationValueButton.setText(resourceMap.getString("copyLocationValueButton.text")); // NOI18N
    copyLocationValueButton.setToolTipText(resourceMap.getString("copyLocationValueButton.toolTipText")); // NOI18N
    copyLocationValueButton.setEnabled(false);
    copyLocationValueButton.setName("copyLocationValueButton"); // NOI18N
    copyLocationValueButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        copyLocationValueButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout browseViewPanelLayout = new javax.swing.GroupLayout(browseViewPanel);
    browseViewPanel.setLayout(browseViewPanelLayout);
    browseViewPanelLayout.setHorizontalGroup(
      browseViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(browseViewPanelLayout.createSequentialGroup()
        .addComponent(locationLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(locationComboBox, 0, 338, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(copyLocationValueButton))
      .addComponent(ComponentViews, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
    );
    browseViewPanelLayout.setVerticalGroup(
      browseViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(browseViewPanelLayout.createSequentialGroup()
        .addGroup(browseViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(locationLabel)
          .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(copyLocationValueButton))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(ComponentViews, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
    );

    rightSplitPane.setTopComponent(browseViewPanel);

    javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
    rightPanel.setLayout(rightPanelLayout);
    rightPanelLayout.setHorizontalGroup(
      rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(rightSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
    );
    rightPanelLayout.setVerticalGroup(
      rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(rightSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
    );

    mainSplitPane.setRightComponent(rightPanel);

    openButton.setFont(resourceMap.getFont("openButton.font")); // NOI18N
    openButton.setIcon(resourceMap.getIcon("openButton.icon")); // NOI18N
    openButton.setText(resourceMap.getString("openButton.text")); // NOI18N
    openButton.setToolTipText(resourceMap.getString("openButton.toolTipText")); // NOI18N
    openButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    openButton.setIconTextGap(0);
    openButton.setName("openButton"); // NOI18N
    openButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    openButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openButtonActionPerformed(evt);
      }
    });

    saveButton.setFont(resourceMap.getFont("saveButton.font")); // NOI18N
    saveButton.setIcon(resourceMap.getIcon("saveButton.icon")); // NOI18N
    saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
    saveButton.setToolTipText(resourceMap.getString("saveButton.toolTipText")); // NOI18N
    saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    saveButton.setIconTextGap(0);
    saveButton.setName("saveButton"); // NOI18N
    saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    saveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveButtonActionPerformed(evt);
      }
    });

    newButton.setFont(resourceMap.getFont("newButton.font")); // NOI18N
    newButton.setIcon(resourceMap.getIcon("newButton.icon")); // NOI18N
    newButton.setText(resourceMap.getString("newButton.text")); // NOI18N
    newButton.setToolTipText(resourceMap.getString("newButton.toolTipText")); // NOI18N
    newButton.setEnabled(false);
    newButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    newButton.setIconTextGap(0);
    newButton.setName("newButton"); // NOI18N
    newButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    newButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        newButtonActionPerformed(evt);
      }
    });

    goToAddressButton.setIcon(resourceMap.getIcon("goToLocationButton.icon")); // NOI18N
    goToAddressButton.setText(resourceMap.getString("goToLocationButton.text")); // NOI18N
    goToAddressButton.setToolTipText(resourceMap.getString("goToLocationButton.toolTipText")); // NOI18N
    goToAddressButton.setName("goToLocationButton"); // NOI18N
    goToAddressButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        goToAddressButtonActionPerformed(evt);
      }
    });

    locationBarTextField.setText(resourceMap.getString("locationBarTextField.text")); // NOI18N
    locationBarTextField.setName("locationBarTextField"); // NOI18N
    locationBarTextField.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        locationBarTextFieldActionPerformed(evt);
      }
    });

    appHeaderSeparator.setName("appHeaderSeparator"); // NOI18N

    javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
    mainPanel.setLayout(mainPanelLayout);
    mainPanelLayout.setHorizontalGroup(
      mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(mainSplitPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
          .addGroup(mainPanelLayout.createSequentialGroup()
            .addComponent(newButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(openButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(saveButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(locationBarTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(goToAddressButton)))
        .addContainerGap())
      .addComponent(appHeaderSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE)
    );
    mainPanelLayout.setVerticalGroup(
      mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(mainPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(newButton)
            .addComponent(openButton)
            .addComponent(saveButton))
          .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(goToAddressButton)
            .addComponent(locationBarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(appHeaderSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
        .addContainerGap())
    );

    mainPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {goToAddressButton, locationBarTextField, newButton, openButton, saveButton});

    menuBar.setName("menuBar"); // NOI18N

    fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
    fileMenu.setName("fileMenu"); // NOI18N

    newDocumentMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
    newDocumentMenuItem.setIcon(resourceMap.getIcon("newDocumentMenuItem.icon")); // NOI18N
    newDocumentMenuItem.setText(resourceMap.getString("newDocumentMenuItem.text")); // NOI18N
    newDocumentMenuItem.setEnabled(false);
    newDocumentMenuItem.setName("newDocumentMenuItem"); // NOI18N
    fileMenu.add(newDocumentMenuItem);

    openDocumentMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    openDocumentMenuItem.setIcon(resourceMap.getIcon("openDocumentMenuItem.icon")); // NOI18N
    openDocumentMenuItem.setText(resourceMap.getString("openDocumentMenuItem.text")); // NOI18N
    openDocumentMenuItem.setName("openDocumentMenuItem"); // NOI18N
    openDocumentMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openDocumentMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(openDocumentMenuItem);

    saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    saveMenuItem.setIcon(resourceMap.getIcon("saveMenuItem.icon")); // NOI18N
    saveMenuItem.setText(resourceMap.getString("saveMenuItem.text")); // NOI18N
    saveMenuItem.setName("saveMenuItem"); // NOI18N
    fileMenu.add(saveMenuItem);

    saveAsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    saveAsMenuItem.setIcon(resourceMap.getIcon("saveAsMenuItem.icon")); // NOI18N
    saveAsMenuItem.setText(resourceMap.getString("saveAsMenuItem.text")); // NOI18N
    saveAsMenuItem.setName("saveAsMenuItem"); // NOI18N
    saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAsMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(saveAsMenuItem);

    separator1.setName("separator1"); // NOI18N
    fileMenu.add(separator1);

    ParseFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    ParseFileMenuItem.setIcon(resourceMap.getIcon("ParseFileMenuItem.icon")); // NOI18N
    ParseFileMenuItem.setText(resourceMap.getString("ParseFileMenuItem.text")); // NOI18N
    ParseFileMenuItem.setName("ParseFileMenuItem"); // NOI18N
    ParseFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ParseFileMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(ParseFileMenuItem);

    ParseURLMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
    ParseURLMenuItem.setIcon(resourceMap.getIcon("ParseURLMenuItem.icon")); // NOI18N
    ParseURLMenuItem.setText(resourceMap.getString("ParseURLMenuItem.text")); // NOI18N
    ParseURLMenuItem.setName("ParseURLMenuItem"); // NOI18N
    ParseURLMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ParseURLMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(ParseURLMenuItem);

    separator2.setName("separator2"); // NOI18N
    fileMenu.add(separator2);

    javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(TreeHTMLParseView.class, this);
    exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
    exitMenuItem.setIcon(resourceMap.getIcon("exitMenuItem.icon")); // NOI18N
    exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
    exitMenuItem.setName("exitMenuItem"); // NOI18N
    fileMenu.add(exitMenuItem);

    menuBar.add(fileMenu);

    editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
    editMenu.setName("editMenu"); // NOI18N

    preferencesMenuItem.setIcon(resourceMap.getIcon("preferencesMenuItem.icon")); // NOI18N
    preferencesMenuItem.setText(resourceMap.getString("preferencesMenuItem.text")); // NOI18N
    preferencesMenuItem.setEnabled(false);
    preferencesMenuItem.setName("preferencesMenuItem"); // NOI18N
    preferencesMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        preferencesMenuItemActionPerformed(evt);
      }
    });
    editMenu.add(preferencesMenuItem);

    alterSourceMenuItem.setIcon(resourceMap.getIcon("alterSourceMenuItem.icon")); // NOI18N
    alterSourceMenuItem.setText(resourceMap.getString("alterSourceMenuItem.text")); // NOI18N
    alterSourceMenuItem.setName("alterSourceMenuItem"); // NOI18N
    alterSourceMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        alterSourceMenuItemActionPerformed(evt);
      }
    });
    editMenu.add(alterSourceMenuItem);

    urlFilterMEnuItem.setIcon(resourceMap.getIcon("urlFilterMEnuItem.icon")); // NOI18N
    urlFilterMEnuItem.setText(resourceMap.getString("urlFilterMEnuItem.text")); // NOI18N
    urlFilterMEnuItem.setName("urlFilterMEnuItem"); // NOI18N
    urlFilterMEnuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        urlFilterMEnuItemActionPerformed(evt);
      }
    });
    editMenu.add(urlFilterMEnuItem);

    menuBar.add(editMenu);

    viewMenu.setText(resourceMap.getString("viewMenu.text")); // NOI18N
    viewMenu.setName("viewMenu"); // NOI18N

    refreshMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
    refreshMenuItem.setIcon(resourceMap.getIcon("refreshMenuItem.icon")); // NOI18N
    refreshMenuItem.setText(resourceMap.getString("refreshMenuItem.text")); // NOI18N
    refreshMenuItem.setName("refreshMenuItem"); // NOI18N
    refreshMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        refreshMenuItemActionPerformed(evt);
      }
    });
    viewMenu.add(refreshMenuItem);

    menuBar.add(viewMenu);

    helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
    helpMenu.setName("helpMenu"); // NOI18N

    aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
    aboutMenuItem.setName("aboutMenuItem"); // NOI18N
    helpMenu.add(aboutMenuItem);

    menuBar.add(helpMenu);

    statusPanel.setName("statusPanel"); // NOI18N

    statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

    statusMessageLabel.setIcon(resourceMap.getIcon("statusMessageLabel.icon")); // NOI18N
    statusMessageLabel.setText(resourceMap.getString("statusMessageLabel.text")); // NOI18N
    statusMessageLabel.setEnabled(false);
    statusMessageLabel.setName("statusMessageLabel"); // NOI18N

    statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

    progressBar.setName("progressBar"); // NOI18N

    javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
    statusPanel.setLayout(statusPanelLayout);
    statusPanelLayout.setHorizontalGroup(
      statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE)
      .addGroup(statusPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(statusMessageLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 430, Short.MAX_VALUE)
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(statusAnimationLabel)
        .addContainerGap())
    );
    statusPanelLayout.setVerticalGroup(
      statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(statusPanelLayout.createSequentialGroup()
        .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(statusMessageLabel)
          .addComponent(statusAnimationLabel)
          .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(3, 3, 3))
    );

    setComponent(mainPanel);
    setMenuBar(menuBar);
    setStatusBar(statusPanel);
  }// </editor-fold>//GEN-END:initComponents

    private void ParseFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ParseFileMenuItemActionPerformed
        chooser.setFileFilter(markupFileFilter);
        chooser.setSelectedFile(new File ("./"));

        int r = chooser.showOpenDialog(mainPanel);
        if (r != JFileChooser.APPROVE_OPTION) return;

        appLogger.log(Level.INFO, "Opening: {0}", chooser.getSelectedFile().getPath());

        HTMLTree.clearSelection();
        updateHTMLTree(chooser.getSelectedFile().getPath());
    }//GEN-LAST:event_ParseFileMenuItemActionPerformed

    private void ParseURLMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ParseURLMenuItemActionPerformed

        String source = JOptionPane.showInputDialog("Type URL", "http://www.detiknews.com/read/2010/03/11/224203/1316662/10/misteri-siapa-perempuan-yang-tinggal-bersama-dulmatin-di-pamulang");
        if (source != null) {
            try {
                URL url = new URL(source);

                appLogger.log(Level.INFO, "Opening: {0}", source);
                HTMLTree.clearSelection();
                updateHTMLTree(source);

                XPathTreeConfigNode c = (XPathTreeConfigNode) ((XPathTreeConfigInitialNodes) xPathRootNode.getRoot()).getUserObject();
                c.setSourceURL(source);
                c.setHostName(url.getHost());
            } catch (MalformedURLException ex) {
                Logger.getLogger(TreeHTMLParseView.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex + "\nError Opening URL :" + source, "Error Malformed URL Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_ParseURLMenuItemActionPerformed

    private void refreshMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshMenuItemActionPerformed
        updateHTMLTree(this.path);
    }//GEN-LAST:event_refreshMenuItemActionPerformed

    private void xPathTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_xPathTreeValueChanged
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) xPathTree.getLastSelectedPathComponent();
        if (node != null) {
            String namaObjectPilihan = node.getUserObject().getClass().getName();
            if (namaObjectPilihan.matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode")) {
                org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode Entry = (org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode) node.getUserObject();
                entryItem.setEntryItem(Entry);
                rightSplitPane.setRightComponent(entryItem);
                rightSplitPane.setDividerLocation(rightSplitPane.getHeight() - entryItem.getPreferredSize().height);
            } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeFieldNode")) {
                org.emje.treehtmlparse.xpathtree.nodes.XPathTreeFieldNode Field = (org.emje.treehtmlparse.xpathtree.nodes.XPathTreeFieldNode) node.getUserObject();
                fieldItem.setFieldItem(Field);
                rightSplitPane.setRightComponent(fieldItem);
                rightSplitPane.setDividerLocation(rightSplitPane.getHeight() - fieldItem.getPreferredSize().height);
            } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode")) {
                org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode Config = (org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode) node.getUserObject();
                configItem.setConfigItem(Config);
                rightSplitPane.setRightComponent(configItem);
                rightSplitPane.setDividerLocation(rightSplitPane.getHeight() - configItem.getPreferredSize().height);
            } else {
                if (node.toString().equalsIgnoreCase("Add new Field")) {
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                    DefaultMutableTreeNode newCategory = (new XPathTreeFieldInitialNodes("New Field").getInitialCategoryNode());
                    ((DefaultTreeModel) xPathTree.getModel()).insertNodeInto(newCategory, parentNode, parentNode.getChildCount() - 1);
                    xPathTree.expandPath(new TreePath(newCategory.getPath()));
                    xPathTree.setSelectionPath(new TreePath(newCategory.getPath()));
                } else if (node.toString().equalsIgnoreCase("Add new Entry")) {
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                    DefaultMutableTreeNode newEntry = new DefaultMutableTreeNode(new XPathTreeEntryNode("New Entry","xpath.string"));
                    ((DefaultTreeModel) xPathTree.getModel()).insertNodeInto(newEntry, parentNode, parentNode.getChildCount() - 1);
                    xPathTree.setSelectionPath(new TreePath(newEntry.getPath()));
                }
            }
        }
    }//GEN-LAST:event_xPathTreeValueChanged

    private void copyLocationValueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyLocationValueButtonActionPerformed
        if (this.locationComboBox.getSelectedItem() != null)
            System.out.println(this.locationComboBox.getSelectedItem().toString());
    }//GEN-LAST:event_copyLocationValueButtonActionPerformed

    private void xPathTreeClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xPathTreeClearButtonActionPerformed
        xPathRootNode.removeAllChildren();
        xPathRootNode.populateInitialNode();
        xPathTreeModel.reload();
    }//GEN-LAST:event_xPathTreeClearButtonActionPerformed

    private void xPathTreeRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xPathTreeRemoveButtonActionPerformed
        TreePath currentSelection = xPathTree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
            if (parent != null) {
                xPathTreeModel.removeNodeFromParent(currentNode);
                return;
            }
        }
    }//GEN-LAST:event_xPathTreeRemoveButtonActionPerformed

    private void xPathTreeAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xPathTreeAddButtonActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) xPathTree.getLastSelectedPathComponent();
        if (node != null) {
            String namaObjectPilihan = node.getUserObject().getClass().getName();
            if (namaObjectPilihan.matches("org.emje.treehtmlparse.xpathtree.nodes.Config")) {
                DefaultMutableTreeNode newField = (new XPathTreeFieldInitialNodes("New Field").getInitialCategoryNode());
                ((DefaultTreeModel) xPathTree.getModel()).insertNodeInto(newField, node, node.getChildCount() - 1);
                xPathTree.expandPath(new TreePath(newField.getPath()));
                xPathTree.setSelectionPath(new TreePath(newField.getPath()));
            } else {
                DefaultMutableTreeNode newEntry = new DefaultMutableTreeNode(new XPathTreeEntryNode("New Entry","xpath.string"));
                if (namaObjectPilihan.matches("org.emje.treehtmlparse.xpathtree.nodes.Entry")) {
                    node = (DefaultMutableTreeNode) node.getParent();
                }
                ((DefaultTreeModel) xPathTree.getModel()).insertNodeInto(newEntry, node, node.getChildCount() - 1);
                xPathTree.setSelectionPath(new TreePath(newEntry.getPath()));
            }
        }
    }//GEN-LAST:event_xPathTreeAddButtonActionPerformed

    private void xPathTreeMoveUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xPathTreeMoveUpButtonActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) xPathTree.getLastSelectedPathComponent();
        if (node != null && !node.isRoot()) {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
            int nodeIndex = parentNode.getIndex(node);
            if (nodeIndex != 0){
                parentNode.remove(node);
                parentNode.insert(node, nodeIndex - 1);
                xPathTreeModel.nodeStructureChanged(parentNode);
                xPathTreeModel.reload(parentNode);
                xPathTree.setSelectionPath(new TreePath(node.getPath()));
            }
            else {
                if (!parentNode.isRoot() &&  parentNode.getParent().getIndex(parentNode) != 0){
                    DefaultMutableTreeNode parentNodePreviousSibling = (DefaultMutableTreeNode) parentNode.getPreviousSibling();
                    parentNode.remove(node);
                    parentNodePreviousSibling.insert(node, parentNodePreviousSibling.getChildCount() - 1);
                    xPathTreeModel.nodeStructureChanged(parentNodePreviousSibling);
                    xPathTreeModel.reload(parentNodePreviousSibling);
                    xPathTreeModel.reload(parentNode);
                    xPathTree.setSelectionPath(new TreePath(node.getPath()));
                }
            }
        }
    }//GEN-LAST:event_xPathTreeMoveUpButtonActionPerformed

    private void xPathTreeMoveDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xPathTreeMoveDownButtonActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) xPathTree.getLastSelectedPathComponent();
        if (node != null && !node.isRoot()) {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
            int nodeIndex = parentNode.getIndex(node);
            if (nodeIndex != (parentNode.getChildCount() - 2)) {
                parentNode.remove(node);
                parentNode.insert(node, nodeIndex + 1);
                xPathTreeModel.nodeStructureChanged(parentNode);
                xPathTree.setSelectionPath(new TreePath(node.getPath()));
            } else {
                if (!parentNode.isRoot() && parentNode.getParent().getIndex(parentNode) != (parentNode.getParent().getChildCount() - 2)) {
                    DefaultMutableTreeNode parentNodeNextSibling = (DefaultMutableTreeNode) parentNode.getNextSibling();
                    parentNode.remove(node);
                    parentNodeNextSibling.insert(node, 0);
                    xPathTreeModel.nodeStructureChanged(parentNode);
                    xPathTreeModel.nodeStructureChanged(parentNodeNextSibling);
                    xPathTreeModel.reload(parentNodeNextSibling);
                    xPathTree.setSelectionPath(new TreePath(node.getPath()));
                }
            }
        }
    }//GEN-LAST:event_xPathTreeMoveDownButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if(this.targetFile == null){
            this.saveAsMenuItemActionPerformed(evt);
        } else{
            saveAction();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void alterSourceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alterSourceMenuItemActionPerformed
        JFrame mainFrame = TreeHTMLParseApp.getApplication().getMainFrame();
        ((XPathTreeConfigInitialNodes) xPathRootNode.getRoot()).
                setUserObject(AlteredSourceStringView.showDialog(
                    mainFrame, true, (XPathTreeConfigNode) (
                        (XPathTreeConfigInitialNodes) xPathRootNode.getRoot()).getUserObject()));
    }//GEN-LAST:event_alterSourceMenuItemActionPerformed

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        chooser.setFileFilter(configFileFilter);

        int r = chooser.showOpenDialog(mainPanel);
        if (r != JFileChooser.APPROVE_OPTION) {
            return;
        }
        this.targetFile = chooser.getSelectedFile();
        openAction();
    }//GEN-LAST:event_openButtonActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        this.targetFile = new File(this.configItem.getConfigItem().getName() + ".xtcp");
        chooser.setSelectedFile(targetFile);
        int returnVal = chooser.showDialog(mainPanel, "Save As");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            targetFile = chooser.getSelectedFile();
            saveAction();
        } else {
            appLogger.log(Level.INFO, "Save command cancelled by user.\n");
        }
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void HTMLTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_HTMLTreeValueChanged
        HTMLTreeNode node = (HTMLTreeNode) HTMLTree.getLastSelectedPathComponent();
        if (node != null) {
            setSourceViewTextArea(node.getContentStrings());
            setBrowseViewEditorPane(node.getContentStrings());
            // set xPath Location Names alternatives to locationComboBox
            String pathString = "";
            TreeNode[] nodes = node.getPath();
            for (int i = 1; i < nodes.length; i++) {
                pathString += "/" + getPathString(nodes[i]);
            }
            if (!node.isRoot()) {
                ArrayList<String> h = node.getXpathLocations();
                String[] data = new String[h.size()];
                for (int z = 0; z < h.size(); z++) {
                    String d = h.get(z);
                    data[z] = d;
                }
                setLocationComboBoxModel(data);
            } else {
                String[] data = null;
                setLocationComboBoxModel(data);
            }
        }
    }//GEN-LAST:event_HTMLTreeValueChanged

    private void goToAddressButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToAddressButtonActionPerformed
        HTMLTree.clearSelection();
        updateHTMLTree(locationBarTextField.getText());
    }//GEN-LAST:event_goToAddressButtonActionPerformed

    private void locationBarTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationBarTextFieldActionPerformed
        goToAddressButtonActionPerformed(evt);
        locationBarTextField.setCaretPosition(0);
    }//GEN-LAST:event_locationBarTextFieldActionPerformed

    private void urlFilterMEnuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlFilterMEnuItemActionPerformed
        JFrame mainFrame = TreeHTMLParseApp.getApplication().getMainFrame();
        ((XPathTreeConfigInitialNodes) xPathRootNode.getRoot()).
                setUserObject(URLFilterView.showDialog(
                    mainFrame, true, (XPathTreeConfigNode) (
                        (XPathTreeConfigInitialNodes) xPathRootNode.getRoot()).getUserObject()));
    }//GEN-LAST:event_urlFilterMEnuItemActionPerformed

    private void openDocumentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDocumentMenuItemActionPerformed
        openButtonActionPerformed(evt);
    }//GEN-LAST:event_openDocumentMenuItemActionPerformed

    private void preferencesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preferencesMenuItemActionPerformed
        if (preferencesBox == null) {
            JFrame mainFrame = TreeHTMLParseApp.getApplication().getMainFrame();
            preferencesBox = new Preferences(mainFrame, (XPathTreeConfigNode) ((XPathTreeConfigInitialNodes) xPathRootNode.getRoot()).getUserObject());
            preferencesBox.backupDataBeforeEdit();
            preferencesBox.setLocationRelativeTo(mainFrame);
        } else {
            preferencesBox.backupDataBeforeEdit();
        }
        TreeHTMLParseApp.getApplication().show(preferencesBox);
    }//GEN-LAST:event_preferencesMenuItemActionPerformed

    private void configurationResultRefreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configurationResultRefreshButtonActionPerformed
        ConfigurationResult cr = new ConfigurationResult(this.parse);
        configurationResultTree.setModel(new DefaultTreeModel(cr.getResult(xPathRootNode)));
    }//GEN-LAST:event_configurationResultRefreshButtonActionPerformed

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        TreeHTMLParseApp.main(null);
        //        TreeHTMLParseApp.main(new String[]{"-dispose"});
}//GEN-LAST:event_newButtonActionPerformed

    private void saveAction(){
        String configuration = this.getConfiguration();
        String sourceString = parse.getSourceString();

        // Creating a temporary configuration file before compressing it
        File configFile = new File("configuration.xml");
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(configFile));
            bw.write(configuration);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(TreeHTMLParseView.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError Creating Temporary Configuration File", "Save Action : IO Error", JOptionPane.ERROR_MESSAGE);
        }

        // Creating a temporary source file before compressing it
        File sourceFile = new File("source.xhtml");
        try {
            bw = new BufferedWriter(new FileWriter(sourceFile));
            bw.write(sourceString);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(TreeHTMLParseView.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError Creating Temporary Source File", "Save Action : IO Error", JOptionPane.ERROR_MESSAGE);
        }

        // Include the two files writed above in the ZIP file
        File[] source = new File[]{configFile, sourceFile};

        // Create a buffer for reading the files
        byte[] buf = new byte[1024];

        try {
            // Create the ZIP file
            String target = targetFile.getAbsolutePath();
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(target));

            // Compress the files
            for (int i = 0; i < source.length; i++) {
                FileInputStream in = new FileInputStream(source[i]);

                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(source[i].getName()));

                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                // Complete the entry
                out.closeEntry();
                in.close();
            }

            // Complete the ZIP file
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(TreeHTMLParseView.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex + "\nError Creating ZIP configuration File", "Save Action : IO Error", JOptionPane.ERROR_MESSAGE);
        }

        //Delete Temporary source file
        if (!sourceFile.delete()) {
            JOptionPane.showMessageDialog(null, "Error Deleting configuration File on: " + configFile.getAbsolutePath(), "Save Action : IO Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Delete Temporary config file
        if (!configFile.delete()) {
            JOptionPane.showMessageDialog(null, "Error Deleting configuration File on: " + configFile.getAbsolutePath(), "Save Action : IO Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // set the application title
        getFrame().setTitle(targetFile.getName() + " - Tree HTML Parse");
    }

    private void openAction(){
        appLogger.log(Level.INFO, "Opening: {0}", this.targetFile);

        XTCPFileParser fileParser = new XTCPFileParser(this.targetFile);

        // parse configFile
        ConfigFileParser parser = new ConfigFileParser(fileParser.getConfigFile().getAbsolutePath());
        parser.parse();
        xPathRootNode = null;
        xPathRootNode = parser.getConfigTreeNode();

        xPathTreeModel = null;
        xPathTreeModel = new DefaultTreeModel(xPathRootNode);
        xPathTreeModel.reload();
        xPathTree.setModel(xPathTreeModel);
        xPathTree.setSelectionRow(0);

        // parse source File
        updateHTMLTree(fileParser.getSourceFile().getAbsolutePath());
        setHTMLTreeRootNodeURL(parser.getSourceURL());
        this.path = parser.getSourceURL();
        locationBarTextField.setText(parser.getSourceURL());
        locationBarTextField.setCaretPosition(0);

        // set the application title
        getFrame().setTitle(targetFile.getName() + " - Tree HTML Parse");
    }

    private void updateHTMLTree(String path){
        this.path = path;
        parse = new HtmlParse(this.path, (XPathTreeConfigNode) ((XPathTreeConfigInitialNodes) xPathRootNode.getRoot()).getUserObject());        
        configItem.setConfigItem((XPathTreeConfigNode) ((XPathTreeConfigInitialNodes) xPathRootNode.getRoot()).getUserObject());
        entryItem.setParser(parse);
        HTMLTree.setModel(new DefaultTreeModel(parse.getRootNode()));
        locationBarTextField.setText(path);
        locationBarTextField.setCaretPosition(0);
        setHTMLTreeRootNodeURL(path);
    }

    private void setHTMLTreeRootNodeURL(String url){
        HTMLTreeNode rootNode = (HTMLTreeNode) HTMLTree.getModel().getRoot();
        ((org.emje.treehtmlparse.htmltree.nodes.HTMLURL) rootNode.getUserObject()).setURLString(url);
    }

    private String getHTMLTreeRootNodeURL(){
        HTMLTreeNode rootNode = (HTMLTreeNode) HTMLTree.getModel().getRoot();
        return ((org.emje.treehtmlparse.htmltree.nodes.HTMLURL) rootNode.getUserObject()).getURLString();
    }

    private String getPathString(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        String namaObjectPilihan = node.getUserObject().getClass().getName();
        if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLTag")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLTag HTMLTag = (org.emje.treehtmlparse.htmltree.nodes.HTMLTag) node.getUserObject();
            return HTMLTag.getTagName();
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag HTMLSimpleTag = (org.emje.treehtmlparse.htmltree.nodes.HTMLSimpleTag) node.getUserObject();
            return HTMLSimpleTag.getTagName();
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLText")) {
            return "text()";
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLComment")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLComment HTMLComment = (org.emje.treehtmlparse.htmltree.nodes.HTMLComment) node.getUserObject();
            return HTMLComment.toString();
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLParsingError")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLParsingError HTMLParsingError = (org.emje.treehtmlparse.htmltree.nodes.HTMLParsingError) node.getUserObject();
            return HTMLParsingError.toString();
        } else if (namaObjectPilihan.matches("org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute")) {
            org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute HTMLTagAttribute = (org.emje.treehtmlparse.htmltree.nodes.HTMLTagAttribute) node.getUserObject();
            return "@" + HTMLTagAttribute.toString();
        }
        return null;
    }

    public void setSourceViewTextArea(String text){
        this.sourceViewTextArea.setText(text);
        this.sourceViewTextArea.setCaretPosition(0);
    }

    public void setBrowseViewEditorPane(String text){
        this.browseViewEditorPane.setText("<html>"+text+"</html>");
        this.browseViewEditorPane.setCaretPosition(0);
    }

    public void setLocationComboBoxModel(String[] data){
        if(data != null ){
            this.locationComboBox.setEnabled(true);
            this.locationComboBox.setModel(new javax.swing.DefaultComboBoxModel(data));
            // select the last item (usually the best xpath path location string)
            this.locationComboBox.setSelectedIndex(this.locationComboBox.getItemCount() - 1);
        } else {
            this.locationComboBox.setEnabled(false);
        }
    }

    private String getConfiguration(){
        StringBuilder configuration = new StringBuilder();
        configuration.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"+"<config xmlns=\"http://ammarshadiq.web.id/TR/TreeHtmlParse/\">\n");

        // Get xPathTree Structure and data
        Enumeration en = this.xPathRootNode.preorderEnumeration();
        boolean hitConfigTag = false;
        boolean hitFieldTag = false;
        while (en.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
            String objectName = node.getUserObject().getClass().getName();
            if (objectName.matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeConfigNode")) {
                hitConfigTag = true;
                XPathTreeConfigNode conf = (XPathTreeConfigNode) node.getUserObject();
                configuration.append("\t<name>").append(replaceDOMEscapeCharacter(conf.getName())).append("</name>\n");
                if (getHTMLTreeRootNodeURL() != null ) configuration.append("\t<source>").append(replaceDOMEscapeCharacter(parse.getRootNode().toString())).append("</source>\n");
                if (conf.getHostName() != null ) configuration.append("\t<host>").append(conf.getHostName()).append("</host>\n");
                if (conf.getParser() != null ) configuration.append("\t<parser>").append(conf.getParser()).append("</parser>\n");

                // Get Alter source data
                if ((conf.getReplacedDataTableModel().getRowCount() != 0) || !conf.getRemovedDataListModel().isEmpty()) {
                    configuration.append("\t<alter>\n");

                    // Get Replaced Text
                    for (int i = 0; i < conf.getReplacedDataTableModel().getRowCount(); i++) {
                        String text = replaceDOMEscapeCharacter((String) conf.getReplacedDataTableModel().getValueAt(i, 0));
                        String with = replaceDOMEscapeCharacter((String) conf.getReplacedDataTableModel().getValueAt(i, 1));
                        configuration.append("\t\t<replace text=\"").append(text).append("\" with=\"").append(with).append("\" /> \n");
                    }

                    // Get Removed Text
                    Enumeration listEn = conf.getRemovedDataListModel().elements();
                    while (listEn.hasMoreElements()) {
                        String text = replaceDOMEscapeCharacter((String) listEn.nextElement());
                        configuration.append("\t\t<remove text=\"").append(text).append("\" /> \n");
                    }
                    configuration.append("\t</alter>\n");
                }

                // get URL Filter data
                if(!conf.getAcceptedURLListModel().isEmpty() || !conf.getSkippedURLListModel().isEmpty()){
                    configuration.append("\t<urlfilter>\n");
                    // get Accepted URL
                    Enumeration listEnAcceptedURL = conf.getAcceptedURLListModel().elements();
                    while (listEnAcceptedURL.hasMoreElements()) {
                        String value = replaceDOMEscapeCharacter((String) listEnAcceptedURL.nextElement());
                        configuration.append("\t\t<accepturl value=\"").append(value).append("\" /> \n");
                    }
                    
                    // get skipped URL
                    Enumeration listEnSkippedURL = conf.getSkippedURLListModel().elements();
                    while (listEnSkippedURL.hasMoreElements()) {
                        String value = replaceDOMEscapeCharacter((String) listEnSkippedURL.nextElement());
                        configuration.append("\t\t<skipurl value=\"").append(value).append("\" /> \n");
                    }
                    configuration.append("\t</urlfilter>\n");
                }
            } else if (objectName.matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeFieldNode")) {
                if (hitFieldTag) {
                    configuration.append("\t</field>\n");
                }
                XPathTreeFieldNode field = (XPathTreeFieldNode) node.getUserObject();
                if (field.getFieldType().equalsIgnoreCase(XPathTreeFieldNode.CONTINUOUS_TEXT)){
                    configuration.append("\t<field name=\"").append(replaceDOMEscapeCharacter(field.getFieldName()))
                            .append("\" type=\"").append(field.getFieldType())
                            .append("\" delimiter=\"").append(replaceDOMEscapeCharacter(field.getEntryDelimiter())).append("\" >\n");
                } else {
                    configuration.append("\t<field name=\"").append(field.getFieldName())
                            .append("\" type=\"").append(field.getFieldType()).append("\" >\n");
                }
                hitFieldTag = true;
            } else if (objectName.matches("org.emje.treehtmlparse.xpathtree.nodes.XPathTreeEntryNode")) {
                XPathTreeEntryNode entry = (XPathTreeEntryNode) node.getUserObject();
                configuration.append("\t\t<entry type=\"").append(entry.getExpressionType())
                        .append("\" value=\"").append(entry.getxPathExpression()).append("\" />\n");
            }
        }
        if (hitFieldTag) {
            configuration.append("\t</field>\n");
        }
        if (hitConfigTag) {
            configuration.append("</config>");
        }
        return configuration.toString();
    }

  /**
     * Escape sequences that represent single characters are actually called character
     * references in XML. There are a handful of predefined character references that you
     * can use when working with XML. Table A shows these character references.
     *
     * <table>
     *  <tr>
     *      <td>Ampersand</td>
     *      <td>&amp;</td>
     *      <td>&amp;amp;</td>
     *  </tr>
     *  <tr>
     *      <td>Greater-than</td>
     *      <td>&gt;</td>
     *      <td>&amp;gt;</td>
     *  </tr>
     *  <tr>
     *      <td>Less-than</td>
     *      <td>&lt;</td>
     *      <td>&amp;lt;</td>
     *  </tr>
     *  <tr>
     *      <td>Apostrophe</td>
     *      <td>'</td>
     *      <td>&amp;apos;</td>
     *  </tr>
     *  <tr>
     *      <td>Quote</td>
     *      <td>"</td>
     *      <td>&amp;quot;</td>
     *  </tr>
     *  <tr>
     *      <td>Newline</td>
     *      <td>\n</td>
     *      <td>&amp;#10;</td>
     *  </tr>
     *  <tr>
     *      <td>Tab</td>
     *      <td>\t</td>
     *      <td>&amp;#09;</td>
     *  </tr>
     * </table>
     *
     * Working with XML data is sometimes challenging, and there are usually certain
     * caveats to be aware of. Using characters such as ampersands and greater-than
     * symbols can cause your XML parser to fail, even though the data appears correct.
     * Fortunately, you can rely on predefined character references to avoid problems with
     * special characters.
     *
     * ref : http://articles.techrepublic.com.com/5100-10878_11-5031844.html [Accessed 8-25-2010]
     *
     * @param str a String of character that may or may not contains Escape Character sequence
     * @return Replaced String of characters with escape sequence
     */
    private String replaceDOMEscapeCharacter(String str){
        String ret = str;
        ret = ret.replaceAll("&", "&amp;");
        ret = ret.replaceAll("'", "&apos;");
        ret = ret.replaceAll("\"", "&quot;");
        ret = ret.replaceAll("<", "&lt;");
        ret = ret.replaceAll(">", "&gt;");
        ret = ret.replaceAll("\t", "&#09;");
        ret = ret.replaceAll("\n", "&#10;");
        return ret;
    }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTabbedPane ComponentViews;
  private javax.swing.JTree HTMLTree;
  private javax.swing.JPanel HTMLTreePanel;
  private javax.swing.JMenuItem ParseFileMenuItem;
  private javax.swing.JMenuItem ParseURLMenuItem;
  private javax.swing.JMenuItem alterSourceMenuItem;
  private javax.swing.JSeparator appHeaderSeparator;
  private javax.swing.JEditorPane browseViewEditorPane;
  private javax.swing.JPanel browseViewPanel;
  private javax.swing.JScrollPane browseViewScrollPane;
  private javax.swing.JPanel configurationResultPanel;
  private javax.swing.JButton configurationResultRefreshButton;
  private javax.swing.JTree configurationResultTree;
  private javax.swing.JScrollPane configurationResultTreeScrollPane;
  private javax.swing.JButton copyLocationValueButton;
  private javax.swing.JMenu editMenu;
  private javax.swing.JButton goToAddressButton;
  private javax.swing.JPanel leftPanel;
  private javax.swing.JSplitPane leftSplitPane;
  private javax.swing.JTextField locationBarTextField;
  private javax.swing.JComboBox locationComboBox;
  private javax.swing.JLabel locationLabel;
  private javax.swing.JPanel mainPanel;
  private javax.swing.JSplitPane mainSplitPane;
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JButton newButton;
  private javax.swing.JMenuItem newDocumentMenuItem;
  private javax.swing.JButton openButton;
  private javax.swing.JMenuItem openDocumentMenuItem;
  private javax.swing.JMenuItem preferencesMenuItem;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JMenuItem refreshMenuItem;
  private javax.swing.JPanel rightPanel;
  private javax.swing.JSplitPane rightSplitPane;
  private javax.swing.JMenuItem saveAsMenuItem;
  private javax.swing.JButton saveButton;
  private javax.swing.JMenuItem saveMenuItem;
  private javax.swing.JButton searchButton;
  private javax.swing.JTextField searchSourceTextField;
  private javax.swing.JPopupMenu.Separator separator1;
  private javax.swing.JPopupMenu.Separator separator2;
  private javax.swing.JScrollPane sourceViewScrollPane;
  private javax.swing.JTextArea sourceViewTextArea;
  private javax.swing.JLabel statusAnimationLabel;
  private javax.swing.JLabel statusMessageLabel;
  private javax.swing.JPanel statusPanel;
  private javax.swing.JScrollPane treeScrollPane;
  private javax.swing.JMenuItem urlFilterMEnuItem;
  private javax.swing.JMenu viewMenu;
  private javax.swing.JTree xPathTree;
  private javax.swing.JButton xPathTreeAddButton;
  private javax.swing.JButton xPathTreeClearButton;
  private javax.swing.JButton xPathTreeMoveDownButton;
  private javax.swing.JButton xPathTreeMoveUpButton;
  private javax.swing.JPanel xPathTreePanel;
  private javax.swing.JButton xPathTreeRemoveButton;
  private javax.swing.JScrollPane xPathTreeScrollPane;
  // End of variables declaration//GEN-END:variables

    private JFileChooser chooser;

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private HtmlParse parse = new HtmlParse();
    private String path = null;

    private File targetFile;

    public static boolean viewError;
    public static boolean viewComment;

    private XPathTreeConfigInitialNodes xPathRootNode = new XPathTreeConfigInitialNodes("Config Name");
    private DefaultTreeModel xPathTreeModel = new DefaultTreeModel(xPathRootNode);
    private XPathConfigItem configItem = new XPathConfigItem();
    private XPathFieldItem fieldItem = new XPathFieldItem();
    private XPathEntryItem entryItem = new XPathEntryItem();

    private JDialog aboutBox;
    private Preferences preferencesBox;

    private final ConfigFileFilter configFileFilter = new ConfigFileFilter();
    private final MarkupFileFilter markupFileFilter = new MarkupFileFilter();

    private final class ConfigFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals("xtcp")){
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        //The description of this filter
        public String getDescription() {
            return "X-Tree Config Files";
        }

        public String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }
    }

    private final class MarkupFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals("html")){
                    return true;
                } else if (extension.equals("xhtml")){
                    return true;
                } else if (extension.equals("xml")){
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        //The description of this filter
        public String getDescription() {
            return "HTML, XHTML, XML Files";
        }

        public String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }
    }

    public static final Logger appLogger = Logger.getLogger("HTMLTreeParse");
}