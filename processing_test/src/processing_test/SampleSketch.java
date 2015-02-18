/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import static javafx.scene.paint.Color.color;
import processing.core.*;

/**
 *
 * @author nikla_000
 */
public class SampleSketch extends PApplet implements ActionListener {

    int sizeWidth = 450;
    int sizeHeight = 500;
    PImage bgImg;
    boolean gogo = false;

    @Override
    public void setup() {
        size(sizeWidth, sizeHeight);
        background(255);
        noLoop();
    }

    @Override
    public void draw() {
        if (gogo) {
            image(bgImg, 0, 0);
            
        }
    }

    public void loadBgImage(File filein) {
        bgImg = loadImage(filein.getAbsolutePath());
        gogo = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "run":
                redraw();
                break;
        }

    }
}
