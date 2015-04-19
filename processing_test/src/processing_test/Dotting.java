package processing_test;

import java.util.ArrayList;
import processing.core.*;

/**
 *
 * @author nikla_000
 */
public class Dotting extends PApplet {
    
    PImage bgImg;
    PGraphics gr;
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
        
        size(bgImg.width, bgImg.height);
        gr = createGraphics(bgImg.width, bgImg.height);
        
        gr.beginDraw();
        gr.rectMode(CORNER);
        gr.noStroke();

        gr.image(bgImg, 0, 0);
        gr.loadPixels();
        for (int x = 0; x < (gr.width / pxSize); x++) {
            for (int y = 0; y < (gr.height / pxSize); y++) {
                loc = (x * pxSize) + ((y * pxSize) * gr.width);
                r = red(gr.pixels[loc]);
                g = green(gr.pixels[loc]);
                b = blue(gr.pixels[loc]);
                ave[loc] = ((r + g + b) / 3);
            }
        }

        gr.rectMode(CORNER);

        gr.fill(0);
        gr.rect(0, 0, width, height);
        gr.fill(240, 110, 110);
        for (int i = 0; i < gr.width / pxSize; i++) {
            for (int j = 0; j < gr.height / pxSize; j++) {
                loc = (i * pxSize) + ((j * pxSize) * gr.width);
                createSize = (int) constrain(map(ave[loc], 0, 255, 0, (float) (pxSize * 1.1)), 0, (float) (pxSize * 1.1));
                if (noSave) {
                    gr.ellipse((i * pxSize) + (pxSize), (j * pxSize) + (pxSize), createSize, createSize);
                } else {
                    
                    ellipseList.add(new Ellipse((i * pxSize) + (pxSize), (j * pxSize) + (pxSize), createSize, createSize));
                    //loop();
                }
            }
        }
        
    }
    
    public PGraphics getResult() {
        
        return gr;
        
    }
    
}
