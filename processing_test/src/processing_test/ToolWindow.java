package processing_test;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author nikla_000
 */
public class ToolWindow extends JFrame {

    private HashMap<String, Component> components;
    private ImageIcon[] functionIcons;
    private String[] functionNames = {"Original", "Dots", "Squares", "3D"};


    /**
     * Constructor for the toolwindow
     */
    public ToolWindow() {
        setSize(250, 570);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        components = new HashMap<>();

        makeComboBox();
        components.put("sizeSlider", new JSlider(JSlider.HORIZONTAL, 4, 30, 20));
        components.put("clearButton", new JButton("Clear"));
        components.put("cloneButton", new JButton("Clone"));
        components.put("blurButton", new JButton("Blur"));
        components.put("invertButton", new JButton("Invert Colors"));
        components.put("setPointsButton", new JButton("Set new points"));

//        setLayout(new FlowLayout());

        Set<String> keys = components.keySet();
        for (String key : keys) {
            add(components.get(key));
        }
        
        arrangeLayout();
        
        setVisible(true);
    }
    
    /**
     * Arranges the layout of the components in the tool window.
     */
    private void arrangeLayout() {
        setLayout(null);
        
        add(new JLabel("Velg funksjon:")).setBounds(15, 5, 200, 10);
        components.get("functionComboBox").setBounds(20, 20, 180, 100);
        components.get("cloneButton").setBounds(10, 140, 200, 50);
        components.get("setPointsButton").setBounds(10, 200, 200, 50);
        components.get("clearButton").setBounds(10, 380, 200, 50);
        components.get("blurButton").setBounds(10, 260, 200, 50);
        components.get("invertButton").setBounds(10, 320, 200, 50);
        add(new JLabel("Velg størrelse:")).setBounds(10, 455, 200, 10);
        components.get("sizeSlider").setBounds(5, 475, 200, 20);
    }
    
    /**
     * Returns a hash map of the components for setting listeners.
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
