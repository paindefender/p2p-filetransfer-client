/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networksp1client;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import networksp1.FMTEntry;

/**
 *
 * @author Kanat Alimanov
 */

class Downloader implements Runnable{
    static volatile ArrayList<String> progress = new ArrayList<>();
    
    public static void updateProgress(){
        String text = "<html>";
        for (String add: progress){
            text = text + add + "<br/>";
        }
        Frame1.progressLabel.setText(text + "</html>");
        if (progress.isEmpty()){
            Frame1.progressBar.setVisible(false);
        }
    }
    
    public String filename;
    public String host;
    public int port;
    public Socket socket;
    public String folder;
    
    public Downloader(String filename, String host, int port, String folder) throws Exception{
        this.filename = filename;
        this.host = host;
        this.port = port;
        this.folder = folder;
        System.out.println(filename + " " + host + " " + port);
        this.socket = new Socket(this.host, this.port);
        System.out.println("got socket");
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        //output.writeBytes(filename + "\n");
        output.writeChars(filename + "\n");
        progress.add("Downloading " + filename);
        updateProgress();
    }

    @Override
    public void run() {
        try {
            
            InputStream in = socket.getInputStream();
            OutputStream out = new FileOutputStream(folder + "\\" + filename);
            System.out.println("Saving to: " + folder + "\\" + filename);
            
            byte[] bytes = new byte[8*1024];
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
            
            System.out.println("Downloaded!");
            out.close();
            progress.remove("Downloading " + filename);
            updateProgress();
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class Waiter implements Runnable{
    
    ServerSocket welcome;
    String folder;
    
    public Waiter(ServerSocket welcomeSocket, String folder){
        this.welcome = welcomeSocket;
        this.folder = folder;
    }

    @Override
    public void run(){
        try {
            while (true){
                System.out.println("Waiting on port " + welcome.getLocalPort());
                Socket socket = welcome.accept();
                System.out.println("Accepted. ");
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-16")); //If problems occur change this
                String s = input.readLine();
                System.out.println("WAITER: " + s);
                new Thread(new Uploader(socket, s, folder)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Waiter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


class Uploader implements Runnable{
    public Socket outSocket;
    public String filename;
    public String folder;
    
    public Uploader(Socket outSocket, String filename, String folder){
        this.outSocket = outSocket;
        this.filename = filename;
        this.folder = folder;
    }

    @Override
    public void run() {
        OutputStream out = null;
        try {
            
            File file = new File(folder + "\\" + filename);
            long length = file.length();
            byte[] bytes = new byte[8 * 1024];
            InputStream in = new FileInputStream(file);
            out = outSocket.getOutputStream();
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Uploader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(Uploader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

public class Frame1 extends javax.swing.JFrame {

    NetworksP1Client client;
    
    /**
     * Creates new form Frame1
     */
    public Frame1() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startDialog = new javax.swing.JDialog();
        hostPortLabel = new javax.swing.JLabel();
        hostnameTextField = new javax.swing.JTextField();
        portTextField = new javax.swing.JTextField();
        directoryLabel = new javax.swing.JLabel();
        directoryTextField = new javax.swing.JTextField();
        directoryButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        directoryChooser = new javax.swing.JFileChooser();
        progressBar = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        progressLabel = new javax.swing.JLabel();
        searchForLabel = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        foundLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        foundTable = new javax.swing.JTable();
        downloadButton = new javax.swing.JButton();

        startDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        startDialog.setTitle("FAILMAIL 3000");
        startDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        startDialog.setMinimumSize(new java.awt.Dimension(296, 180));
        startDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        startDialog.setResizable(false);
        startDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                startDialogWindowClosed(evt);
            }
        });

        hostPortLabel.setText("Please enter server host and port");

        hostnameTextField.setText("localhost");
        hostnameTextField.setToolTipText("Enter the ip of a server");
        hostnameTextField.setName(""); // NOI18N

        portTextField.setText("port");

        directoryLabel.setText("Please pick public directory");

        directoryTextField.setEditable(false);
        directoryTextField.setText("press the view button");

        directoryButton.setText("View...");
        directoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directoryButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout startDialogLayout = new javax.swing.GroupLayout(startDialog.getContentPane());
        startDialog.getContentPane().setLayout(startDialogLayout);
        startDialogLayout.setHorizontalGroup(
            startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hostPortLabel)
                    .addComponent(directoryLabel)
                    .addGroup(startDialogLayout.createSequentialGroup()
                        .addGroup(startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(directoryTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(hostnameTextField, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(portTextField)
                            .addComponent(directoryButton, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        startDialogLayout.setVerticalGroup(
            startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(hostPortLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostnameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(directoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(startDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(directoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(directoryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton)
                .addContainerGap())
        );

        hostnameTextField.getAccessibleContext().setAccessibleName("");
        hostnameTextField.getAccessibleContext().setAccessibleDescription("");

        directoryChooser.setDialogTitle("Please select a public directory");
        directoryChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        progressBar.setTitle("Download status");
        progressBar.setMinimumSize(new java.awt.Dimension(500, 100));

        progressLabel.setText("...");
        jScrollPane2.setViewportView(progressLabel);

        javax.swing.GroupLayout progressBarLayout = new javax.swing.GroupLayout(progressBar.getContentPane());
        progressBar.getContentPane().setLayout(progressBarLayout);
        progressBarLayout.setHorizontalGroup(
            progressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        progressBarLayout.setVerticalGroup(
            progressBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FailMail3000");
        setMinimumSize(new java.awt.Dimension(640, 480));
        setPreferredSize(new java.awt.Dimension(640, 480));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        searchForLabel.setText("Search for:");

        searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyPressed(evt);
            }
        });

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        foundLabel.setText("Found:");

        foundTable.setAutoCreateRowSorter(true);
        foundTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Filename", "Filetype", "Size", "Modified", "IP", "Port"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        foundTable.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        foundTable.setName(""); // NOI18N
        jScrollPane1.setViewportView(foundTable);

        downloadButton.setText("Download");
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
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
                            .addComponent(searchForLabel)
                            .addComponent(foundLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                            .addComponent(searchTextField))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(downloadButton)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchForLabel)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(foundLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 357, Short.MAX_VALUE)
                        .addComponent(downloadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        try {
            // OKBUTTON:  
            okButton.setEnabled(false);
            directoryChooser.approveSelection();
            client = new NetworksP1Client(hostnameTextField.getText(), Integer.parseInt(portTextField.getText()));
            
            new Thread(new Waiter(client.welcomeSocket, directoryChooser.getCurrentDirectory().getAbsolutePath())).start(); // Creates uploaders
            
            ArrayList<FMTEntry> entries =  client.createFMTEntryList(directoryChooser.getCurrentDirectory().getAbsolutePath());
            client.sendFMTs(entries);
            startDialog.setVisible(false);
            this.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(Frame1.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        directoryTextField.setText(directoryChooser.getCurrentDirectory().getAbsolutePath());
        startDialog.setVisible(true);
        startDialog.setLocationRelativeTo(null);
    }//GEN-LAST:event_formWindowOpened

    private void directoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directoryButtonActionPerformed
        // TODO add your handling code here:
        System.out.println("it should be visible now");
        if (directoryChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
            directoryChooser.setCurrentDirectory(directoryChooser.getSelectedFile());
            directoryTextField.setText(directoryChooser.getSelectedFile().getAbsolutePath());
        }
      else {
        System.out.println("No Selection ");
        }
        
    }//GEN-LAST:event_directoryButtonActionPerformed

    
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        try {
            // TODO add your handling code here:
            client.endConnection();
        } catch (Exception ex) {
            Logger.getLogger(Frame1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosed

    private void startDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_startDialogWindowClosed
        // TODO add your handling code here:
        System.exit(1);
    }//GEN-LAST:event_startDialogWindowClosed

    
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        try {
            // TODO add your handling code here:
            String keyword = searchTextField.getText();
            DefaultTableModel model = (DefaultTableModel) foundTable.getModel();
            foundTable.setModel(model);
            model.setRowCount(0);
            
            ArrayList<FMTEntry> entries = client.getFMTList(keyword);
            for (FMTEntry entry: entries){
                model.addRow(new Object[]{entry.filename,entry.filetype,(long)entry.filesize, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (entry.lastmodified)),entry.ip,entry.port});
            }
        } catch (Exception ex) {
            Logger.getLogger(Frame1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButtonActionPerformed
        // TODO add your handling code here:
        int row = foundTable.getSelectedRow();
        String filename = (String) foundTable.getValueAt(row, 0);
        String ip = (String) foundTable.getValueAt(row, 4);
        int port = (int) foundTable.getValueAt(row, 5);
        
        try {
            new Thread(new Downloader(filename, ip, port, directoryChooser.getCurrentDirectory().getAbsolutePath())).start();
            //progressLabel.setText("Attempting to download " + filename + " from " + ip + ":" + port);
            progressBar.setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(Frame1.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }//GEN-LAST:event_downloadButtonActionPerformed

    private void searchTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10){
            searchButton.doClick();
        }
    }//GEN-LAST:event_searchTextFieldKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton directoryButton;
    private javax.swing.JFileChooser directoryChooser;
    private javax.swing.JLabel directoryLabel;
    private javax.swing.JTextField directoryTextField;
    private javax.swing.JButton downloadButton;
    private javax.swing.JLabel foundLabel;
    private javax.swing.JTable foundTable;
    private javax.swing.JLabel hostPortLabel;
    private javax.swing.JTextField hostnameTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField portTextField;
    public static javax.swing.JDialog progressBar;
    public static javax.swing.JLabel progressLabel;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel searchForLabel;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JDialog startDialog;
    // End of variables declaration//GEN-END:variables
}
