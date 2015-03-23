package processing_test;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author nikla_000
 */
public class Dotting extends PApplet {
    
    PImage bgImg;
    int pxSize = 20;
    boolean noSave = true;
    
    ArrayList<Ellipse> ellipseList = new ArrayList<>();
    
    public void setupSketch(PImage image, int pixelSize, boolean noSave) {
        
        bgImg = image;
        pxSize = pixelSize;
        this.noSave = noSave;
        
    }
    
    public void runFunction() {
        
        int loc = 0;
        int createSize = 0;

        float r;
        float g;
        float b;

        float[] ave = new float[5000000];
        
        rectMode(CORNER);
        size(bgImg.width, bgImg.height);
        noStroke();

        image(bgImg, 0, 0);
        loadPixels();
        for (int x = 0; x < (width / pxSize); x++) {
            for (int y = 0; y < (height / pxSize); y++) {
                loc = (x * pxSize) + ((y * pxSize) * width);
                r = red(pixels[loc]);
                g = green(pixels[loc]);
                b = blue(pixels[loc]);
                ave[loc] = ((r + g + b) / 3);
            }
        }

        rectMode(CORNER);

        fill(0);
        rect(0, 0, width, height);
        fill(240, 110, 110);
        for (int i = 0; i < width / pxSize; i++) {
            for (int j = 0; j < height / pxSize; j++) {
                loc = (i * pxSize) + ((j * pxSize) * width);
                createSize = (int) constrain(map(ave[loc], 0, 255, 0, (float) (pxSize * 1.1)), 0, (float) (pxSize * 1.1));
                if (noSave) {
                    ellipse((i * pxSize) + (pxSize), (j * pxSize) + (pxSize), createSize, createSize);
                } else {
                    
                    ellipseList.add(new Ellipse((i * pxSize) + (pxSize), (j * pxSize) + (pxSize), createSize, createSize));
                    //loop();
                }
            }
        }
        
    }
    
    public ArrayList<Ellipse> getEllipseList() {
        
        return ellipseList;
        
    }
    
    public PImage getResult() {
        
        return this.get();
        
    }
    
}
