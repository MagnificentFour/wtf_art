package processing_test;

import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 *
 * @author nikla_000
 */
public class Layer implements ItemListener {

    private ArrayList<PGraphics> layerGraphics;
    private PImage layerImage;
    private boolean show;
    private boolean isDisplayed;
    private boolean isBackground;
    private JCheckBox checkShow;

    /**
     *
     */
    public Layer() {
        layerGraphics = new ArrayList<>();
        show = true;
        isDisplayed = false;
        isBackground = false;

        setUp();
    }

    private void setUp() {
        checkShow = new JCheckBox();
        checkShow.setSelected(true);
        checkShow.addItemListener(this);

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
    public ArrayList<PGraphics> getGraphics() {
        return layerGraphics;
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

    /**
     *
     * @param show
     */
    public void setShow(boolean show) {
        this.show = show;
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
}
