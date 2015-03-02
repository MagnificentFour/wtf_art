/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    
    private final JLabel step1;
    private final JLabel step2;
    private final JLabel step3;
    private final JLabel sliderLabel;
    
    private ProcessImage imageProcessor;
    private JPanel panel;
    private JSlider slider;
    
    SampleSketch s;

    /**
     * Constructor for instances of class DisplayFrame. Initializes all components
     * of the GUI as well as the processing sketch.
     */
    public DisplayFrame() {
        this.setSize(1600, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        button = new JButton("Start");
        fncButton2 = new JButton("Pixelate");
        fncButton3 = new JButton("Show dots");
        fileChooseButton = new JButton("Velg fil");
        clearButton = new JButton("Clear Canvas");
        doubleSpeed = new JButton("2X Speed");
        tripleSpeed = new JButton("3X Speed");
        imageProcessor = new ProcessImage();
        
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

        
        this.setVisible(true);
    }
    
    /**
     * Arranges the layout of the panel and buttons.
     */
    private void arrangeLayout() {
        setLayout(null);
        
        panel = new JPanel();
        
        panel.setBounds(20, 20, 1280, 720);
        fileChooseButton.setBounds(1320, 65, 215, 50);
        button.setBounds(1320, 13065, 100, 50);
        fncButton2.setBounds(1320, 195, 100, 50);
        fncButton3.setBounds(1435, 195, 100, 50);
        clearButton.setBounds(1320, 340, 215, 50);
        doubleSpeed.setBounds(1320, 410, 100, 50);
        tripleSpeed.setBounds(1435, 410, 100, 50);
        slider.setBounds(1320, 510, 215, 20);
        
        step1.setBounds(1320, 25, 150, 30);
        step2.setBounds(1320, 155, 150, 30);
        step3.setBounds(1320, 300, 150, 30);
        sliderLabel.setBounds(1360, 480, 150, 30);
    }
    
    /**
     * Adds action listener to all relevant components
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
            if(returnVal == JFileChooser.APPROVE_OPTION) {
//                imageProcessor.setCurrentImage(chooser.getSelectedFile().getAbsolutePath());
                s.loadBgImage(chooser.getSelectedFile());
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
