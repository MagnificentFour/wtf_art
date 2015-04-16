package processing_test;

import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author nikla_000
 */
public class BigPix extends PApplet{
    int pxSize = 20;
    PImage bgImg;
    
    public void runFunction() {
        
        int loc = 0;
        int barSize = 2;
        int contrast1 = 80; //detection of darker color
        int contrast2 = 190; //detection of lighter color

        float r;
        float g;
        float b;
        float[] ave = new float[1000000];

        size(((int) (bgImg.width / pxSize)) * pxSize, ((int) (bgImg.height / pxSize)) * pxSize); //this removes extra border space
        noStroke();

        image(bgImg, 0, 0);
        loadPixels();

        rectMode(CENTER);

        for (int x = 0; x < (width / pxSize) - (pxSize / width * 2); x++) {
            for (int y = 0; y < (height / pxSize) - (pxSize / height * 2); y++) {
                loc = (x * pxSize) + ((y * pxSize) * width);
                r = red(pixels[loc]);
                g = green(pixels[loc]);
                b = blue(pixels[loc]);
                ave[loc] = ((r + g + b) / 3);
            }
        }

        fill(color(251, 251, 238));
        rect(width / 2, height / 2, width, height);
        fill(12, 17, 60);
        for (int i = 0; i < (width / pxSize) - (pxSize / width * 2); i++) {
            for (int j = 0; j < (height / pxSize) - (pxSize / height * 2); j++) {
                loc = (i * pxSize) + ((j * pxSize) * width);
                if (ave[loc] < contrast1) {
                    fill(color(12, 17, 60)); //darker color
                    rect((i * pxSize) + (pxSize / 2), (j * pxSize) + (pxSize / 2), pxSize + 1, pxSize - barSize);
                } else if (ave[loc] < contrast2) {
                    fill(color(248, 159, 159)); //lighter color
                    rect((i * pxSize) + (pxSize / 2), (j * pxSize) + (pxSize / 2), pxSize + 1, pxSize - barSize);
                }
            }
        }
    }
    
    public void setupSketch(PImage image, int pxSize) {
        
        bgImg = image;
        this.pxSize = pxSize;
        
    }
    
    public PImage getResult() {
        
        return this.get();
        
    }
    
}
