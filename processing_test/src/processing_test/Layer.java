package processing_test;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 *
 * @author nikla_000
 */
public class Layer implements ItemListener, ActionListener {

    private ArrayList<PGraphics> layerGraphics;
    private PGraphics mainGraphics;
    private PImage layerImage;
    private boolean show;
    private boolean isDisplayed;
    private boolean isBackground;
    private boolean selected;
    private boolean remove;
    private int currentLayerNum;
    private JCheckBox checkShow;
    private JButton removeButton;

    /**
     *
     */
    public Layer() {
        layerGraphics = new ArrayList<>();
        show = true;
        isDisplayed = false;
        isBackground = false;
        remove = false;

        setUp();
    }

    private void setUp() {
        checkShow = new JCheckBox();
        removeButton = new JButton("x");
        
        checkShow.setSelected(true);
        checkShow.addItemListener(this);

        removeButton.addActionListener(this);
    }

    /**
     *
     * @param image
     */
    public Layer(PImage image) {

        this();
        layerImage = image;

    }

    /**
     *
     * @param graphic
     */
    public Layer(PGraphics graphic) {

        this();
        layerGraphics.add(graphic);
        mainGraphics = graphic;

    }

    /**
     *
     * @return
     */
    public PImage getLayerImage() {
        return layerImage;
    }

    /**
     *
     * @return
     */
    public ArrayList<PGraphics> getAllGraphics() {
        return layerGraphics;
    }
    
    public PGraphics getGraphics() {
        return mainGraphics;
    }
    
    public void setGraphics(PGraphics graphics) {
        mainGraphics = graphics;
        layerGraphics.clear();
        layerGraphics.add(graphics);
    }

    /**
     *
     * @return
     */
    public boolean show() {
        return show;
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    public void isDisplayed(boolean val) {
        isDisplayed = val;
    }

    /**
     *
     * @return
     */
    public JCheckBox getCheckBox() {
        return checkShow;
    }
    
    public JButton getRemoveButton() {
        return removeButton;
    }
    
    public int layerNum() {
        return currentLayerNum;
    }
    
    public void setLayerNum(int num) {
        currentLayerNum = num;
    }

    /**
     *
     * @param show
     */
    public void setShow(boolean show) {
        this.show = show;
    }
    
    public boolean selected() {
        return selected;
    }
    
    public void selected(boolean selected) {
        this.selected = selected;
    }
    
    public boolean remove() {
        return remove;
    }
    
    public void remove(boolean remove) {
        this.remove = remove;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isBackground() {
        return isBackground;
    }
    
    /**
     * 
     * @param val 
     */
    public void isBackground(boolean val) {
        isBackground = val;
    }

    public ImageIcon getImageIcon() {

        if (layerGraphics.size() > 0) {
            PGraphics gr = layerGraphics.get(0);
            gr.beginDraw();
            for (PGraphics g : layerGraphics) {
                if (layerGraphics.indexOf(g) != 0) {
                    gr.image(g, 0, 0);
                    layerGraphics.remove(g);
                }
            }
            gr.endDraw();
            
            PImage img = gr.get();
            img.resize(0, 75);
            
            return new ImageIcon(img.getImage());
            
        }else if (layerImage != null) {
            PImage p = layerImage.get();
            p.resize(0, 75);
            Image image = p.getImage();
            return new ImageIcon(image);
        }
        
        return null;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        show = !show;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        remove = true;
        System.out.println("Roger remove is " + remove);
    }
}
