package com.morntea.test;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import javax.imageio.*;

public class Converting extends JFrame {
    JLabel promptLabel;
    JTextField prompt;
    JButton promptButton;
    JFileChooser fileChooser;
    JComboBox comboBox;
    JButton saveButton;
    public Converting() {
        super("Image Conversion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        JPanel inputPanel = new JPanel();
        promptLabel = new JLabel("Filename:");
        inputPanel.add(promptLabel);
        prompt = new JTextField(20);
        inputPanel.add(prompt);
        promptButton = new JButton("Browse");
        inputPanel.add(promptButton);
        contentPane.add(inputPanel, BorderLayout.NORTH);

        fileChooser = new JFileChooser();
        promptButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int returnValue = 
                          fileChooser.showOpenDialog(null);
                    if (returnValue == 
                         JFileChooser.APPROVE_OPTION) {
                      File selectedFile = 
                            fileChooser.getSelectedFile();
                       if (selectedFile != null) {
                          prompt.setText(selectedFile.getAbsolutePath());
                       }
                    }
                }
            }
        );

        JPanel outputPanel = new JPanel();
        String writerFormats[] = 
                ImageIO.getWriterFormatNames();
        ComboBoxModel comboBoxModel = new 
                DefaultComboBoxModel(writerFormats);
        comboBox = new JComboBox(comboBoxModel);
        outputPanel.add(comboBox);
        saveButton = new JButton("Save");
        outputPanel.add(saveButton);
        saveButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                    String name = prompt.getText();
                    File file = new File(name);
                    if (file.exists()) {
                        BufferedImage image = 
                                ImageIO.read(file.toURL());
                    if (image == null) {
                        System.err.println("Invalid input file format");
                    } else {
                       String selection = 
                             (String)comboBox.getSelectedItem();
                        String outputFilename = name + 
                               "." + selection;
                        File outputFile = new File(outputFilename);
                        boolean found = ImageIO.write(image, 
                                selection, outputFile);
                        if (found) {
                          JDialog window = new JDialog();
                          Container windowContent = 
                                   window.getContentPane();
                          BufferedImage newImage = 
                                   ImageIO.read(outputFile);
                          JLabel label = new JLabel(new 
                                   ImageIcon(newImage));
                         JScrollPane pane = new 
                                   JScrollPane(label);
                        windowContent.add(pane, 
                                 BorderLayout.CENTER);
                        window.setSize(300, 300);
                        window.show();
                    } else {
                      System.err.println("Error saving");
                    }
                 }
              } else {
                 System.err.println("Bad filename");
              }
           } catch (MalformedURLException mur) {
              System.err.println("Bad filename");
          } catch (IOException ioe) {
             System.err.println("Error reading file");
         }
      }
    }
  );

  contentPane.add(outputPanel, BorderLayout.SOUTH);

  }
  public static void main(String args[]) {
      JFrame frame = new Converting();
      frame.pack();
      frame.show();
  }
}