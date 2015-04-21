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
public class ToolWindow extends JFrame implements ActionListener {

    private HashMap<String, Component> components;
    private LinkedHashMap<LayerView, Layer> layerList;
    private ImageIcon[] functionIcons;
    private ColorChooserDemo cs;
    private String[] functionNames = {"Original", "Dots", "Squares", "3D"};
    private JPanel layerPanel;
    private Box layerDisplay;

    /**
     * Constructor for the tool window
     */
    public ToolWindow() throws IOException {
        setSize(240, 760);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setAlwaysOnTop (true);
        components = new HashMap<>();
        layerList = new LinkedHashMap<>();
        layerPanel = new JPanel();
        layerDisplay = Box.createVerticalBox();
        layerPanel.add(layerDisplay);
//        addLayerView(new Layer());

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

//        setLayout(new FlowLayout());
        Set<String> keys = components.keySet();
        for (String key : keys) {
            add(components.get(key));
        }

        arrangeLayout();

        setLocationByPlatform(true);

        setVisible(true);
    }

    /**
     * Adds a view of the newly added layer to the tool window.
     * @param layer The new layer.
     */
    public void addLayerView(Layer layer) {

        
        ImageIcon icon = layer.getImageIcon();
        JButton b = layer.getRemoveButton();
        int layerNum = layerList.size() + 1;
        layer.setLayerNum(layerNum);
        
        b.addActionListener(this);
        
        LayerView newLayerView = new LayerView(icon, b, layerNum, layer.getCheckBox());

        // Add a mouse listener to the panel view of the layer.
        newLayerView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int count = mouseEvent.getClickCount();
                if (count == 1 && mouseEvent.getSource() instanceof JPanel) {
                    LayerView panel = (LayerView) mouseEvent.getSource();
                    setSelected(panel);
                }
            }
        });

        layerList.put(newLayerView, layer);
        setSelected(newLayerView);
    }

    /**
     * Remove and repaint the view of layers.
     */
    public void refresh() {
        layerDisplay.removeAll();

        Set<LayerView> keys = layerList.keySet();

        for (LayerView panel : keys) {
            Layer layer = layerList.get(panel);

            if (layer.remove()) {
                layerDisplay.remove(panel);
            } else {
                panel.setLayerIcon(layer.getImageIcon());
                layerDisplay.add(panel, BorderLayout.WEST);
                if (layer.selected()) {
                    panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                } else {
                    panel.setBorder(BorderFactory.createEmptyBorder());
                }
            }
        }

        layerDisplay.revalidate();
        layerDisplay.repaint();
        validate();
        repaint();
    }
    
    /**
     * Set the selected layer.
     * @param selectedPanel Currently selected layer panel.
     */
    private void setSelected(LayerView selectedPanel) {

        layerList.get(selectedPanel).selected(true);
        Set<LayerView> keys = layerList.keySet();
        for (LayerView p : keys) {
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
        components.get("clearButton").setBounds(10, 440, 200, 50);

        add(new JLabel("Velg størrelse:")).setBounds(10, 305, 200, 10);
        components.get("sizeSlider").setBounds(5, 315, 200, 20);

        add(new JLabel("Layers: ")).setBounds(10, 365, 200, 15);
        add(layerPanel).setBounds(10, 375, 250, 475);

        components.get("setPointsButton").setBounds(10, 200, 200, 50);
        components.get("blurButton").setBounds(10, 260, 200, 50);
        components.get("invertButton").setBounds(10, 320, 200, 50);
        components.get("wrappingButton").setBounds(10, 380, 200, 50);
        add(new JLabel("Velg størrelse:")).setBounds(10, 575, 200, 10);
        components.get("sizeSlider").setBounds(5, 595, 200, 20);
        //components.get("squareButton").setBounds(25, 635, 50, 50);
        //components.get("ellipseButton").setBounds(85, 635, 50, 50);
        //components.get("hazeButton").setBounds(145, 635, 50, 50);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        
        Set<LayerView> keys = layerList.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            JPanel panel = (JPanel) it.next();
            Layer layer = layerList.get(panel);

            if (source == layer.getRemoveButton()) {
                it.remove();
                System.out.println("one to remove");
                layerDisplay.remove(panel);
            }
        }
        refresh();
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

    public Color getColor() {
        return cs.getColor();
    }
}
