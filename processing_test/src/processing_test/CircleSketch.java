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
    boolean running;

    public void setup() {
        size(sizeWidth, sizeHeight);
        background(255);
        frameRate(120);
        running = false;
        xK = 0;
        yK = 0;
        noLoop();
    }

    public void draw() {
        if (running) {
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
    }

    public void start() {
        running = true;
        loop();
    }
    
    public void clear() {
        running = false;
        background(255);
        xK = 0;
        yK = 0;
    }
}
