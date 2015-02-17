/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

import ij.ImagePlus;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Clock;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author nikla_000
 */
public class DisplayFrame extends JFrame implements ActionListener {

    processing.core.PApplet sketch;
    JButton fileChooseButton;
    JButton button;
    JButton clearButton;
    ProcessImage imageProcessor;

    public DisplayFrame() {
        this.setSize(450, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        button = new JButton("Start");
        fileChooseButton = new JButton("Velg fil");
        clearButton = new JButton("Clear Canvas");
        imageProcessor = new ProcessImage();

        setLayout(new FlowLayout());
        panel.setBounds(20, 20, 450, 500);
        sketch = new CircleSketch();
        panel.add(sketch);
        this.add(panel);
        this.add(button);
        this.add(fileChooseButton);
        this.add(clearButton);

        button.addActionListener(this);
        fileChooseButton.addActionListener(this);
        clearButton.addActionListener(this);

        sketch.init(); //this is the function used to start the execution of the sketch
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            sketch.start();
        } else if (e.getSource() == fileChooseButton) {
            chooseFile();
        } else if (e.getSource() == clearButton) {
            sketch.clear();
        }
    }

    public void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG, GIF & PNG", "jpg", "gif", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            imageProcessor.setCurrentImage(chooser.getSelectedFile().getAbsolutePath());
        }
    }  
}
