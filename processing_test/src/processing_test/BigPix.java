package processing_test;

import processing.core.*;

/**
 *
 * @author nikla_000
 */
public class BigPix extends PApplet{
    int pxSize = 20;
    PImage bgImg;
    PGraphics gr;
    
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
                    gr.fill(color(248, 159, 159)); //lighter color
                    gr.rect((i * pxSize) + (pxSize / 2), (j * pxSize) + (pxSize / 2), pxSize + 1, pxSize - barSize);
                }
            }
        }
        gr.endDraw();
    }
    
    public void setupSketch(PImage image, int pxSize) {
        
        bgImg = image;
        this.pxSize = pxSize;
                
    }
    
    public PImage getResult() {
        
        return gr.get();
        
    }
    
}
