/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author nikla_000
 */
public class DisplayFrame extends JFrame implements ActionListener {

    //private final CircleSketch sketch;
    private final JButton fileChooseButton;
    private final JButton button;
    private final JButton fncButton2;
    private final JButton fncButton3;
    private final JButton clearButton;
    private final JButton doubleSpeed;
    private final JButton tripleSpeed;
    private final JButton saveButton;

    private final JLabel step1;
    private final JLabel step2;
    private final JLabel step3;
    private final JLabel sliderLabel;

    private final ProcessImage imageProcessor;
    private JPanel panel;
    private final JSlider slider;

    SampleSketch s;

    /**
     * Constructor for instances of class DisplayFrame. Initializes all
     * components of the GUI as well as the processing sketch.
     */
    public DisplayFrame() throws IOException {
        this.setSize(1600, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        button = new JButton("Start");
        fncButton2 = new JButton("Pixelate");
        fncButton3 = new JButton("Show dots");
        fileChooseButton = new JButton("Velg fil");
        clearButton = new JButton("Clear Canvas");
        doubleSpeed = new JButton("2X Speed");
        tripleSpeed = new JButton("3X Speed");
        imageProcessor = new ProcessImage();

        saveButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/Save-icon.png"))));

        step1 = new JLabel("Step1: Choose a picture");
        step2 = new JLabel("Step2: Choose a function");
        step3 = new JLabel("Step3: Edit the result");
        sliderLabel = new JLabel("Change size of \"pixels\"");

        slider = new JSlider(JSlider.HORIZONTAL, 4, 30, 20);

        fncButton3.setToolTipText("Show a dot representation for your picture");

        arrangeLayout();

        s = new SampleSketch();

        setActionListeners(s);
        s.init(); //this is the function used to start the execution of the sketch

        panel.add(s);
        this.add(panel);
        this.add(button);
        this.add(fileChooseButton);
        this.add(clearButton);
        this.add(fncButton2);
        this.add(fncButton3);
        this.add(doubleSpeed);
        this.add(tripleSpeed);
        this.add(step1);
        this.add(step2);
        this.add(step3);
        this.add(sliderLabel);
        this.add(slider);
        this.add(saveButton);

        this.setVisible(true);
    }

    /**
     * Arranges the layout of the panel and buttons.
     */
    private void arrangeLayout() {
        setLayout(null);

        panel = new JPanel();
        
        saveButton.setBounds(20, 10, 50, 50);

        panel.setBounds(20, 100, 1280, 720);
        fileChooseButton.setBounds(1320, 145, 215, 50);
        button.setBounds(1320, 1445, 100, 50);
        fncButton2.setBounds(1320, 275, 100, 50);
        fncButton3.setBounds(1435, 275, 100, 50);
        clearButton.setBounds(1320, 420, 215, 50);
        doubleSpeed.setBounds(1320, 490, 100, 50);
        tripleSpeed.setBounds(1435, 490, 100, 50);
        slider.setBounds(1320, 590, 215, 20);

        step1.setBounds(1320, 105, 150, 30);
        step2.setBounds(1320, 235, 150, 30);
        step3.setBounds(1320, 380, 150, 30);
        sliderLabel.setBounds(1360, 560, 150, 30);

        //saveButton.setBorder(BorderFactory.createEmptyBorder());
        //saveButton.setContentAreaFilled(false);
    }

    /**
     * Adds action listener to all relevant components
     *
     * @param s The processing sketch where buttons execute the listener.
     */
    private void setActionListeners(SampleSketch s) {

        button.addActionListener(s);
        button.setActionCommand("run");
        clearButton.addActionListener(s);
        clearButton.setActionCommand("clear");
        fncButton2.addActionListener(s);
        fncButton2.setActionCommand("pxlate");
        fncButton3.addActionListener(s);
        fncButton3.setActionCommand("dot");
        doubleSpeed.addActionListener(s);
        doubleSpeed.setActionCommand("double");
        tripleSpeed.addActionListener(s);
        tripleSpeed.setActionCommand("triple");

        slider.addChangeListener(s);

        fileChooseButton.addActionListener((ActionEvent arg0) -> {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JPG, GIF & PNG", "jpg", "gif", "png");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showOpenDialog(panel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                imageProcessor.setCurrentImage(chooser.getSelectedFile().getAbsolutePath());
                s.loadBgImage(chooser.getSelectedFile());
            }
        });

        saveButton.addActionListener((ActionEvent arg0) -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
//            FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                    "JPG, GIF & PNG", "jpg", "gif", "png");
//            chooser.setFileFilter(filter);
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG: png", "png"));
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG: jpg & jpeg", "jpg", "jpeg"));
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("TIF: tif", "tif"));

            int returnVal = chooser.showOpenDialog(panel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                imageProcessor.setCurrentImage(chooser.getSelectedFile().getAbsolutePath());
                s.saveImage(chooser.getSelectedFile());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == button) {
//            sketch.start();
//        } else if (e.getSource() == fileChooseButton) {
//            chooseFile();
//        } else if (e.getSource() == clearButton) {
//            sketch.clear();
//        }
    }

    public void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG, GIF & PNG", "jpg", "gif", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            imageProcessor.setCurrentImage(chooser.getSelectedFile().getAbsolutePath());
        }
    }
}
