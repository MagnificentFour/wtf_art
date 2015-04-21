/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processing_test;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author nikla_000
 */
public class LayerView extends JPanel implements ActionListener {
    
    private final LinkedHashMap<LayerViewItem, Layer> layerList;
    private final Box layerDisplay;
    
    public LayerView() {
        layerList = new LinkedHashMap<>();
        layerDisplay = Box.createVerticalBox();
        add(layerDisplay);
    }
    
    /**
     * Adds a view of the newly added layer to the tool window.
     *
     * @param layer The new layer.
     */
    public void addLayerView(Layer layer) {
        ImageIcon icon = layer.getImageIcon();
        JButton b = layer.getRemoveButton();
        int layerNum = layerList.size() + 1;
        layer.setLayerNum(layerNum);

        b.addActionListener(this);

        LayerViewItem newLayerView = new LayerViewItem(icon, b, layerNum, layer.getCheckBox());

        // Add a mouse listener to the panel view of the layer.
        newLayerView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int count = mouseEvent.getClickCount();
                if (count == 1 && mouseEvent.getSource() instanceof JPanel) {
                    LayerViewItem panel = (LayerViewItem) mouseEvent.getSource();
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

        Set<LayerViewItem> keys = layerList.keySet();
        Iterator it = keys.iterator();
        while(it.hasNext()) {
            LayerViewItem panel = (LayerViewItem) it.next();
            Layer layer = layerList.get(panel);

            if (layer.remove()) {
                it.remove();
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
    
    public void refreshLayers(ArrayList<Layer> layers) {
        
        layerDisplay.removeAll();
        
        Set<LayerViewItem> keys = layerList.keySet();
        
        for(LayerViewItem panel : keys) {
            Layer layer = layerList.get(panel);
            layer.isDisplayed(false);
        }
        
        layerList.clear();
        
        for(Layer layer : layers) {
            addLayerView(layer);
            layer.isDisplayed(true);
        }
    }

    /**
     * Set the selected layer.
     *
     * @param selectedPanel Currently selected layer panel.
     */
    private void setSelected(LayerViewItem selectedPanel) {

        layerList.get(selectedPanel).selected(true);
        Set<LayerViewItem> keys = layerList.keySet();
        for (LayerViewItem p : keys) {
            if (p != selectedPanel) {
                layerList.get(p).selected(false);
            }
        }
        refresh();

    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        Set<LayerViewItem> keys = layerList.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            JPanel panel = (JPanel) it.next();
            Layer layer = layerList.get(panel);

            if (source == layer.getRemoveButton()) {
                it.remove();
                layerDisplay.remove(panel);
            }
        }
        refresh();
    }
    
}
