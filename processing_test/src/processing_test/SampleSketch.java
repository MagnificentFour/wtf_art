package processing_test;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import static javafx.scene.paint.Color.color;
import static javafx.scene.paint.Color.color;
import static javafx.scene.paint.Color.color;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import processing.core.*;
import static processing.core.PConstants.RGB;

/**
 *
 * @author nikla_000
 */
public class SampleSketch extends PApplet implements ActionListener, ChangeListener {

    int sizeWidth = 1280;
    int sizeHeight = 720;
    int pxSize = 20;
    int waitingPoint = 0;
    PImage bgImg;
    boolean gogo = false;
    boolean noSave = false;
    boolean copying = false;
    Random rand = new Random();
    int i;
    boolean done = false;

    JButton fwd;
    JButton back;
    CloneTool clTool = new CloneTool();

    State methodState = State.CLEAR;
    State nextState = State.CLEAR;

    ArrayList<Ellipse> ellipseList = new ArrayList<>();
    ArrayList<Ellipse> drawList = new ArrayList<>();

    JSlider source;

    ChangeTracker tracker = new ChangeTracker();

    @Override
    public void setup() {
        size(sizeWidth, sizeHeight);
        background(255);
        noLoop();
    }

    @Override
    public void draw() {
        //ellipse(mouseX, mouseY, 20, 20);
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

        if (tracker.hasNext()) {
            fwd.setEnabled(true);
        } else {
            fwd.setEnabled(false);
        }
        if (copying = true) {
            if (mousePressed == true) {
                System.out.println("heya");
                //Point p1 = clTool.getPoint1();
                //Point p2 = clTool.getPoint2();
                //Point cp = new Point(mouseX, mouseY);
                //int colour = color(bgImg.get(mouseX - 50, mouseY));
                for (int i = -10; i < 10; i++) {
                    for (int t = -10; t < 10; t++) {
                        int colour = color(get(mouseX + i - 50, mouseY + t));
                        set(mouseX + i, mouseY + t, colour);
                    }
                }
                //copy(bgImg, (p1.x-30), (p1.y-30), 60, 60, mouseX - 30, mouseY - 30, 60, 60);
                //tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
                //updatePixels();
                //redraw();
                //image(bgImg,0,0);
            }
        }

        if (tracker.hasPrev()) {
            back.setEnabled(true);
        } else {
            back.setEnabled(false);
        }

        if (bgImg
                != null) {

            if (methodState == State.DOTREP) {
                dotRep();
            } else if (methodState == State.PXLATION) {
                pxlation();
            } else if (methodState == State.CLEAR) {
                image(bgImg, 0, 0);
                tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
            } else if (methodState == State.SETPOINTS) {

            } else if (methodState == State.IMPORT) {
                methodState = nextState;
            }

        } else {

            fill(0);
            textSize(32);
            textAlign(CENTER);
            text("Please select an image", width / 2, height / 2);

        }

    }

    public void mouseClicked() {
        System.out.println("lolol");
        System.out.println(methodState);
        if (methodState == State.SETPOINTS) {
            if (waitingPoint == 0) {
                Point p = new Point(mouseX, mouseY);
                clTool.setPoint1(p);
                waitingPoint = 1;
            } else {
                Point p = new Point(mouseX, mouseY);
                clTool.setPoint2(p);
                waitingPoint = 0;
                methodState = State.CLONE;
                copying = true;
            }
        } else if (methodState == State.CLONE) {
            Point p1 = clTool.getPoint1();
            Point p2 = clTool.getPoint2();
            Point cp = new Point(mouseX, mouseY);
            loop();
            //colorMode(RGB, 100);
            //color c = color(12,12,12); 
            //color c = (RGB)bgImg.get(p.x, p.y);
            //copying = true;
            //redraw();
            System.out.println("copying");
            //System.out.println(i);
        }
    }

    /**
     * Loads and displays an image selected by the FileChooser
     *
     * @param filein Input image
     */
    public void loadBgImage(File filein) {
        bgImg = loadImage(filein.getAbsolutePath());
        if (bgImg.width > 720) {
            bgImg.resize(0, 720);
        }
        image(bgImg, 0, 0);
        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
        redraw();
    }

    /**
     * Saves the display window to an image file.
     *
     * @param dir The directory where the image will be saved.
     */
    public void saveImage(File dir) {
        save(dir.getAbsolutePath());
    }

    /**
     * Makes a representation of the image in dots.
     */
    private void dotRep() {
        int loc = 0;
        //int pxSize = 10;
        int createSize = 0;

        float r;
        float g;
        float b;

        float[] ave = new float[5000000];
        rectMode(CORNER);
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

        rectMode(CORNER);

        fill(0);
        rect(0, 0, width, height);
        fill(240, 110, 110);
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
        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
    }

    /**
     * Makes a pixelated representation of the image with two colors.
     */
    private void pxlation() {
        int loc = 0;
        int barSize = 2;
        int contrast1 = 80; //detection of darker color
        int contrast2 = 190; //detection of lighter color

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
        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
    }

    /**
     * Takes a StateCapture and imports the parameters.
     *
     * @param state The state to be loaded.
     */
    public void importState(StateCapture state) {

        image(state.getDisplayWindow(), 0, 0);
        pxSize = state.getPxSize();
        methodState = State.IMPORT;
        nextState = state.getRunState();

        if (source != null) {

            source.setValue(pxSize);

        }

        redraw();

    }

    public void setButtons(JButton fwd, JButton back) {
        this.fwd = fwd;
        this.back = back;
    }

    public void reset() {
        bgImg = null;
        tracker = new ChangeTracker();

        methodState = State.CLEAR;

        background(255);
        redraw();
    }

    public boolean changeHasNext() {
        return tracker.hasNext();
    }

    public boolean changeHasPrev() {
        return tracker.hasPrev();
    }

    /**
     * Action performed by buttons.
     *
     * @param e The action event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "run":
                gogo = false;
                noSave = false;
                background(255);
                methodState = State.DOTREP;
                redraw();
                break;
            case "dot":
                noSave = true;
                gogo = false;
                methodState = State.DOTREP;
                redraw();
                break;
            case "clear":
                gogo = false;
                background(255);
                redraw();
                methodState = State.CLEAR;

                break;
            case "pxlate":
                gogo = false;
                background(255);
                methodState = State.PXLATION;
                redraw();
                break;
            case "double":
                frameRate(120);
                break;
            case "triple":
                frameRate(180);
                break;
            case "forward":
                if (tracker.hasChanged()) {
                    importState(tracker.getNextEntry());
                }
                if (!tracker.hasNext()) {
                    fwd.setEnabled(false);
                }
                break;
            case "back":
                if (tracker.hasChanged()) {
                    importState(tracker.getPrevEntry());
                }
                if (!tracker.hasPrev()) {
                    back.setEnabled(false);
                }
                break;
            case "clone":
                //CloneTool clTool = new CloneTool();
                //Point p = MouseInfo.getPointerInfo().getLocation(); 
                //System.out.println(p);
                methodState = State.CLONE;
                break;
            case "setPoints":
                methodState = State.SETPOINTS;
                break;
        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        source = (JSlider) e.getSource();

        if (!source.getValueIsAdjusting()) {
            pxSize = source.getValue();
            redraw();
        }
    }
}
