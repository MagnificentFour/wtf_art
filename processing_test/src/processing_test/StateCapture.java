package processing_test;

import processing.core.PImage;

/**
 *
 * @author nikla_000
 */
public class StateCapture {
    
    private final PImage displayWindow; 
    private final State runState;
    private final int pxSize;
    
    public StateCapture(PImage currentWindow, State currentSate, int pxSize) {
        displayWindow = currentWindow;
        runState = currentSate;
        this.pxSize = pxSize;
    }

    public PImage getDisplayWindow() {
        return displayWindow;
    }

    public State getRunState() {
        return runState;
    }

    public int getPxSize() {
        return pxSize;
    }
    
    
}
