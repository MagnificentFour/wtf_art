/**
 * Draw method source code rights goes to Morgan Kaufmann Publishers, Copyright
 * © 2008 Elsevier Inc. All rights reserved.
 *
 * @author Oystein
 */
package processing_test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.*;

    /**
     * A Class to map image from 2D to 3D introduces a third axe
     *
     * @author Oystein
     */
public class MapTo extends PApplet implements ChangeListener, ActionListener {

    PImage bgImg;
    PGraphics gr;
    int cellsize = 1; // Dimensions of each cell in the grid
    int cols, rows;   // Number of columns and rows in our system
    int sizeWidth;
    int sizeHeight;
    int changeValue = 0;

    /**
     * Main method for processing, it needs the size(P3D) to make the function
     * work
     */
    @Override
    public void setup() {
        size(1280, 960, P3D);

        cols = sizeWidth / cellsize;             // Calculate # of columns
        rows = sizeHeight / cellsize;            // Calculate # of rows
        background(0);
    }

    /**
     * Sets up the sketch and assigns variables
     *
     * @bgImg, sizeWidth, sizeHeight, cols, rows
     * @param image
     */
    public void setupSketch(PImage image) {
        bgImg = image;
        sizeWidth = bgImg.width;
        sizeHeight = bgImg.height;
        cols = bgImg.width / cellsize;             // Calculate # of columns
        rows = bgImg.height / cellsize;            // Calculate # of rows
    }

    /**
     * Main draw method to map the image from 2D to 3D, ChangeValue is the
     * variable that changes from the input on the slider
     */
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

    /**
     * Returns a PImage
     *
     * @return bgImg
     */
    public PImage function() {

        return this.get();
    }

    /**
     * Returns the PImage as a PGraphics image to show on the interface
     *
     * @return gr
     */
    public PGraphics getResult() {
        gr = createGraphics(bgImg.width, bgImg.height);
        gr.beginDraw();
        gr.image(this.get(), 0, 0);
        gr.endDraw();
        return gr;
    }

    /**
     * A method to decrease the size of cellsize
     * Changes the value of cellsize
     */
    public void decreaseCell() {
        this.cellsize = cellsize - 2;
        setup();
    }

    /**
     * ChangeEvent listener to change the value of ChangeValue men the slider is
     * moved
     *
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        changeValue = source.getValue();
    }

    /**
     * A ActionListener to change the value of 
     * cellsize when the button is pressed
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.cellsize = cellsize + 2;
        setup();
    }

}
