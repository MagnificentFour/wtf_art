/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import processing.core.PApplet;

/**
 *
 * @author nikla_000
 */
public class SampleSketch extends PApplet implements ActionListener {

    int sizeWidth = 400;
    int sizeHeight = 400;
    
    @Override
    public void setup() {
        size(sizeWidth, sizeHeight);
        background(255);
        noLoop();
    }
    
    @Override
    public void draw() {
        ellipse(mouseX, mouseY, 40, 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        loop();
    }

}
