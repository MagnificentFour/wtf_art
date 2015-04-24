/*
 *  Credit to Jonathan Martin for his processing project "Pixelate" upon
 *  which this code is based.
 *  
 *  Jonathan Martin - reddit user "creepyeyes"
 *  http://www.reddit.com/r/processing/comments/21sgz0/more_progress_on_my_photofilter_its_come_out/
 *
 */

package processing_test;

import java.awt.Color;
import processing.core.*;

/**
 *
 * @author nikla_000
 */
public class BigPix extends PApplet{
    int pxSize = 20;
    PImage bgImg;
    PGraphics gr;

    /**
     * Code to translate the given picture to a pixelated version.
     *
     */
    public void runFunction(Color c) {
               
        int loc = 0;
        int barSize = 2;
        int contrast1 = 80; //detection of darker color
        int contrast2 = 190; //detection of lighter color

        float r;
        float g;
        float b;
        float[] ave = new float[1000000];
        
        size(((int) (bgImg.width / pxSize)) * pxSize, ((int) (bgImg.height / pxSize)) * pxSize); //this removes extra border space
        gr = createGraphics(bgImg.width, bgImg.height);
        gr.beginDraw();
        //gr.background(0,0);
        gr.noStroke();

        gr.image(bgImg, 0, 0);
        gr.loadPixels();

        gr.rectMode(CENTER);

        for (int x = 0; x < (gr.width / pxSize) - (pxSize / gr.width * 2); x++) {
            for (int y = 0; y < (height / pxSize) - (pxSize / height * 2); y++) {
                loc = (x * pxSize) + ((y * pxSize) * gr.width);
                r = red(gr.pixels[loc]);
                g = green(gr.pixels[loc]);
                b = blue(gr.pixels[loc]);
                ave[loc] = ((r + g + b) / 3);
            }
        }

        gr.fill(color(251, 251, 238));
        gr.rect(gr.width / 2, gr.height / 2, gr.width, gr.height);
        gr.fill(12, 17, 60);
        for (int i = 0; i < (gr.width / pxSize) - (pxSize / gr.width * 2); i++) {
            for (int j = 0; j < (gr.height / pxSize) - (pxSize / gr.height * 2); j++) {
                loc = (i * pxSize) + ((j * pxSize) * gr.width);
                if (ave[loc] < contrast1) {
                    gr.fill(color(12, 17, 60)); //darker color
                    gr.rect((i * pxSize) + (pxSize / 2), (j * pxSize) + (pxSize / 2), pxSize + 1, pxSize - barSize);
                } else if (ave[loc] < contrast2) {
                    gr.fill(color(c.getRed(), c.getGreen(), c.getBlue())); //lighter color
                    gr.rect((i * pxSize) + (pxSize / 2), (j * pxSize) + (pxSize / 2), pxSize + 1, pxSize - barSize);
                }
            }
        }
        gr.endDraw();
    }

    /**
     * Sets up the sketch
     *
     * @param image Image object
     * @param pxSize
     */
    public void setupSketch(PImage image, int pxSize) {
        
        bgImg = image;
        this.pxSize = pxSize;
                
    }

    /**
     * Returns result of the algorithm
     * @return gr
     */
    public PGraphics getResult() {
        
        return gr;
        
    }
    
}
