
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
        imgPath = "E:/image.jpg";
        setCurrentImage(imgPath);
    }
    
    private BufferedImage makeCannyEdge(BufferedImage in) {
        CannyEdgeDetector ced = new CannyEdgeDetector(in, 40, 80);
        BufferedImage out = ced.filter();
        
        return out;
    }
    
    public PImage getImage() {
        pImage = new PImage(image.getBufferedImage());
        return pImage;
    }
    
    public void setCurrentImage(String imgPath) {
        image = new ImagePlus(imgPath);
    }
}
