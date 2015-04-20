package processing_test;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author nikla_000
 */
public class ToolWindow extends JFrame {

    private HashMap<String, Component> components;
    private HashMap<JPanel, Layer> layerList;
    private ImageIcon[] functionIcons;
    private String[] functionNames = {"Original", "Dots", "Squares", "3D"};
    private JPanel layerPanel;
    private Box layerDisplay;

    /**
     * Constructor for the tool window
     */
    public ToolWindow() {
        setSize(250, 850);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        components = new HashMap<>();
        layerList = new HashMap<>();
        layerPanel = new JPanel();
        layerDisplay = Box.createVerticalBox();
        layerPanel.add(layerDisplay);
//        addLayerView(new Layer());

        makeComboBox();
        components.put("sizeSlider", new JSlider(JSlider.HORIZONTAL, 4, 30, 20));
        components.put("clearButton", new JButton("Clear"));
        components.put("cloneButton", new JButton("Clone"));

//        setLayout(new FlowLayout());
        Set<String> keys = components.keySet();
        for (String key : keys) {
            add(components.get(key));
        }

        arrangeLayout();

        setVisible(true);
    }

    public void addLayerView(Layer layer) {

        ImageIcon icon = layer.getImageIcon();
        Box hBox = Box.createHorizontalBox();

        hBox.add(new JLabel(icon));
        hBox.add(layer.getCheckBox());
        hBox.add(new JLabel("New Layer"));
        hBox.add(Box.createVerticalStrut(100));
//        hBox.add(new JLabel("Afoisna"));
        // Add Layer name/number

        JPanel newPanel = new JPanel();
        newPanel.add(hBox);

        newPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int count = mouseEvent.getClickCount();
                if (count == 1 && mouseEvent.getSource() instanceof JPanel) {
                    JPanel panel = (JPanel) mouseEvent.getSource();
                    setSelected(panel);
                }
            }
        });

        layerList.put(newPanel, layer);
        setSelected(newPanel);
    }

    private void refresh() {

        layerDisplay.removeAll();
        
        Set<JPanel> keys = layerList.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            JPanel panel = (JPanel) it.next();
            Layer layer = layerList.get(panel);
            
            if(layer.remove())
                it.remove();
            
            layerDisplay.add(panel, BorderLayout.WEST);
            if (layer.selected()) {
                panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            } else {
                panel.setBorder(BorderFactory.createEmptyBorder());
            }
        }

        revalidate();
    }

    private void setSelected(JPanel selectedPanel) {

        layerList.get(selectedPanel).selected(true);
        Set<JPanel> keys = layerList.keySet();
        for (JPanel p : keys) {
            if (p != selectedPanel) {
                layerList.get(p).selected(false);
            }
        }
        refresh();

    }

    /**
     * Arranges the layout of the components in the tool window.
     */
    private void arrangeLayout() {
        setLayout(null);

        add(new JLabel("Velg funksjon:")).setBounds(15, 5, 200, 10);
        components.get("functionComboBox").setBounds(20, 20, 180, 100);

        components.get("cloneButton").setBounds(10, 140, 200, 50);
        components.get("clearButton").setBounds(10, 210, 200, 50);

        add(new JLabel("Velg størrelse:")).setBounds(10, 305, 200, 10);
        components.get("sizeSlider").setBounds(5, 315, 200, 20);

        add(new JLabel("Layers: ")).setBounds(10, 365, 200, 15);
        add(layerPanel).setBounds(10, 375, 250, 475);
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
