package processing_test;

import processing.core.*;

/**
 *
 * @author nikla_000
 */
public class CircleSketch extends PApplet {

    int sizeWidth = 400;
    int sizeHeight = 400;
    int drawStatus = 0;
    int xK = 0;
    int yK = 0;

    public void setup() {
        size(sizeWidth, sizeHeight);
        background(255);
        frameRate(120);
        wtfSikring();
    }

    public void draw() {
        for (int i = yK; i < yK + 11; i++) {
            for (int x = xK; x < xK + 11; x++) {
                stroke(random(255), random(255), random(255));
                point(x, i);
            }
        }
        xK += 10;

        if (xK == sizeWidth) {
            yK += 10;
            xK = 0;
        }
    }

    public void wtfSikring() {
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {
                stroke(random(255), random(255), random(255));
                point(x, y);
            }
        }
    }
}
