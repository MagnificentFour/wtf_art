
package processing_test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import processing.core.*;

/**
 *
 * @author nikla_000
 */
public class SampleSketch extends PApplet implements ActionListener, ChangeListener {

    int sizeWidth = 1280;
    int sizeHeight = 720;
    int pxSize = 20;
    PImage bgImg;
    boolean gogo = false;
    boolean noSave = false;
    Random rand = new Random();
    int i;
    boolean done = false;
    
    boolean dotrep = false;
    boolean pxlation = false;

    ArrayList<Ellipse> ellipseList = new ArrayList<>();
    ArrayList<Ellipse> drawList = new ArrayList<>();

    @Override
    public void setup() {
        size(sizeWidth, sizeHeight);
        background(255);
        noLoop();
    }

    @Override
    public void draw() {
        if (gogo) {
            if (!ellipseList.isEmpty()) {
                drawList.add(ellipseList.remove(rand.nextInt(ellipseList.size())));
            } else if (done) {
                noLoop();
            } else {
                done = true;
                drawList.stream().forEach((e) -> {
                    if (e.incrementSize()) {
                        done = false;
                    }
                });
            }

            background(0);

            fill(255);

            drawList.stream().forEach((e) -> {
                ellipse(e.getxCoord(), e.getyCoord(), e.getWidth(), e.getHeight());
                e.incrementSize();
            });

        }
        if(dotrep) {
            dotRep();
        } else if(pxlation) {
            pxlation();
        }
    }

    public void loadBgImage(File filein) {
        bgImg = loadImage(filein.getAbsolutePath());
        if (bgImg.width > 720) {
            bgImg.resize(0, 720);
        }
        image(bgImg, 0, 0);

        redraw();
    }

    private void dotRep() {
        int loc = 0;
        //int pxSize = 10;
        int createSize = 0;

        float r;
        float g;
        float b;

        float[] ave = new float[5000000];

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
        
        fill(0);
        rect(0, 0, width, height);
        fill(240,110,110);
        for (int i = 0; i < width / pxSize; i++) {
            for (int j = 0; j < height / pxSize; j++) {
                loc = (i * pxSize) + ((j * pxSize) * width);
                createSize = (int) constrain(map(ave[loc], 0, 255, 0, (float) (pxSize * 1.1)), 0, (float) (pxSize * 1.1));
                if (noSave) {
                    ellipse((i * pxSize) + (pxSize), (j * pxSize) + (pxSize), createSize, createSize);
                } else {
                    ellipseList.add(new Ellipse((i * pxSize) + (pxSize), (j * pxSize) + (pxSize), createSize, createSize));
                    gogo = true;
                    loop();
                }
            }
        }
    }

    private void pxlation() {
        int loc = 0;
        int barSize = 2;
        int contrast1 = 100; //detection of darker color
        int contrast2 = 150; //detection of lighter color

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

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "run":
                gogo = false;
                noSave = false;
                background(255);
                dotrep = true;
                pxlation = false;
                redraw();
                break;
            case "dot":
                noSave = true;
                gogo = false;
                dotrep = true;
                pxlation = false;
                redraw();
                break;
            case "clear":
                gogo = false;
                background(255);
                redraw();
                break;
            case "pxlate":
                gogo = false;
                background(255);
                dotrep = false;
                pxlation = true;
                redraw();
                break;
            case "double":
                frameRate(120);
                break;
            case "triple":
                frameRate(180);
        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        pxSize = source.getValue();
        redraw();
    }
}
