package processing_test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import processing.core.*;

/**
 *
 * @author nikla_000
 */
public class CircleSketch extends PApplet implements ActionListener {

    int sizeWidth = 400;
    int sizeHeight = 400;
    int drawStatus = 0;
    int xK = 0;
    int yK = 0;
    boolean running;
    ProcessImage imageProcessor;

    @Override
    public void setup() {
        size(sizeWidth, sizeHeight);
        background(255);
        frameRate(120);
        running = false;
        xK = 0;
        yK = 0;
        imageProcessor = new ProcessImage();
        noLoop();
    }

    public void draw() {
//        if (running) {
//            for (int i = yK; i < yK + 11; i++) {
//                for (int x = xK; x < xK + 11; x++) {
//                    stroke(random(255), random(255), random(255));
//                    point(x, i);
//                }
//            }
//            xK += 10;
//
//            if (xK == sizeWidth) {
//                yK += 10;
//                xK = 0;
//            }
//        }
        image(imageProcessor.getImage(), 0, 0);
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
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getActionCommand().equals(" ")) {
            
        }
    }
}
