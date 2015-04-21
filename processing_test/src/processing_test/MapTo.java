/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.*;

/**
 *
 * @author Oystein
 */
public class MapTo extends PApplet implements ChangeListener, ActionListener{

    PImage bgImg;
    int cellsize = 1; // Dimensions of each cell in the grid
    int cols, rows;   // Number of columns and rows in our system
    int sizeWidth;
    int sizeHeight;
    int changeValue = 0;

    @Override
    public void setup() {
        size(1280, 960, P3D);

        cols = sizeWidth / cellsize;             // Calculate # of columns
        rows = sizeHeight / cellsize;            // Calculate # of rows
        background(0);

    }

//    public void setSize(int sizeWidth, int sizeHeight) {
//        size(sizeWidth, sizeHeight, P3D);
//        
//    }
    public void setupSketch(PImage image) {
        bgImg = image;
        sizeWidth = bgImg.width;
        sizeHeight = bgImg.height;
        cols = bgImg.width / cellsize;             // Calculate # of columns
        rows = bgImg.height / cellsize;            // Calculate # of rows
    }

 
    public void draw() {

        if (bgImg != null) {
            loadPixels();
            // Begin loop for columns
            for (int i = 0; i < cols; i++) {
                // Begin loop for rows
                for (int j = 0; j < rows; j++) {
                    int x = i * cellsize + cellsize / 2; // x position
                    int y = j * cellsize + cellsize / 2; // y position
                    int loc = x + y * bgImg.width;           // Pixel array location
                    int c = color(bgImg.pixels[loc]);       // Grab the color
                    // Calculate a z position as a function of mouseX and pixel brightness
                    float z = (float) ((changeValue / (float) width) * brightness(bgImg.pixels[loc]) - 100.0);
                    // Translate to the location, set fill and stroke, and draw the rect
                    pushMatrix();
                    translate(x, y, z);
                    noStroke();
                    rectMode(CENTER);
                    fill(c);
                    rect(0, 0, cellsize, cellsize);
                    popMatrix();

                }

            }
        }
    }

    public PImage function() {

        return this.get();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        changeValue = source.getValue();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       this.cellsize = cellsize + 5;
       setup();
       
    }

}
