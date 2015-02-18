
package processing_test;

import ij.ImagePlus;
import java.awt.image.BufferedImage;
import processing.core.PImage;

/**
 *
 * @author nikla_000
 */
public class ProcessImage {
    
    ImagePlus image;
    PImage pImage;
    String imgPath;
    
    public ProcessImage() {
//        imgPath = "E:/newimage.jpg";
//        setCurrentImage(imgPath);
    }
    
    private ImagePlus makeCannyEdge(BufferedImage in) {
        ImagePlus out = new ImagePlus(); 
        CannyEdgeDetector ced = new CannyEdgeDetector(in, 40, 80);
        out.setImage(ced.filter());
        return out;
    }
    
    public PImage getImage() {
        pImage = new PImage(image.getBufferedImage());
        return pImage;
    }
    
    public PImage getCannyImage() {
        ImagePlus cannyEdge = makeCannyEdge(image.getBufferedImage());
        cannyEdge.show();
        
        try {
        pImage = new PImage(cannyEdge.getBufferedImage());
        } catch(ClassCastException e) {
            System.out.println("Meh, something something " + e);
        }
        return pImage;
    }
    
    public void setCurrentImage(String imgPath) {
        image = new ImagePlus(imgPath);
    }
}
