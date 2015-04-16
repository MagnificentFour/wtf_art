/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.*;

/**
 *
 * @author Oystein
 */
public class MapTo extends PApplet {

    PImage bgImg;
    boolean noSave = true;
    int cellsize = 2; // Dimensions of each cell in the grid
    int cols, rows;   // Number of columns and rows in our system

    public void setup() {
        size(bgImg.width, bgImg.height, P3D);
    }
    public void setupSketch(PImage image) {

        bgImg = image;

        cols = bgImg.width / cellsize;             // Calculate # of columns
        rows = bgImg.height / cellsize;            // Calculate # of rows

    }

    public void function() {
        size(bgImg.width, bgImg.height, P3D);

        background(0);
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
                float z = (float) ((mouseX / (float) width) * brightness(bgImg.pixels[loc]) - 100.0);
                // Translate to the location, set fill and stroke, and draw the rect
                pushMatrix();
                translate(x, y, z);
                fill(c);
                noStroke();
                rectMode(CENTER);
                rect(0, 0, cellsize, cellsize);
                popMatrix();
            }

        }
    }

}
