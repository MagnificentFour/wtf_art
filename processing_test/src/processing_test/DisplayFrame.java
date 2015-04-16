/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.iharder.dnd.FileDrop;

/**
 * @author nikla_000
 */
public class DisplayFrame extends JFrame implements ActionListener {

    //private final CircleSketch sketch;
    private final JButton fileChooseButton;
    //private final JButton button;
    private final JButton fncButton2;
    private final JButton fncButton3;
    private final JButton fncButton4;
    private final JButton clearButton;
    private final JButton doubleSpeed;
    private final JButton tripleSpeed;
    private final JButton saveButton;
    private final JButton blankButton;
    private final JButton backButton;
    private final JButton forwardButton;
    private final JButton cloneButton;
    private final JButton setPoints;
    private final JButton newTab;
    private final JButton closeTab;
    private final JComboBox functionChooser;
    private final JTabbedPane sketchTabs;
//    private final JLabel sliderLabel;

    private String[] functionNames = {"Original", "Dots", "Squares", "3D"};

    private final JSlider slider;
    private final JSlider cloneRadiusSlider;

    private int tabIndex;
    private int tabs = 2;

    private ArrayList<Component> componentList;
    private HashMap<String, Component> toolWindowComponents;

    private SampleSketch currentSketch;

    /**
     * Constructor for instances of class DisplayFrame. Initializes all
     * components of the GUI as well as the processing sketch.
     */
    public DisplayFrame() throws IOException {
        this.setSize(1350, 850);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        componentList = new ArrayList<>();
        //button = new JButton("Start");
        fncButton2 = new JButton("Pixelate");
        fncButton3 = new JButton("Show dots");
        fncButton4 = new JButton("MapTo3D");
        doubleSpeed = new JButton("2X Speed");
        tripleSpeed = new JButton("3X Speed");
        setPoints = new JButton("Set Points");
        newTab = new JButton("New Tab");
        closeTab = new JButton("x");
        sketchTabs = new JTabbedPane();

        fileChooseButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/OpenButton.gif"))));
        saveButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/Save-icon.png"))));
        blankButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/blank.jpg"))));
        backButton = new JButton("<");
        forwardButton = new JButton(">");

        cloneRadiusSlider = new JSlider(JSlider.HORIZONTAL, 1, 50, 25);
        fncButton3.setToolTipText("Show a dot representation for your picture");


        arrangeLayout();

        sketchTabs.addTab("Sketch 1", createNewSketch());
        add(fileChooseButton);
        add(cloneRadiusSlider);
        add(saveButton);
        add(blankButton);
        add(backButton);
        add(forwardButton);
        add(setPoints);
        add(sketchTabs);
        add(newTab);
        add(closeTab);

        ToolWindow tw = new ToolWindow(currentSketch);
        toolWindowComponents = tw.getToolComponents();
        
        componentList.add(backButton);
        componentList.add(forwardButton);
        componentList.add(blankButton);
        componentList.add(setPoints);
        componentList.add(fileChooseButton);
        componentList.add(saveButton);
        
        Set<String> keys = toolWindowComponents.keySet();
        for(String key : keys) {
            componentList.add(toolWindowComponents.get(key));
        }

        clearButton = (JButton) toolWindowComponents.get("clearButton");
        cloneButton = (JButton) toolWindowComponents.get("cloneButton");
        slider = (JSlider) toolWindowComponents.get("sizeSlider");
        functionChooser = (JComboBox) toolWindowComponents.get("functionComboBox");
        
        tabIndex = sketchTabs.getSelectedIndex();
        currentSketch = (SampleSketch) sketchTabs.getSelectedComponent();
        setActionListeners(currentSketch);
        setLocalActionListeners();

        sketchTabs.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                if (tabIndex != sketchTabs.getSelectedIndex()) {

                    removeOldActionListeners(currentSketch);
                    SampleSketch newSketch = (SampleSketch) sketchTabs.getSelectedComponent();
                    setActionListeners(newSketch);
                    currentSketch = newSketch;
                    tabIndex = sketchTabs.getSelectedIndex();

                }

            }

        });

        setVisible(true);
    }

    private void newTab() {

        sketchTabs.addTab("Sketch " + tabs, createNewSketch());
        sketchTabs.setSelectedIndex(sketchTabs.getTabCount() - 1);
        tabs++;

    }

    /**
     * Creates and instance of the processing sketch class SampleSketch.
     *
     * @return The created sketch.
     */
    private SampleSketch createNewSketch() {

        SampleSketch newSketch = new SampleSketch();
        newSketch.setButtons(forwardButton, backButton);

        new FileDrop(newSketch, new FileDrop.Listener() {

            @Override
            public void filesDropped(File[] files) {

                String fileName = files[0].getAbsolutePath();

                if (fileName.endsWith("jpg") || fileName.endsWith("png") || fileName.endsWith("gif")) {

                    newSketch.loadBgImage(files[0]);

                } else {

                    JOptionPane.showMessageDialog(newSketch, "This it not a picture. Please drop an image with jpg, png or gif format.");

                }

            }

        });

        newSketch.init();

        return newSketch;

    }

    /**
     * Arranges the layout of the panel and buttons.
     */
    private void arrangeLayout() {
        setLayout(null);

        //Position and size for buttons.
        closeTab.setBounds(1267, 50, 35, 35);
        newTab.setBounds(600, 10, 100, 50);
        blankButton.setBounds(20, 10, 50, 50);
        saveButton.setBounds(80, 10, 50, 50);
        fileChooseButton.setBounds(140, 10, 50, 50);
        backButton.setBounds(220, 10, 50, 50);
        forwardButton.setBounds(280, 10, 50, 50);
        setPoints.setBounds(450, 10, 100, 50);
        fncButton2.setBounds(1320, 275, 100, 50);
        fncButton3.setBounds(1435, 275, 100, 50);
        fncButton4.setBounds(1320, 330, 100, 50);
        sketchTabs.setBounds(20, 70, 1282, 722);
        doubleSpeed.setBounds(1320, 490, 100, 50);
        tripleSpeed.setBounds(1435, 490, 100, 50);
        cloneRadiusSlider.setBounds(490, 10, 215, 20);

        //saveButton.setBorder(BorderFactory.createEmptyBorder());
        //saveButton.setContentAreaFilled(false);
    }

    /**
     * Removes action listeners for components.
     *
     * @param removeFrom A pointer to the instance of the sketch where the
     *                   listeners will be removed from
     */
    private void removeOldActionListeners(SampleSketch removeFrom) {

        for (Component c : componentList) {

            if (c instanceof JButton) {
                JButton b = (JButton) c;

                for (ActionListener a : b.getActionListeners()) {
                    b.removeActionListener(a);
                }
            } else if (c instanceof JSlider) {
                JSlider s = (JSlider) c;

                for (ChangeListener ch : s.getChangeListeners()) {
                    s.removeChangeListener(ch);
                }
            } else if (c instanceof JComboBox) {
                JComboBox cb = (JComboBox) c;

                for (ItemListener il : cb.getItemListeners()) {
                    cb.removeItemListener(il);
                }
            }

        }

    }

    private void setLocalActionListeners() {

        newTab.addActionListener(this);
        newTab.setActionCommand("newTab");

        blankButton.addActionListener(this);
        blankButton.setActionCommand("blank");

        closeTab.addActionListener(this);
        closeTab.setActionCommand("closeTab");

    }

    /**
     * Adds action listener to all relevant components
     *
     * @param s The processing sketch where buttons execute the listener.
     */
    private void setActionListeners(SampleSketch s) {


        clearButton.addActionListener(s);
        clearButton.setActionCommand("clear");

        backButton.addActionListener(s);
        backButton.setActionCommand("back");

        forwardButton.addActionListener(s);
        forwardButton.setActionCommand("forward");

        cloneButton.addActionListener(s);
        cloneButton.setActionCommand("clone");

        setPoints.addActionListener(s);
        setPoints.setActionCommand("setPoints");

        slider.addChangeListener(s);
        cloneRadiusSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                currentSketch.cloneRadChanged(cloneRadiusSlider.getValue());
            }

        });

        fileChooseButton.addActionListener((ActionEvent arg0) -> {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JPG, GIF & PNG", "jpg", "gif", "png");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showOpenDialog(sketchTabs);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                s.loadBgImage(chooser.getSelectedFile());
            }
        });

        saveButton.addActionListener((ActionEvent arg0) -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG: png", "png"));
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG: jpg & jpeg", "jpg", "jpeg"));
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("TIF: tif", "tif"));

            int returnVal = chooser.showOpenDialog(sketchTabs);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                s.saveImage(chooser.getSelectedFile());
            }
        });

        functionChooser.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    s.selectFunction(functionNames[(int) e.getItem()]);

                }

            }

        });

    }

    /**
     * Creates a dialog asking the user whether or not they want to reset.
     *
     * @return
     */
    private boolean wantToReset() {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, "All unsaved progress will be lost, "
                + "are you sure you want to do this?", "Warning", dialogButton);
        return dialogResult == JOptionPane.YES_OPTION;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "blank":
                if (wantToReset()) {
                    currentSketch.reset();
                }
                break;
            case "newTab":
                newTab();
                break;
            case "closeTab":
                sketchTabs.remove(sketchTabs.getSelectedIndex());
                if (sketchTabs.getTabCount() < 1) {
                    newTab();
                }
        }
    }

    //    public void chooseFile() {
//        JFileChooser chooser = new JFileChooser();
//        FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                "JPG, GIF & PNG", "jpg", "gif", "png");
//        chooser.setFileFilter(filter);
//        int returnVal = chooser.showOpenDialog(this);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            imageProcessor.setCurrentImage(chooser.getSelectedFile().getAbsolutePath());
//        }
//    }
}
