package processing_test;

import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

/**
 * The visual in the toolwindow
 *
 * @author nikla_000
 */
public class ToolWindow extends JFrame {

    private HashMap<String, Component> components;
    private ImageIcon[] functionIcons;
    private ColorChooserDemo cs;
    private String[] functionNames = {"Original", "Dots", "Squares", "3D"};
    private JScrollPane scrollPane;

    /**
     * Constructor for the tool window
     */
    public ToolWindow() throws IOException {
        setSize(280, 890);

        /**
         * Sets location of main window in relation to toolwindow.
         */
        setLocation(60, 100);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setAlwaysOnTop(false);

        components = new HashMap<>();
        scrollPane = new JScrollPane();

        makeComboBox();

        components.put("sizeSlider", new JSlider(JSlider.HORIZONTAL, 4, 30, 20));
        components.put("clearButton", new JButton("Clear"));
        components.put("cloneButton", new JButton("Clone"));
        components.put("blurButton", new JButton("Blur"));
        components.put("invertButton", new JButton("Invert Colors"));
        components.put("setPointsButton", new JButton("Set new points"));
        components.put("wrappingButton", new JButton("Wrapping"));
        components.put("squareButton", new JButton(new ImageIcon(ImageIO.read(new File("graphics/square.png")))));
        components.put("ellipseButton", new JButton(new ImageIcon(ImageIO.read(new File("graphics/ellipse.png")))));
        components.put("hazeButton", new JButton(new ImageIcon(ImageIO.read(new File("graphics/haze.png")))));

        Set<String> keys = components.keySet();
        for (String key : keys) {
            add(components.get(key));
        }

        arrangeLayout();
        setVisible(true);
    }
    
    /**
     * Removes a layer view and replaces it. Used when switching between sketches using
     * tabs.
     * @param layerView Sketch-specific layer view.
     */
    public void refreshLayerView(LayerView layerView) {
        
        remove(scrollPane);
        revalidate();
        
        scrollPane = new JScrollPane(layerView);
        add(scrollPane).setBounds(10, 465, 250, 380);
        revalidate();
        
    }

    

    /**
     * Arranges the layout of the components in the tool window.
     */
    private void arrangeLayout() {
        setLayout(null);

        //Function chooser.
        add(new JLabel("Velg funksjon:")).setBounds(10, 10, 220, 15);
        components.get("functionComboBox").setBounds(10, 30, 240, 100);

        //First row of buttons
        components.get("cloneButton").setBounds(10, 140, 120, 50);
        components.get("setPointsButton").setBounds(130, 140, 120, 50);

        //Second row
        components.get("blurButton").setBounds(10, 200, 120, 50);
        components.get("invertButton").setBounds(130, 200, 120, 50);


        //Third row
        components.get("wrappingButton").setBounds(10, 260, 120, 50);
        components.get("clearButton").setBounds(130, 260, 120, 50);

        //Fourth row slider+label
        add(new JLabel("Velg størrelse:")).setBounds(35, 320, 200, 15);
        components.get("sizeSlider").setBounds(30, 335, 200, 20);

        //Fifth row three somethingbuttons
        components.get("squareButton").setBounds(50, 375, 50, 50);
        components.get("ellipseButton").setBounds(110, 375, 50, 50);
        components.get("hazeButton").setBounds(170, 375, 50, 50);

        //Layerpanel takes rest of window
        add(new JLabel("Layers: ")).setBounds(10, 445, 200, 15);
        add(scrollPane).setBounds(10, 465, 250, 380);
    }

    /**
     * Returns a hash map of the components for setting listeners.
     *
     * @return HashMap of components in this window.
     */
    public HashMap<String, Component> getToolComponents() {
        return components;
    }

    /**
     * Creates a combo box.
     */
    private void makeComboBox() {

        functionIcons = new ImageIcon[functionNames.length];
        Integer[] intArray = new Integer[functionNames.length];

        for (int i = 0;
             i < functionNames.length;
             i++) {

            intArray[i] = i;
            functionIcons[i] = createImageIcon("graphics/" + functionNames[i] + ".png");
            if (functionIcons[i] != null) {

                functionIcons[i].setDescription(functionNames[i]);

            }

        }

        JComboBox comboBox = new JComboBox(intArray);
        ComboBoxRenderer rend = new ComboBoxRenderer();
        rend.setPreferredSize(new Dimension(150, 90));
        comboBox.setRenderer(rend);
        comboBox.setMaximumRowCount(3);
        components.put("functionComboBox", comboBox);

    }

    /**
     * Creates an ImageIcon
     *
     * @param path
     * @return icon
     */
    protected static ImageIcon createImageIcon(String path) {
        ImageIcon icon = null;

        try {
            icon = new ImageIcon(ImageIO.read(new File(path)));
        } catch (IOException ex) {

        }

        return icon;
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

    /**
     * Colorgetter
     *
     * @return color
     */
    public Color getColor() {
        return cs.getColor();
    }
}
