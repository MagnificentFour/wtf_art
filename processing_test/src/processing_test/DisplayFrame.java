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
 * The visual in the main view window
 *
 * @author nikla_000
 */
public class DisplayFrame extends JFrame implements ActionListener {

    private final JButton fileChooseButton;
    private final JButton clearButton;
    private final JButton saveButton;
    private final JButton backButton;
    private final JButton forwardButton;
    private final JButton cloneButton;
    private final JButton setPoints;
    private final JButton blurButton;
    private final JButton invertButton;
    private final JButton newTab;
    private final JButton closeTab;
    private final JButton randomShit;
    private final JButton wrappingButton;
    private final JButton squareButton;
    private final JButton ellipseButton;
    private final JButton hazeButton;
    private final JComboBox functionChooser;
    private final JTabbedPane sketchTabs;

    private String[] functionNames = {"Original", "Dots", "Squares", "3D", "Flop", "Clone"};

    ColorChooserDemo ccd;
    private JColorChooser tcc;
    private final JSlider slider;
    private final JSlider cloneRadiusSlider;
    //colorPicker cp;
    private int tabIndex;
    private int tabs = 2;
    private ArrayList<Component> componentList;
    private HashMap<String, Component> toolWindowComponents;

    private ToolWindow tw;
    private SampleSketch currentSketch;

