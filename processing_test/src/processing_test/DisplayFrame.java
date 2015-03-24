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
import javax.imageio.ImageIO;
import javax.swing.*;
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
    private final JButton clearButton;
    private final JButton doubleSpeed;
    private final JButton tripleSpeed;
    private final JButton saveButton;
    private final JButton blankButton;
    private final JButton backButton;
    private final JButton forwardButton;
    private final JButton cloneButton;
    private final JButton setPoints;
    private final JComboBox functionChooser;
    private final JLabel step1;
    private final JLabel step2;
    private final JLabel step3;
    private final JLabel sliderLabel;

    private ImageIcon[] functionIcons;
    private String[] functionNames = {"Original", "Dots", "Squares"};

    private final ProcessImage imageProcessor;
    private JPanel panel;
    private final JSlider slider;

    SampleSketch s;

    /**
     * Constructor for instances of class DisplayFrame. Initializes all
     * components of the GUI as well as the processing sketch.
     */
    public DisplayFrame() throws IOException {
        this.setSize(1600, 850);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        button = new JButton("Start");
        fncButton2 = new JButton("Pixelate");
        fncButton3 = new JButton("Show dots");
        clearButton = new JButton("Clear Canvas");
        doubleSpeed = new JButton("2X Speed");
        tripleSpeed = new JButton("3X Speed");
        cloneButton = new JButton("Clone");
        setPoints = new JButton("Set Points");
        imageProcessor = new ProcessImage();

        functionIcons = new ImageIcon[functionNames.length];
        Integer[] intArray = new Integer[functionNames.length];

        fileChooseButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/OpenButton.gif"))));
        saveButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/Save-icon.png"))));
        blankButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/blank.jpg"))));
        backButton = new JButton("<");
        forwardButton = new JButton(">");

        step1 = new JLabel("Step1: Choose a picture");
        step2 = new JLabel("Step2: Choose a function");
        step3 = new JLabel("Step3: Edit the result");
        sliderLabel = new JLabel("Change size of \"pixels\"");

        slider = new JSlider(JSlider.HORIZONTAL, 4, 30, 20);

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

        arrangeLayout();

        s = new SampleSketch();
        s.setButtons(forwardButton, backButton);

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
        this.add(blankButton);
        this.add(backButton);
        this.add(forwardButton);
        this.add(cloneButton);
        this.add(setPoints);
        add(functionChooser);

        this.setVisible(true);
    }

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

        panel = new JPanel();

        //Position and size for buttons.
        blankButton.setBounds(20, 10, 50, 50);
        saveButton.setBounds(80, 10, 50, 50);
        fileChooseButton.setBounds(140, 10, 50, 50);
        backButton.setBounds(220, 10, 50, 50);
        forwardButton.setBounds(280, 10, 50, 50);
        cloneButton.setBounds(340, 10, 100, 50);
        setPoints.setBounds(450, 10, 100, 50);
        fncButton2.setBounds(1320, 275, 100, 50);
        fncButton3.setBounds(1435, 275, 100, 50);
        panel.setBounds(20, 70, 1280, 720);
        button.setBounds(1320, 1445, 100, 50);
        clearButton.setBounds(1320, 420, 215, 50);
        doubleSpeed.setBounds(1320, 490, 100, 50);
        tripleSpeed.setBounds(1435, 490, 100, 50);
        slider.setBounds(1320, 590, 215, 20);
        functionChooser.setBounds(1280, 10, 300, 120);

        //Position and size for labels
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

        new FileDrop(s, new FileDrop.Listener() {

            @Override
            public void filesDropped(File[] files) {

                String fileName = files[0].getAbsolutePath();

                if (fileName.endsWith("jpg") || fileName.endsWith("png") || fileName.endsWith("gif")) {

                    s.loadBgImage(files[0]);

                } else {

                    JOptionPane.showMessageDialog(s, "This it not a picture. Please drop an image with jpg, png or gif format.");

                }

            }

        });
        
        functionChooser.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    
                    s.selectFunction(functionNames[(int) e.getItem()]);
                    
                }
                
            }
        
        });
        
        
    }

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
                    s.reset();
                }
        }
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
