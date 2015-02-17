package processing_test;

import processing.core.*;

/**
 *
 * @author nikla_000
 */
public class CircleSketch extends PApplet {

    float lastpnkt = 100;
    float lastpnkt2 = 100;

    public void setup() {
        size(400, 400);
        background(255);
        noLoop();
    }

    public void draw() {
        //background(155);
        float pnkt = random(400);
        float pnkt2 = random(400);
        fill(80, 150, 20);
        if ((pnkt > lastpnkt - 25 && pnkt < lastpnkt + 25)
                && (pnkt2 > lastpnkt2 - 25 || pnkt2 < lastpnkt2 + 25)) {
            line(pnkt, pnkt2, pnkt + 20, pnkt2 + 20);
            lastpnkt = pnkt;
            lastpnkt2 = pnkt2;
        }
        ellipseMode(CENTER);
        ellipse(mouseX, mouseY, 40, 40);
    }
    
    public void start() {
        loop();
    }
}
