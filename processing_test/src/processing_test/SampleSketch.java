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
    boolean moveToSpot = false;
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
//        if (gogo) {
//            background(255);
//            for (Ellipse e : ellipseList) {
//                
//                fill(0);
//                e.setAnimXAndY(mouseX + (rand.nextInt(80) - 40), mouseY + (rand.nextInt(80) - 40));
//                ellipse(e.getAnimX(), e.getAnimY(), e.getHeight(), e.getWidth());
//
//            }
//        } else if(moveToSpot) {
//            
//            moveToSpot();
//            
//        }

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

        if (bgImg != null) {

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
    
    public void moveToSpot() {
        background(0);
        for(Ellipse e : ellipseList) {
            float oldX = e.getAnimX();
            float oldY = e.getAnimY();
            float actualX = e.getxCoord();
            float actualY = e.getyCoord();
            float newX = 0;
            float newY = 0;
            
            if(oldX >= actualX - 11 && oldX <= actualX + 10) {
                newX = actualX;
            } else if(oldX > actualX) {
                newX = oldX - 10;
            } else if(oldX < actualX){
                newX = oldX + 10;
            } 
            
            if(oldY >= actualY - 11 && oldY <= actualY + 11) {
                newY = actualY;
            } else if(oldY > actualY) {
                newY = oldY - 10;
            } else if(oldY < actualY) {
                newY = oldY + 10;
            } 
            e.setAnimXAndY(newX, newY);
            
            fill(255,0,0);
            ellipse(e.getAnimX(), e.getAnimY(), e.getHeight(), e.getWidth());
        }
        
    }

    public void animateDots() {

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
        background(bgImg);
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
        Dotting dotRep = new Dotting();
        dotRep.setupSketch(bgImg, pxSize, noSave);
        dotRep.init();
        dotRep.runFunction();
//        ellipseList = dotRep.getEllipseList();
//        gogo = true;
//        methodState = State.NOACTION;

//        loop();
        PImage img = dotRep.getResult();
        image(img, 0, 0);

//        blend(img, 0, 0, img.width, img.height, 0, 0, width, height, SUBTRACT);
//        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
    }

    /**
     * Makes a pixelated representation of the image with two colors.
     */
    private void pxlation() {
        BigPix pxlation = new BigPix();
        pxlation.setupSketch(bgImg, pxSize);
        pxlation.init();
        pxlation.runFunction();
        image(pxlation.getResult(), 0, 0);

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
                //gogo = false;
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
