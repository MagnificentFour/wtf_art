/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

/**
 *
 * @author nikla_000
 */
public class Ellipse {

    private final float xCoord;
    private final float yCoord;
    private final float width;
    private final float height;
    private float drawWidth;
    private float drawHeight;

    public Ellipse(float xCoord, float yCoord, float width, float height) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.width = width;
        this.height = height;
        drawWidth = 0;
        drawHeight = 0;
    }

    public float getxCoord() {
        return xCoord;
    }

    public float getyCoord() {
        return yCoord;
    }

    public float getWidth() {
        return drawWidth;
    }

    public float getHeight() {
        return drawHeight;
    }
    
    public boolean incrementSize() {
        if(drawWidth < width && drawHeight < height) {
            drawWidth++;
            drawHeight++;
            
            return true;
        } else {
            return false;
        }
    }

}