    /**
     * Constructor for instances of class DisplayFrame. Initializes all
     * components of the GUI as well as the processing sketch.
     */
    public DisplayFrame() throws IOException {
        this.setSize(1340, 890);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /**
         * Sets location of main window in relation to toolwindow.
         * Recommended that first parameter is 290 larger than x-pos for toolwindow
         */
        setLocation(350, 100);

        //cp = new colorPicker();
        ccd = new ColorChooserDemo();
        tcc = ccd.getTCC();
        //setLocationByPlatform(true);

        componentList = new ArrayList<>();
        sketchTabs = new JTabbedPane();

        closeTab = new JButton(new ImageIcon(ImageIO.read(new File("graphics/X.gif"))));
        fileChooseButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/OpenButton.gif"))));
        saveButton = new JButton(new ImageIcon(ImageIO.read(new File("graphics/Save-icon.png"))));
        newTab = new JButton(new ImageIcon(ImageIO.read(new File("graphics/blank.jpg"))));
        backButton = new JButton("<");
        forwardButton = new JButton(">");
        tw = new ToolWindow();
        randomShit = new JButton("Surprise!");

        cloneRadiusSlider = new JSlider(JSlider.HORIZONTAL, 1, 50, 25);

        arrangeLayout();
        setHoverText();

        sketchTabs.addTab("Sketch 1", createNewSketch());
        add(fileChooseButton);
        add(saveButton);
        add(backButton);
        add(forwardButton);
        add(sketchTabs);
        add(newTab);
        add(closeTab);
        add(randomShit);
        add(ccd);

        toolWindowComponents = tw.getToolComponents();

        componentList.add(backButton);
        componentList.add(forwardButton);
        componentList.add(fileChooseButton);
        componentList.add(saveButton);
        componentList.add(randomShit);
        componentList.add(tcc);

        Set<String> keys = toolWindowComponents.keySet();
        for (String key : keys) {
            componentList.add(toolWindowComponents.get(key));
        }

        clearButton = (JButton) toolWindowComponents.get("clearButton");
        cloneButton = (JButton) toolWindowComponents.get("cloneButton");
        blurButton = (JButton) toolWindowComponents.get("blurButton");
        invertButton = (JButton) toolWindowComponents.get("invertButton");
        wrappingButton = (JButton) toolWindowComponents.get("wrappingButton");
        setPoints = (JButton) toolWindowComponents.get("setPointsButton");
        slider = (JSlider) toolWindowComponents.get("sizeSlider");
        squareButton = (JButton) toolWindowComponents.get("squareButton");
        ellipseButton = (JButton) toolWindowComponents.get("ellipseButton");
        functionChooser = (JComboBox) toolWindowComponents.get("functionComboBox");
        hazeButton = (JButton) toolWindowComponents.get("hazeButton");

        tabIndex = sketchTabs.getSelectedIndex();
        currentSketch = (SampleSketch) sketchTabs.getSelectedComponent();
        setActionListeners(currentSketch);
        setLocalActionListeners();

        sketchTabs.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                if (tabIndex != sketchTabs.getSelectedIndex()) {
                    currentSketch.noLoop();
                    removeOldActionListeners(currentSketch);
                    SampleSketch newSketch = (SampleSketch) sketchTabs.getSelectedComponent();
                    setActionListeners(newSketch);
                    currentSketch = newSketch;
                    tabIndex = sketchTabs.getSelectedIndex();
                    currentSketch.loop();
                    currentSketch.getLayers();

                }
            }
        });
        setVisible(true);
        tw.setVisible(true);
    }

    /**
     * Creating a new tab
     */
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
        newSketch.setToolWindow(tw);
        newSketch.setColorPicker(ccd);
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

        newSketch.setToolWindow(tw);
        newSketch.init();

        return newSketch;

    }

    /**
     * Arranges the layout of the panel and buttons.
     */
    private void arrangeLayout() {
        setLayout(null);

        //Position and size for buttons.
        closeTab.setBounds(1267, 10, 30, 30);
        newTab.setBounds(20, 10, 50, 50);
        fileChooseButton.setBounds(80, 10, 50, 50);
        saveButton.setBounds(140, 10, 50, 50);
        backButton.setBounds(220, 10, 50, 50);
        forwardButton.setBounds(280, 10, 50, 50);
        sketchTabs.setBounds(20, 110, 1282, 722);
        cloneRadiusSlider.setBounds(720, 30, 215, 20);
        randomShit.setBounds(927, 10, 90, 30);
        ccd.setBounds(407, -173, 500, 500);
        ccd.setBounds(407, -163, 500, 500);
    }

    /**
     * Sets the hovertext for main window buttons
     * Gives a description of buttonfunctions
     */
    private void setHoverText() {
        closeTab.setToolTipText("Close current tab");
        newTab.setToolTipText("Create new tab");
        fileChooseButton.setToolTipText("Open a file");
        saveButton.setToolTipText("Save current work");
        forwardButton.setToolTipText("Redo");
        backButton.setToolTipText("Undo");

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

    /**
     * Adds actionlisteners to buttons that shall be relevant for all tabs
     */
    private void setLocalActionListeners() {

        newTab.addActionListener(this);
        newTab.setActionCommand("newTab");

        closeTab.addActionListener(this);
        closeTab.setActionCommand("closeTab");

    }

    /**
     * Adds action listener to all relevant components, in the current tab
     *
     * @param s The processing sketch where buttons execute the listener.
     */
    private void setActionListeners(SampleSketch s) {
        randomShit.addActionListener(s);
        randomShit.setActionCommand("randomFucks");

        clearButton.addActionListener(s);
        clearButton.setActionCommand("clear");

        backButton.addActionListener(s);
        backButton.setActionCommand("back");

        forwardButton.addActionListener(s);
        forwardButton.setActionCommand("forward");

        cloneButton.addActionListener(s);
        cloneButton.setActionCommand("clone");

        blurButton.addActionListener(s);
        blurButton.setActionCommand("blur");

        setPoints.addActionListener(s);
        setPoints.setActionCommand("setPoints");

        invertButton.addActionListener(s);
        invertButton.setActionCommand("invert");

        wrappingButton.addActionListener(s);
        wrappingButton.setActionCommand("wrapping");

        squareButton.addActionListener(s);
        squareButton.setActionCommand("square");

        ellipseButton.addActionListener(s);
        ellipseButton.setActionCommand("ellipse");

        hazeButton.addActionListener(s);
        hazeButton.setActionCommand("haze");

        slider.addChangeListener(s);

        
        tcc.getSelectionModel().addChangeListener(s);
        
        fileChooseButton.addActionListener((
                        ActionEvent arg0
                ) ->

                {
                    JFileChooser chooser = new JFileChooser();

                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "JPG, GIF & PNG", "jpg", "gif", "png");
                    chooser.setFileFilter(filter);

                    int returnVal = chooser.showOpenDialog(sketchTabs);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        s.loadBgImage(chooser.getSelectedFile());
                    }
                }

        );

        saveButton.addActionListener((
                        ActionEvent arg0
                ) ->

                {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG: png", "png"));
                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG: jpg & jpeg", "jpg", "jpeg"));
                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("TIF: tif", "tif"));

                    int returnVal = chooser.showSaveDialog(sketchTabs);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        s.saveImage(chooser.getSelectedFile());
                    }
                }

        );

        functionChooser.addItemListener(new ItemListener() {

                                            @Override
                                            public void itemStateChanged(ItemEvent e) {

                                                if (e.getStateChange() == ItemEvent.SELECTED) {

                                                    s.selectFunction(functionNames[(int) e.getItem()]);

                                                }

                                            }

                                        }

        );

    }

    /**
     * @return dialogResult
     * @deprecated Not in use after changed implementations of newtab button. Tabs are no longer closed when a new document is created.
     * <p>
     * Creates a dialog asking the user whether or not they want to reset.
     */
    private boolean wantToReset() {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, "All unsaved progress will be lost, "
                + "are you sure you want to do this?", "Warning", dialogButton);
        return dialogResult == JOptionPane.YES_OPTION;
    }


    /**
     * Actionlistener for creating new tabs and closing tabs.
     *
     * @param e Actionevent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "newTab":
                newTab();
                break;
            case "closeTab":
                sketchTabs.remove(sketchTabs.getSelectedIndex());
                tabs--;
                System.out.println(tabs);
                if (sketchTabs.getTabCount() < 1) {
                    newTab();
                }
        }
    }

}
