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
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import processing.core.*;

/**
 * This class contains the code that makes the buttons execute functions. Much of the processingcode is here as well.
 * Setup for the processing canvas, and the class that controls the flow of processing code in the application.
 *
 * @author nikla_000
 */
public class SampleSketch extends PApplet
        implements ActionListener, ChangeListener, ItemListener {

    int sizeWidth = 1280;
    int sizeHeight = 720;
    int pxSize = 20;
    int cloneR = 60;
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
    boolean isDrawn = false;
    boolean wrapperInit = false;
    boolean wrappingCreated = false;

    boolean firstState = false;
    boolean hasChanged = false;

    Color currentColor;
    ColorChooserDemo cp;

    JButton fwd;
    JButton back;
    JButton mapDone;
    JButton cellSize;
    JButton cellSize2;

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
    JSlider mapSlider;
    JSlider pxSlider;

    ChangeTracker tracker = new ChangeTracker();
    ToolWindow toolWindow;

    /**
     * Setup of the processing canvas and further execution of processing code
     */
    @Override
    public void setup() {
        size(1280, 720);
        background(255);
        pg = createGraphics(1280, 720);

        cursorP = createGraphics(1280, 720);
        circle = loadImage("graphics/circle2.png");
        circle.resize(cloneR, cloneR);
        point1 = loadImage("graphics/point1.png");
        point2 = loadImage("graphics/point2.png");
        point3 = loadImage("graphics/point3.png");
        frameRate(25);
        initSetup();

    }

    /**
     * Initialization of canvas and layer code
     */
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
        
        if(wrapperInit == true && wrappingCreated == false) {
            PGraphics base = selectedLayer.getGraphics();
            PGraphics gb = createGraphics(bgImg.width, bgImg.height);
            Layer wrappingLayer = new Layer(gb);
                    if (layerHandler.checkFuncStat("Wrapping") < 0) {
                        layerHandler.addLayer(wrappingLayer, "Wrapping");
                        wrappingLayer.setImage(base);
                    } else {
                        //layerHandler.editLayer(index, gr);
                    }
                    selectedLayer = wrappingLayer;
                    selectedLayer.setLayerFunc(methodState);
                    wrapperInit = false;
                    wrappingCreated = true;
        }
        
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

                switch (methodState) {

                    case DOTREP:
                        dotRep(layerHandler.checkFuncStat("Dotting"));
                        break;
                    case PXLATION:
                        pxlation(layerHandler.checkFuncStat("BigPix"));
                        break;
                    case MAPTO:
                        mapTo(layerHandler.checkFuncStat("MapTo"));
                        break;
                    case FLOP:
                        flop();
                        break;
                    case CLEAR:
                        //image(layerHandler.getLayers().get(0).getLayerImage(), 0, 0);
                        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
                        break;
                    case SETPOINTS:
                        textFont(createFont("Arial", 16, true), 16);
                        fill(0);
                        if (waitingPoint == 0) {
                            text("Set 1st reference point", mouseX, mouseY);
                        } else {
                            text("Set 2nd reference point", mouseX, mouseY);
                        }
                        break;
                    case IMPORT:
                        methodState = nextState;
                        break;
                    case CLONE:
                        cursorLayer.setShow(true);
                        cursorP.beginDraw();
                        cursorP.background(0, 0);
                        cursorP.image(circle, mouseX - (cloneR / 2), mouseY - (cloneR / 2));
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
                        break;
                    case BLUR:
                        cursorLayer.setShow(true);
                        cursorP.beginDraw();
                        cursorP.background(0, 0);
                        cursorP.image(circle, mouseX - (cloneR / 2), mouseY - (cloneR / 2));
                        cursorP.endDraw();
                        break;
                }

            }
        }
        drawFunc(selectedLayer.getGraphics());
    }

    /**
     * Checks if mouse is clicked. Used with the clonetool
     */
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
        PImage base;

        if (index < 0) {
            base = selectedLayer.getGraphics();
        } else {
            base = selectedLayer.getLayerImage();
        }

        dotRep.setupSketch(base, pxSize);
        dotRep.init();
        dotRep.runFunction(cp.getColor());

        PGraphics gr = dotRep.getResult();

        Layer dotLayer = new Layer(gr);
        dotLayer.setLayerFunc(methodState);

        if (index < 0) {
            layerHandler.addLayer(dotLayer, "Dotting");
            dotLayer.setImage(base);
        } else {
            layerHandler.editLayer(index, gr);
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
        PImage base;

        if (index < 0) {
            base = selectedLayer.getGraphics();
        } else {
            base = selectedLayer.getLayerImage();
        }

        pxlation.setupSketch(base, pxSize);
        pxlation.init();
        pxlation.runFunction();

        PGraphics gr = pxlation.getResult();

        Layer pxlLayer = new Layer(gr);

        if (index < 0) {
            layerHandler.addLayer(pxlLayer, "BigPix");
            pxlLayer.setLayerFunc(methodState);
            pxlLayer.setImage(base);
        } else {
            layerHandler.editLayer(index, gr);
        }

        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
        mainState = State.VIEWING;
    }

    /**
     * 3D skewing procesing code
     */
    private void mapTo(int index) {
        JFrame f = new JFrame();
        f.setSize(1360, 850);
        mapDone = new JButton("Done");
        cellSize = new JButton("Less detailed");
        cellSize2 = new JButton("More detailed");
        mapSlider = new JSlider(JSlider.HORIZONTAL, 1, 1280, 50);
        mapSlider.setBounds(20, 10, 15, 15);
        cellSize.setBounds(10, 10, 15, 15);
        cellSize2.setBounds(5, 10, 15, 15);
        mapDone.setBounds(1, 10, 20, 20);
        MapTo map = new MapTo();
        JPanel p = new JPanel();
        p.setSize(bgImg.width, bgImg.height);       
        p.add(mapDone);
        p.add(cellSize);
        p.add(cellSize2);
        p.add(mapSlider);
        f.add(p);
        p.add(map);

        cellSize.addActionListener(map);
        mapSlider.addChangeListener(map);
        cellSize2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                map.decreaseCell();
            }
        } );
        mapDone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                p.setVisible(false);
                image(map.function(), 0, 0);
                methodState = State.NOACTION;
                Layer pxlLayer = new Layer(map.getResult());
                pxlLayer.setLayerFunc(methodState);

                if (index < 0) {
                    layerHandler.addLayer(pxlLayer, "3D");
                } else {
                    layerHandler.getLayers().get(index).setGraphics(map.getResult());
                }
            }
        });
        map.setupSketch(this.get());
        map.init();
        f.setVisible(true);
        tracker.addChange(new StateCapture(this.get(), methodState, pxSize));
        mainState = State.VIEWING;
    }

    ArrayList<PImage> imageList = new ArrayList<>();

    /**
     * Processing code for the random flop function
     */
    public void flop() {
        PGraphics dis = createGraphics(1280, 720);
        dis.beginDraw();
        dis.image(selectedLayer.getGraphics(), 0, 0);

        for (int i = 0; i < 100; i++) {
            int randomX = (int) random(0, 1280);
            int randomY = (int) random(0, 720);

            int randomHeight = (int) random(50, 200);
            int randomWidth = (int) random(50, 200);

            imageList.add(dis.get(randomX, randomY, randomWidth, randomHeight));
        }

        for (PImage img : imageList) {
            float randomX = random(0, 1280);
            float randomY = random(0, 720);
            dis.image(img, randomX, randomY);
        }
        dis.endDraw();
        layerHandler.addLayer(new Layer(dis));
        mainState = State.VIEWING;
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
                mainState = State.EDITING;
                break;
            case "Flop":
                methodState = State.FLOP;
                mainState = State.EDITING;
        }
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
                mainState = State.EDITING;
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
                selectedLayer.setLayerFunc(methodState);
                loop();
                break;
            case "setPoints":
                cursor(NORMAL);
                copying = false;
                methodState = State.SETPOINTS;
                mainState = State.EDITING;
                selectedLayer.setLayerFunc(methodState);
                loop();
                break;
            case "blur":
                if (methodState != State.BLUR) {
                    previousState = methodState;
                    mainState = State.EDITING;
                    methodState = State.BLUR;
                    selectedLayer.setLayerFunc(methodState);
                    loop();
                    break;
                } else {
                    methodState = previousState;
                    break;
                }
            case "invert":
                if (methodState != State.INVERT) {
                    //previousState = methodState;
                    selectedLayer = layerHandler.getLayers().get(0);
                    firstState = true;
                    methodState = State.INVERT;
                    mainState = State.EDITING;
                    selectedLayer.setLayerFunc(methodState);
                } else {
                    //methodState = previousState;
                }
                break;
            case "wrapping":
                if (methodState != State.WRAPPING) {
                    
                   
                    wrapperInit = true;
                    methodState = State.WRAPPING;
                    mainState = State.EDITING;
                    
                    loop();
                }
                break;
            case "square":
                figureState = 0;
                mainState = State.EDITING;
                break;
            case "ellipse":
                figureState = 1;
                mainState = State.EDITING;
                break;
            case "haze":
                figureState = 2;
                mainState = State.EDITING;
                break;
            case "randomFucks":
                System.out.println("(!) Randomfucks are given");
                int max = 1;
                int min = 0;
                int randInt = rand.nextInt((max - min) + 1) + min;
                ArrayList<String> randFunc = new ArrayList<>(); //TODO Her trengs mindre skitten kode..........
                randFunc.add("Dots");
                randFunc.add("Squares");

                System.out.println(randFunc.get(randInt));
                selectFunction(randFunc.get(randInt));
                break;
        }

    }

    public void cloneRadChanged(int newR) {
        cloneR = newR;
    }

    /**
     * Changes the variable pxSize with a value based on the slider.
     *
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        
        
        if (e.getSource() instanceof JSlider) {
            source = (JSlider) e.getSource();
            cloneSource = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                cloneR = source.getValue() * 3;
                pxSize = source.getValue();

                methodState = selectedLayer.getLayerFunc();
//            if (methodState == State.INVERT && firstState == false) {
//                methodState = State.INVERT;
//            }
                isDrawn = false;
                circle = loadImage("graphics/circle2.png");
                circle.resize(cloneR, cloneR);
                //firstState = false;
                System.out.println(cloneR);
                System.out.println(methodState);
            }
        }
        mainState = State.EDITING;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        cloneSource = (JSlider) e.getSource();
        if (!cloneSource.getValueIsAdjusting()) {

        }

    }

    public void setColorPicker(ColorChooserDemo c) {
        cp = c;
    }

    private void drawFunc(PGraphics pg) {
        pg.beginDraw();
        if (methodState != null) {
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
                case WRAPPING:
                    wrappingEdit(pg);

            }
        }
        pg.endDraw();
    }

    /**
     * Processing code for the blurfunction
     *
     * @param pg picturegraphics
     */
    private void blurEdit(PGraphics pg) {
        int colour;
        int totalPix = 0;
        float finalR = 0;
        float finalB = 0;
        float finalG = 0;
        for (int i = (-cloneR / 2); i < (cloneR / 2); i++) {
            for (int t = (-cloneR / 2); t < (cloneR / 2); t++) {
                if (dist(mouseX, mouseY, (mouseX + i), (mouseY + t)) <= (cloneR / 2)) {
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
        for (int i = -cloneR / 2; i < cloneR / 2; i++) {
            for (int t = -cloneR / 2; t < cloneR / 2; t++) {
                if (dist(mouseX, mouseY, (mouseX + i), (mouseY + t)) <= (cloneR / 2)) {
                    pg.set(mouseX + i, mouseY + t, c);
                }
            }
        }
    }

    /**
     * processing code for the clone function
     *
     * @param pg picturegraphics
     */
    private void cloneEdit(PGraphics pg) {
        Point p1 = clTool.getPoint1();
        Point p2 = clTool.getPoint2();
        int distanceX = p2.x - p1.x;
        int distanceY = p2.y - p1.y;
        //Point cp = new Point(mouseX, mouseY);
        //int colour = color(bgImg.get(mouseX - 50, mouseY));
        int staticX = mouseX;
        int staticY = mouseY;
        for (int i = -cloneR / 2; i < cloneR / 2; i++) {
            for (int t = -cloneR / 2; t < cloneR / 2; t++) {
                if (dist(mouseX, mouseY, (mouseX + i), (mouseY + t)) <= (cloneR / 2)) {
                    int colour = color(pg.get((mouseX - distanceX) + i, (mouseY - distanceY) + t));
                    pg.set(mouseX + i, mouseY + t, colour);
                }
            }
        }

    }

    /**
     * Processing code for the invertColors functino
     *
     * @param pg picturegraphics
     */
    private void invertEdit(PGraphics pg) {
        boolean fuckTest = false;
        PGraphics gb = createGraphics(bgImg.width, bgImg.height);
        pg.endDraw();
        gb.beginDraw();
        gb.image(bgImg, 0, 0);
        gb.endDraw();
        pg.beginDraw();
        System.out.println("CLonme:" + cloneR);
        for (int x = 1; x <= gb.width; x++) {
            for (int t = 1; t <= gb.height; t++) {
                int colour = color(gb.get(x, t));
                float r = red(colour);
                float b = blue(colour);
                float gr = green(colour);
                if (cloneR > 89) {
                    int c = color(gr, r, r);
                    pg.set(x, t, c);
                } else if (cloneR > 86) {
                    int c = color(gr, b, b);
                    pg.set(x, t, c);
                } else if (cloneR > 83) {
                    int c = color(gr, r, b);
                    pg.set(x, t, c);
                } else if (cloneR > 80) {
                    int c = color(gr, b, r);
                    pg.set(x, t, c);
                } else if (cloneR > 77) {
                    int c = color(b, gr, gr);
                    pg.set(x, t, c);
                    fuckTest = true;
                } else if (cloneR > 74) {
                    int c = color(b, r, r);
                    pg.set(x, t, c);
                } else if (cloneR > 71) {
                    int c = color(b, gr, r);
                    pg.set(x, t, c);
                } else if (cloneR > 68) {
                    int c = color(b, r, gr);
                    pg.set(x, t, c);
                } else if (cloneR > 65) {
                    int c = color(r, b, b);
                    pg.set(x, t, c);
                } else if (cloneR > 62) {
                    int c = color(r, gr, gr);
                    pg.set(x, t, c);
                } else if (cloneR > 59) {
                    int c = color(r, b, gr);
                    pg.set(x, t, c);
                } else if (cloneR > 56) {
                    int c = color(gr, b, gr);
                    pg.set(x, t, c);
                } else if (cloneR > 53) {
                    int c = color(r, r, r);
                    pg.set(x, t, c);
                } else if (cloneR > 50) {
                    int c = color(gr, gr, gr);
                    pg.set(x, t, c);
                } else if (cloneR > 47) {
                    int c = color(b, b, b);
                    pg.set(x, t, c);
                } else if (cloneR > 44) {
                    int c = color(gr, gr, r);
                    pg.set(x, t, c);
                } else if (cloneR > 41) {
                    int c = color(gr, gr, b);
                    pg.set(x, t, c);
                } else if (cloneR > 38) {
                    int c = color(r, r, gr);
                    pg.set(x, t, c);
                } else if (cloneR > 35) {
                    int c = color(r, r, b);
                    pg.set(x, t, c);
                } else if (cloneR > 32) {
                    int c = color(b, b, r);
                    pg.set(x, t, c);
                } else if (cloneR > 29) {
                    int c = color(b, b, gr);
                    pg.set(x, t, c);
                } else if (cloneR > 26) {
                    int c = color(b, gr, b);
                    pg.set(x, t, c);
                } else if (cloneR > 23) {
                    int c = color(b, r, b);
                    pg.set(x, t, c);
                } else if (cloneR > 20) {
                    int c = color(r, gr, r);
                    pg.set(x, t, c);
                } else if (cloneR > 17) {
                    int c = color(r, b, r);
                    pg.set(x, t, c);
                } else if (cloneR > 14) {
                    int c = color(gr, r, gr);
                    pg.set(x, t, c);
                } else if (cloneR > 11) {
                    int c = color(r, gr, b);
                    pg.set(x, t, c);
                }
            }
        }
    }

    /**
     * Processing code for wrapping function
     *
     * @param pg picturegraphics
     */
    private void wrappingEdit(PGraphics pg) {
        if(mainState == State.EDITING){
        pg.clear();
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
                    if (dist(i, t, xMid, yMid) > cloneR * 3) {
                        pg.set(i, t, c);
                    }
                } else if (figureState == 0) {
                    if ((i < (xMid - (cloneR * 3)) || i > (xMid + (cloneR * 3))) || ((t < (yMid - (cloneR * 3))) || t > (yMid + (cloneR * 3)))) {
                        pg.set(i, t, c);
                    }
                } else if (figureState == 2) {
                    if (dist(i, t, xMid, yMid) > cloneR * 3) {
                        if (dist(i, t, xMid, yMid) < cloneR * 6) {
                            int u = (int) dist(i, t, xMid, yMid) - cloneR;
                            int r = rand.nextInt(u);
                            if (r > ((cloneR *6) - (cloneR * 3))) {
                                pg.set(i, t, c);
                            }
                        }
                    }
                    if (dist(i, t, xMid, yMid) > cloneR * 6) {
                        pg.set(i, t, c);
                    }
                }
            }
        }
        layerHandler.refreshLayerView();
        }
        mainState = State.VIEWING;
    }

    public void getLayers() {
        toolWindow.refreshLayerView(layerHandler.getLayerView());
    }
}
