package processing_test;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import processing.core.*;

/**
 * @author nikla_000
 */
public class SampleSketch extends PApplet
        implements ActionListener, ChangeListener, ItemListener {

    int sizeWidth = 1280;
    int sizeHeight = 720;
    int pxSize = 20;
    int cloneRadius = 60;
    int waitingPoint = 0;
    int figureState = 0;
    int i;

    Random rand = new Random();

    PImage bgImg;
    PImage circle;
    PImage point1;
    PImage point2;
    PImage point3;

    PGraphics pg;
    PGraphics cursorP;

    boolean noSave = false;
    boolean copying = false;

    boolean firstState = false;
    boolean hasChanged = false;

    Color currentColor;
    ColorChooserDemo cp;

    JButton fwd;
    JButton back;

    CloneTool clTool = new CloneTool();

    Layer selectedLayer;
    Layer cursorLayer;

    State methodState = State.CLEAR;
    State nextState = State.CLEAR;
    State previousState;
    State mainState = State.VIEWING;

    int cellsize = 2; // Dimensions of each cell in the grid
    int cols, rows;   // Number of columns and rows in our system

    LayerHandler layerHandler = new LayerHandler();

    JSlider source;
    JSlider cloneSource;

    ChangeTracker tracker = new ChangeTracker();
    ToolWindow toolWindow;

    @Override
    public void setup() {
        size(1280, 720);
        background(255);
        pg = createGraphics(1280, 720);
        cursorP = createGraphics(1280, 720);
        circle = loadImage("graphics/circle2.png");
        circle.resize(cloneRadius, cloneRadius);
        point1 = loadImage("graphics/point1.png");
        point2 = loadImage("graphics/point2.png");
        point3 = loadImage("graphics/point3.png");
        frameRate(25);
        initSetup();

    }

    private void initSetup() {

        PGraphics initImage = createGraphics(1280, 720);
        initImage.beginDraw();
        initImage.background(255);
        initImage.fill(0);
        initImage.textSize(32);
        initImage.textAlign(CENTER);
        initImage.text("Please select an image", width / 2, height / 2);
        initImage.endDraw();

        Layer initLayer = new Layer(initImage);
        initLayer.getRemoveButton().setEnabled(false);
        layerHandler.addLayer(initLayer);
//        toolWindow.addLayerView(initLayer);
        initLayer.isDisplayed(true);
        initLayer.selected(true);
        
        cursorLayer = new Layer(cursorP);
        cursorLayer.isDisplayed(true);
        layerHandler.addCursorLayer(cursorLayer);
        cursorLayer.setShow(false);
        
        toolWindow.refreshLayerView(layerHandler.getLayerView());
    }

    @Override
    public void draw() {
        if (tracker.hasNext()) {
            fwd.setEnabled(true);
        } else {
            fwd.setEnabled(false);
        }

        if (tracker.hasPrev()) {
            back.setEnabled(true);
        } else {
            back.setEnabled(false);
        }

        ArrayList<Layer> rList = new ArrayList<>();

        for (Layer layer : layerHandler.getLayers()) {

            if (layer.remove()) {
                rList.add(layer);
            } else {

                if (layer.isBackground()) {
                    image(layer.getLayerImage(), 0, 0);
                } else if (layer.show()) {
                    for (PGraphics graphic : layer.getAllGraphics()) {
                        image(graphic, 0, 0);
                    }
                }
                if (layer.selected()) {
                    selectedLayer = layer;
                }
            }
        }

        for (Layer layer : rList) {
            layerHandler.removeLayer(layer);
        }

        if (!layerHandler.getLayers().isEmpty()) {//bgImg != null) {
            if (mainState == State.EDITING) {
                if (methodState == State.DOTREP) {
                    dotRep(layerHandler.checkFuncStat("Dotting"));
                } else if (methodState == State.PXLATION) {
                    pxlation(layerHandler.checkFuncStat("BigPix"));
                } else if (methodState == State.CLEAR) {
                    //image(layerHandler.getLayers().get(0).getLayerImage(), 0, 0);
                    tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
                } else if (methodState == State.MAPTO) {
                    mapTo();
                } else if (methodState == State.SETPOINTS) {
                    textFont(createFont("Arial", 16, true), 16);
                    fill(0);
                    if (waitingPoint == 0) {
                        text("Set 1st reference point", mouseX, mouseY);
                    } else {
                        text("Set 2nd reference point", mouseX, mouseY);
                    }
                } else if (methodState == State.IMPORT) {
                    methodState = nextState;
                } else if (methodState == State.CLONE) {
                    cursorLayer.setShow(true);
                    cursorP.beginDraw();
                    cursorP.background(0, 0);
                    cursorP.image(circle, mouseX - (cloneRadius / 2), mouseY - (cloneRadius / 2));
                    Point ppoint1 = clTool.getPoint1();
                    Point ppoint2 = clTool.getPoint2();
                    int p1x = ppoint1.x;
                    int p1y = ppoint1.y;
                    int p2x = ppoint2.x;
                    int p2y = ppoint2.y;
                    cursorP.image(point1, p1x, p1y);
                    cursorP.image(point2, p2x, p2y);
                    cursorP.image(point3, mouseX + (p1x - p2x), mouseY + (p1y - p2y));
                    cursorP.endDraw();
                } else if (methodState == State.BLUR) {
                    cursorLayer.setShow(true);
                    cursorP.beginDraw();
                    cursorP.background(0, 0);
                    cursorP.image(circle, mouseX - (cloneRadius / 2), mouseY - (cloneRadius / 2));
                    cursorP.endDraw();
                }

            }
            drawFunc(selectedLayer.getGraphics());
        }
        drawFunc(selectedLayer.getGraphics());
    }

    public void mouseClicked() {
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
        }
    }

    /**
     * Loads and displays an image selected by the FileChooser
     *
     * @param fileIn Input image
     */
    public void loadBgImage(File fileIn) {
        bgImg = loadImage(fileIn.getAbsolutePath());
        if (bgImg.width > 720) {
            bgImg.resize(0, 720);
        }

        PGraphics gr = createGraphics(bgImg.width, bgImg.height);
        gr.beginDraw();
        gr.image(bgImg, 0, 0);
        gr.endDraw();

        layerHandler.setBackground(new Layer(gr));
        layerHandler.refreshLayerView();
        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
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
        dotRep.runFunction(cp.getColor());
        PGraphics gr = dotRep.getResult();

        Layer dotLayer = new Layer(gr);
        dotLayer.setLayerFunc(methodState);

        if (index < 0) {
            layerHandler.addLayer(dotLayer, "Dotting");
        } else {
            layerHandler.getLayers().get(index).setGraphics(gr);
        }

        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));

        hasChanged = false;
        currentColor = cp.getColor();
        mainState = State.VIEWING;
    }

    /**
     * Makes a pixelated representation of the image with two colors.
     */
    private void pxlation(int index) {
        BigPix pxlation = new BigPix();
        pxlation.setupSketch(bgImg, pxSize);
        pxlation.init();
        pxlation.runFunction();

        Layer pxlLayer = new Layer(pxlation.getResult());
        pxlLayer.setLayerFunc(methodState);

        if (index < 0) {
            layerHandler.addLayer(pxlLayer, "BigPix");
        } else {
            layerHandler.getLayers().get(index).setGraphics(pxlation.getResult());
        }

        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
        mainState = State.VIEWING;
    }

    /**
     * TODO Document this
     */
    private void mapTo() {
        JFrame f = new JFrame();
        f.setSize(bgImg.width, bgImg.height);

        MapTo map = new MapTo();
        JPanel p = new JPanel();

        f.add(p);
        p.add(map);

        map.init();
        map.setupSketch(this.get());

        f.setVisible(true);
    }

    /**
     * Takes a StateCapture and imports the parameters.
     *
     * @param state The state to be loaded.
     */
    public void importState(StateCapture state, State metState) {

        image(state.getDisplayWindow(), 0, 0);
        pxSize = state.getPxSize();
        methodState = State.IMPORT;
        nextState = state.getRunState();

        if (source != null && metState != State.INVERT) {
            source.setValue(pxSize);
        }

        redraw();
    }

    /**
     * Assigns pointers to the undo and redo buttons.
     *
     * @param fwd  The redo button.
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
                mainState = State.EDITING;
                break;
            case "Squares":
                background(255);
                methodState = State.PXLATION;
                mainState = State.EDITING;
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
            case "dot":
                noSave = true;
                methodState = State.DOTREP;
                hasChanged = true;
                loop();
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
            case "forward":
                if (tracker.hasChanged()) {
                    importState(tracker.getNextEntry(), methodState);
                }
                if (!tracker.hasNext()) {
                    fwd.setEnabled(false);
                }
                break;
            case "back":
                if (tracker.hasChanged()) {
                    importState(tracker.getPrevEntry(), methodState);
                }
                if (!tracker.hasPrev()) {
                    back.setEnabled(false);
                }
                break;
            case "clone":
                cursor(NORMAL);
                copying = false;
                methodState = State.SETPOINTS;
                mainState = State.EDITING;
                loop();
                break;
            case "setPoints":
                cursor(NORMAL);
                copying = false;
                methodState = State.SETPOINTS;
                mainState = State.EDITING;
                loop();
                break;
            case "blur":
                if (methodState != State.BLUR) {
                    previousState = methodState;
                    mainState = State.EDITING;
                    methodState = State.BLUR;
                    loop();
                    break;
                } else {
                    methodState = previousState;
                    break;
                }
            case "invert":
                if (methodState != State.INVERT) {
                    previousState = methodState;
                    noLoop();
                    firstState = true;
                    methodState = State.INVERT;
                    mainState = State.EDITING;
                    redraw();
                } else {
                    methodState = previousState;
                }
                break;
            case "wrapping":
                methodState = State.WRAPPING;
                mainState = State.EDITING;
                loop();
                break;
            case "square":
                figureState = 0;
                break;
            case "ellipse":
                figureState = 1;
                break;
            case "haze":
                figureState = 2;
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
            mainState = State.EDITING;
            methodState = selectedLayer.getLayerFunc();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        cloneSource = (JSlider) e.getSource();
        if (!cloneSource.getValueIsAdjusting()) {
            if (methodState == State.INVERT && firstState == false) {
                importState(tracker.getPrevEntry(), methodState);
                methodState = State.INVERT;
            }
            cloneRadius = cloneSource.getValue() * 3;
            circle = loadImage("graphics/circle2.png");
            circle.resize(cloneRadius, cloneRadius);
            firstState = false;
        }

    }

    public void setColorPicker(ColorChooserDemo c) {
        cp = c;
    }

    private void drawFunc(PGraphics pg) {
        pg.beginDraw();
        switch (methodState) {
            case BLUR:
                if (mousePressed == true) {
                    blurEdit(pg);
                }
                break;
            case CLONE:
                if (copying == true) {
                    if (mousePressed == true) {
                        cloneEdit(pg);
                        break;
                    }
                }
                break;
            case INVERT:
                invertEdit(pg);
                break;
        }
        pg.endDraw();
    }

    private void blurEdit(PGraphics pg) {
        int colour;
        int totalPix = 0;
        float finalR = 0;
        float finalB = 0;
        float finalG = 0;
        for (int i = (-cloneRadius / 2); i < (cloneRadius / 2); i++) {
            for (int t = (-cloneRadius / 2); t < (cloneRadius / 2); t++) {
                if (dist(mouseX, mouseY, (mouseX + i), (mouseY + t)) <= (cloneRadius / 2)) {
                    totalPix++;
                    colour = color(pg.get(mouseX + i, mouseY + t));
                    float r = red(colour);
                    float b = blue(colour);
                    float g = green(colour);
                    finalB = finalB + b;
                    finalR = finalR + r;
                    finalG = finalG + g;
                }
            }
        }
        finalB = finalB / totalPix;
        finalR = finalR / totalPix;
        finalG = finalG / totalPix;
        int c = color(finalR, finalG, finalB);
        for (int i = -cloneRadius / 2; i < cloneRadius / 2; i++) {
            for (int t = -cloneRadius / 2; t < cloneRadius / 2; t++) {
                if (dist(mouseX, mouseY, (mouseX + i), (mouseY + t)) <= (cloneRadius / 2)) {
                    pg.set(mouseX + i, mouseY + t, c);
                }
            }
        }
    }

    private void cloneEdit(PGraphics pg) {
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
                    int colour = color(pg.get((mouseX - distanceX) + i, (mouseY - distanceY) + t));
                    pg.set(mouseX + i, mouseY + t, colour);
                }
            }
        }

    }

    private void invertEdit(PGraphics pg) {
        for (int i = 1; i <= sizeWidth; i++) {
            for (int t = 1; t <= sizeHeight; t++) {
                int colour = color(get(i, t));
                float r = red(colour);
                float b = blue(colour);
                float g = green(colour);
                if (cloneRadius > 89) {
                    int c = color(g, r, r);
                    pg.set(i, t, c);
                } else if (cloneRadius > 86) {
                    int c = color(g, b, b);
                    pg.set(i, t, c);
                } else if (cloneRadius > 83) {
                    int c = color(g, r, b);
                    pg.set(i, t, c);
                } else if (cloneRadius > 80) {
                    int c = color(g, b, r);
                    pg.set(i, t, c);
                } else if (cloneRadius > 77) {
                    int c = color(b, g, g);
                    pg.set(i, t, c);
                } else if (cloneRadius > 74) {
                    int c = color(b, r, r);
                    pg.set(i, t, c);
                } else if (cloneRadius > 71) {
                    int c = color(b, g, r);
                    pg.set(i, t, c);
                } else if (cloneRadius > 68) {
                    int c = color(b, r, g);
                    pg.set(i, t, c);
                } else if (cloneRadius > 65) {
                    int c = color(r, b, b);
                    pg.set(i, t, c);
                } else if (cloneRadius > 62) {
                    int c = color(r, g, g);
                    pg.set(i, t, c);
                } else if (cloneRadius > 59) {
                    int c = color(r, b, g);
                    pg.set(i, t, c);
                } else if (cloneRadius > 56) {
                    int c = color(g, b, g);
                    pg.set(i, t, c);
                } else if (cloneRadius > 53) {
                    int c = color(r, r, r);
                    pg.set(i, t, c);
                } else if (cloneRadius > 50) {
                    int c = color(g, g, g);
                    pg.set(i, t, c);
                } else if (cloneRadius > 47) {
                    int c = color(b, b, b);
                    pg.set(i, t, c);
                } else if (cloneRadius > 44) {
                    int c = color(g, g, r);
                    pg.set(i, t, c);
                } else if (cloneRadius > 41) {
                    int c = color(g, g, b);
                    pg.set(i, t, c);
                } else if (cloneRadius > 38) {
                    int c = color(r, r, g);
                    pg.set(i, t, c);
                } else if (cloneRadius > 35) {
                    int c = color(r, r, b);
                    pg.set(i, t, c);
                } else if (cloneRadius > 32) {
                    int c = color(b, b, r);
                    pg.set(i, t, c);
                } else if (cloneRadius > 29) {
                    int c = color(b, b, g);
                    pg.set(i, t, c);
                } else if (cloneRadius > 26) {
                    int c = color(b, g, b);
                    pg.set(i, t, c);
                } else if (cloneRadius > 23) {
                    int c = color(b, r, b);
                    pg.set(i, t, c);
                } else if (cloneRadius > 20) {
                    int c = color(r, g, r);
                    pg.set(i, t, c);
                } else if (cloneRadius > 17) {
                    int c = color(r, b, r);
                    pg.set(i, t, c);
                } else if (cloneRadius > 14) {
                    int c = color(g, r, g);
                    pg.set(i, t, c);
                } else if (cloneRadius > 11) {
                    int c = color(r, g, b);
                    pg.set(i, t, c);
                }
            }
        }
    }

    private void wrappingEdit(PGraphics pg) {
        int xMid = bgImg.width / 2;
        int yMid = bgImg.height / 2;
        currentColor = cp.getColor();
        float cr = currentColor.getRed();
        float cb = currentColor.getBlue();
        float cg = currentColor.getGreen();
        int c = color(cr, cg, cb);
        for (int i = 0; i <= bgImg.width; i++) {
            for (int t = 0; t <= bgImg.height; t++) {
                if (figureState == 1) {
                    if (dist(i, t, xMid, yMid) > cloneRadius * 3) {
                        pg.set(i, t, c);
                    }
                } else if (figureState == 0) {
                    if ((i < (xMid - (cloneRadius * 3)) || i > (xMid + (cloneRadius * 3))) || ((t < (yMid - (cloneRadius * 3))) || t > (yMid + (cloneRadius * 3)))) {
                        pg.set(i, t, c);
                    }
                } else if (figureState == 2) {
                    if (dist(i, t, xMid, yMid) > cloneRadius * 3) {
                        if (dist(i, t, xMid, yMid) < cloneRadius * 6) {
                            int u = (int) dist(i, t, xMid, yMid);
                            int r = rand.nextInt(u);
                            if (r > cloneRadius) {
                                pg.set(i, t, c);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void getLayers() {
        toolWindow.refreshLayerView(layerHandler.getLayerView());
    }
}
