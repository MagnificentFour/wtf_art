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
    private final JButton button;
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
    private final JComboBox functionChooser;
    private final JTabbedPane sketchTabs;
    private final JLabel step2;
    private final JLabel step3;
    private final JLabel sliderLabel;

    private ImageIcon[] functionIcons;
    private String[] functionNames = {"Original", "Dots", "Squares"};

    private final JSlider slider;
    private final JSlider cloneRadiusSlider;


    private int tabIndex;

    private ArrayList<Component> componentList;

    private SampleSketch currentSketch;


    /**
     * Constructor for instances of class DisplayFrame. Initializes all
     * components of the GUI as well as the processing sketch.
     */
    public DisplayFrame() throws IOException {
        this.setSize(1670, 850);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        componentList = new ArrayList<>();
        button = new JButton("Start");
        fncButton2 = new JButton("Pixelate");
        fncButton3 = new JButton("Show dots");
        fncButton4 = new JButton("MapTo3D");
        clearButton = new JButton("Clear Canvas");
        doubleSpeed = new JButton("2X Speed");
        tripleSpeed = new JButton("3X Speed");
        cloneButton = new JButton("Clone");
        setPoints = new JButton("Set Points");
        newTab = new JButton("New Tab");
        sketchTabs = new JTabbedPane();

        functionIcons = new ImageIcon[functionNames.length];
        Integer[] intArray = new Integer[functionNames.length];

        fileChooseButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/OpenButton.gif"))));
        saveButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/Save-icon.png"))));
        blankButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/blank.jpg"))));
        backButton = new JButton("<");
        forwardButton = new JButton(">");

        step2 = new JLabel("Step2: Choose a function");
        step3 = new JLabel("Step3: Edit the result");
        sliderLabel = new JLabel("Change size of \"pixels\"");

        slider = new JSlider(JSlider.HORIZONTAL, 4, 30, 20);
        cloneRadiusSlider = new JSlider(JSlider.HORIZONTAL, 1, 50, 25);
        fncButton3.setToolTipText("Show a dot representation for your picture");

        for (int i = 0; i < functionNames.length; i++) {

            intArray[i] = i;
            functionIcons[i] = createImageIcon("graphics/" + functionNames[i] + ".png");
            if (functionIcons[i] != null) {


                functionIcons[i].setDescription(functionNames[i]);

            }

        }

        functionChooser = new JComboBox(intArray);
        ComboBoxRenderer rend = new ComboBoxRenderer();
        rend.setPreferredSize(new Dimension(150, 90));
        functionChooser.setRenderer(rend);
        functionChooser.setMaximumRowCount(3);

        componentList.add(newTab);
        componentList.add(clearButton);
        componentList.add(backButton);
        componentList.add(forwardButton);
        componentList.add(blankButton);
        componentList.add(cloneButton);
        componentList.add(setPoints);
        componentList.add(slider);
        componentList.add(fileChooseButton);
        componentList.add(saveButton);
        componentList.add(functionChooser);

        arrangeLayout();

        sketchTabs.addTab("Sketch 1", createNewSketch());
        this.add(fileChooseButton);
        this.add(clearButton);
        this.add(step2);
        this.add(step3);
        this.add(sliderLabel);
        this.add(slider);
        this.add(cloneRadiusSlider);
        this.add(saveButton);
        this.add(blankButton);
        this.add(backButton);
        this.add(forwardButton);
        this.add(cloneButton);
        this.add(setPoints);
        add(functionChooser);
        add(sketchTabs);
        add(newTab);

        tabIndex = sketchTabs.getSelectedIndex();
        currentSketch = (SampleSketch) sketchTabs.getSelectedComponent();
        setActionListeners(currentSketch);

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

        this.setVisible(true);
    }

    /**
     * Creates and instance of the processing sketch class SampleSketch.
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
     * Creates an ImageIcon
     * @param path
     * @return 
     */
    protected static ImageIcon createImageIcon(String path) {
        ImageIcon icon = null;

        try {
            icon = new ImageIcon(ImageIO.read(new File(path)));
        } catch (IOException ex) {

        }

        return icon;
    }

    /**
     * Arranges the layout of the panel and buttons.
     */
    private void arrangeLayout() {
        setLayout(null);

        //Position and size for buttons.
        newTab.setBounds(600, 10, 100, 50);
        blankButton.setBounds(20, 10, 50, 50);
        saveButton.setBounds(80, 10, 50, 50);
        fileChooseButton.setBounds(140, 10, 50, 50);
        backButton.setBounds(220, 10, 50, 50);
        forwardButton.setBounds(280, 10, 50, 50);
        cloneButton.setBounds(340, 10, 100, 50);
        setPoints.setBounds(450, 10, 100, 50);
        fncButton2.setBounds(1320, 275, 100, 50);
        fncButton3.setBounds(1435, 275, 100, 50);
        fncButton4.setBounds(1320, 330, 100, 50);
        sketchTabs.setBounds(20, 70, 1282, 722);
        button.setBounds(1320, 1445, 100, 50);
        clearButton.setBounds(1320, 420, 215, 50);
        doubleSpeed.setBounds(1320, 490, 100, 50);
        tripleSpeed.setBounds(1435, 490, 100, 50);
        slider.setBounds(1320, 590, 215, 20);
        cloneRadiusSlider.setBounds(490, 10, 215, 20);
        


        functionChooser.setBounds(1320, 10, 300, 120);

        //Position and size for labels

        step2.setBounds(1320, 235, 150, 30);
        step3.setBounds(1320, 380, 150, 30);
        sliderLabel.setBounds(1360, 560, 150, 30);

        //saveButton.setBorder(BorderFactory.createEmptyBorder());
        //saveButton.setContentAreaFilled(false);
    }

    /**
     * Removes action listeners for components.
     * @param removeFrom A pointer to the instance of the sketch where the listeners will be removed from
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
                
                for(ChangeListener ch : s.getChangeListeners()) {
                    s.removeChangeListener(ch);
                }
            } else if (c instanceof JComboBox) {
                JComboBox cb = (JComboBox) c;
                
                for(ItemListener il : cb.getItemListeners()) {
                    cb.removeItemListener(il);
                }
            }

        }

    }

    /**
     * Adds action listener to all relevant components
     *
     * @param s The processing sketch where buttons execute the listener.
     */
    private void setActionListeners(SampleSketch s) {

        newTab.addActionListener(this);
        newTab.setActionCommand("newTab");

        clearButton.addActionListener(s);
        clearButton.setActionCommand("clear");

        backButton.addActionListener(s);
        backButton.setActionCommand("back");

        forwardButton.addActionListener(s);
        forwardButton.setActionCommand("forward");

        blankButton.addActionListener(this);
        blankButton.setActionCommand("blank");

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
                SampleSketch newSketch = new SampleSketch();
                sketchTabs.addTab("New sketch", newSketch);
                newSketch.setButtons(forwardButton, backButton);
                newSketch.init();
                sketchTabs.setSelectedIndex(sketchTabs.getTabCount() - 1);
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
    
    
    class ComboBoxRenderer extends JLabel
            implements ListCellRenderer {

        private Font uhOhFont;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the image and text corresponding
         * to the selected value and returns the label, set up
         * to display the text and image.
         */
        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            //Get the selected index. (The index param isn't
            //always valid, so just use the value.)
            int selectedIndex = ((Integer) value).intValue();

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            //Set the icon and text.  If icon was null, say so.
            ImageIcon icon = functionIcons[selectedIndex];
            String function = functionNames[selectedIndex];
            setIcon(icon);
            if (icon != null) {
                setText(function);
                setFont(list.getFont());
            } else {
                setUhOhText(function + " (no image available)",
                        list.getFont());
            }

            return this;
        }

        //Set the font and text when no image was found.
        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) { //lazily create this font
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
    }
}
