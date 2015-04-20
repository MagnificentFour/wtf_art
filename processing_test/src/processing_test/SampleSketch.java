package processing_test;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import static javafx.scene.paint.Color.color;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import processing.core.*;
import static processing.core.PConstants.RGB;

/**
 *
 * @author nikla_000
 */
public class SampleSketch extends PApplet
        implements ActionListener, ChangeListener, ItemListener {

    int sizeWidth = 1280;
    int sizeHeight = 720;
    int pxSize = 20;
    int cloneRadius = 25;
    int waitingPoint = 0;
    PImage bgImg;
    PImage circle;
    PGraphics pg;
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
    int cellsize = 2; // Dimensions of each cell in the grid
    int cols, rows;   // Number of columns and rows in our system

    LayerHandler layerHandler = new LayerHandler();
    PGraphics dotting;

    JSlider source;
    JSlider cloneSource;

    ChangeTracker tracker = new ChangeTracker();
    ToolWindow toolWindow;

    @Override
    public void setup() {
        size(sizeWidth, sizeHeight);
        background(255);
        pg = createGraphics(sizeWidth, sizeHeight);
        frameRate(1);
//        noLoop();
    }

    @Override
    public void draw() {
//        background(255);
        if (tracker.hasNext()) {
            fwd.setEnabled(true);
        } else {
            fwd.setEnabled(false);
        }

        if (copying == true) {

            //cursor(circle, cloneRadius/2, cloneRadius/2);
            if (mousePressed == true) {
                System.out.println("heya");
                Point p1 = clTool.getPoint1();
                Point p2 = clTool.getPoint2();
                int distanceX = p2.x - p1.x;
                int distanceY = p2.y - p1.y;
                //Point cp = new Point(mouseX, mouseY);
                //int colour = color(bgImg.get(mouseX - 50, mouseY));
                int staticX = mouseX;
                int staticY = mouseY;
                for (int i = -cloneRadius / 2; i < cloneRadius / 2; i++) {
                    for (int t = -cloneRadius / 2; t < cloneRadius / 2; t++) {
                        if (dist(mouseX, mouseY, (mouseX + i), (mouseY + t)) <= (cloneRadius / 2)) {
                            int colour = color(get((mouseX - distanceX) + i, (mouseY - distanceY) + t));
                            set(mouseX + i, mouseY + t, colour);
                        }
                    }
                }
            }
        }

        if (tracker.hasPrev()) {
            back.setEnabled(true);
        } else {
            back.setEnabled(false);
        }

        for (Layer layer : layerHandler.getLayers()) {
            
            if (!layer.isDisplayed()) {
                toolWindow.addLayerView(layer);
                layer.isDisplayed(true);
                layer.getCheckBox().addItemListener(this);
            }

//                    System.out.println("Yap " + layer);
            if (layer.isBackground()) {
                image(layer.getLayerImage(), 0, 0);
            } else if (layer.show()) {
                for (PGraphics graphic : layer.getAllGraphics()) {
                    image(graphic, 0, 0);
                }
            }
        }

        if (!layerHandler.getLayers().isEmpty()) {//bgImg != null) {

            if (methodState == State.DOTREP) {
                dotRep(layerHandler.checkFuncStat("Dotting"));
            } else if (methodState == State.PXLATION) {
                pxlation(layerHandler.checkFuncStat("BigPix"));
            } else if (methodState == State.CLEAR) {
                image(layerHandler.getLayers().get(0).getLayerImage(), 0, 0);
                tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
            } else if (methodState == State.MAPTO) {
                mapTo();
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
                loop();
            }
        }

//        gogo = false;
//        moveToSpot = true;
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
//        resize(bgImg.width, bgImg.height);
//        System.out.println(bgImg.width + " " + bgImg.height);
        layerHandler.setBackground(new Layer(bgImg));
        //background(bgImg);
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
    private void dotRep(int index) {
        Dotting dotRep = new Dotting();
        dotRep.setupSketch(bgImg, pxSize, noSave);
        dotRep.init();
        dotRep.runFunction();
//        methodState = State.NOACTION;
        PGraphics gr = dotRep.getResult();
        
        if (index < 0) {
            layerHandler.addLayer(new Layer(gr), "Dotting");
        } else {
//            layerHandler.replaceLayer(index, new Layer(dotRep.getResult()));
            layerHandler.getLayers().get(index).setGraphics(gr);
        }

//        image(img, 0, 0);
//        blend(img, 0, 0, img.width, img.height, 0, 0, width, height, SUBTRACT);
        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));

//        redraw();
    }

    /**
     * Makes a pixelated representation of the image with two colors.
     */
    private void pxlation(int index) {
        BigPix pxlation = new BigPix();
        pxlation.setupSketch(bgImg, pxSize);
        pxlation.init();
        pxlation.runFunction();

        if (index < 0) {
            layerHandler.addLayer(new Layer(pxlation.getResult()), "BigPix");
        } else {
            layerHandler.getLayers().get(index).setGraphics(pxlation.getResult());
        }

//        image(pxlation.getResult(), 0, 0);
        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));

//        redraw();
    }

    private void mapTo() {
        JFrame f = new JFrame();
        f.setSize(bgImg.width, bgImg.height);

        MapTo map = new MapTo();
        JPanel p = new JPanel();

        f.add(p);
        p.add(map);

        map.init();
        map.setupSketch(this.get());
//        map.function(mouseX);

        f.setVisible(true);
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

    /**
     * Assigns pointers to the undo and redo buttons.
     *
     * @param fwd The redo button.
     * @param back The undo button.
     */
    public void setButtons(JButton fwd, JButton back) {
        this.fwd = fwd;
        this.back = back;
    }

    public void setToolWindow(ToolWindow tw) {
        toolWindow = tw;
    }

    /**
     * Resets the program removing the picture and all changes in the tracker.
     */
    public void reset() {
        bgImg = null;
        tracker = new ChangeTracker();

        methodState = State.CLEAR;

        background(255);
        redraw();
    }

    /**
     * Checks if there is a change to redo.
     *
     * @return True if there is a change to redo. False if not.
     */
    public boolean changeHasNext() {
        return tracker.hasNext();
    }

    /**
     * Checks if there is a change to undo.
     *
     * @return True if there is a change to undo. False if not.
     */
    public boolean changeHasPrev() {
        return tracker.hasPrev();
    }

    /**
     * Runs a function based on the string taken as input.
     *
     * @param function Name of the function.
     */
    public void selectFunction(String function) {

        switch (function) {

            case "Dots":
                noSave = true;
                methodState = State.DOTREP;
                break;
            case "Squares":
                background(255);
                methodState = State.PXLATION;
                break;
            case "Original":
                background(255);
                methodState = State.CLEAR;
                break;
            case "3D":
                methodState = State.MAPTO;
        }
        redraw();
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
                background(255);
                redraw();
                methodState = State.CLEAR;

                break;
            case "pxlate":
                background(255);
                methodState = State.PXLATION;
                redraw();
                break;
            case "3D":
                noSave = false;
                methodState = State.MAPTO;
                redraw();
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
                methodState = State.CLONE;
                break;
            case "setPoints":
                cursor(NORMAL);
                copying = false;
                methodState = State.SETPOINTS;
                break;
        }

    }

    public void cloneRadChanged(int newR) {
        cloneRadius = newR;
    }

    /**
     * Changes the variable pxSize with a value based on the slider.
     *
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        source = (JSlider) e.getSource();
        cloneSource = (JSlider) e.getSource();

        if (!source.getValueIsAdjusting()) {
            pxSize = source.getValue();
            redraw();
            return;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        redraw();
    }
}
