/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processing_test;

import java.awt.MouseInfo;
import java.awt.Point;

/**
 *
 * @author vidar
 */
public class CloneTool {
    private Point p1; //first reference point
    private Point p2; //2nd reference point
    
    public CloneTool() {

    }
    
    /**
     * Sets 1st reference point
     * 
     * @param newPoint 
     */
    public void setPoint1(Point newPoint){
        p1 = newPoint;
    }
    
    /**
     * Sets 2nd reference point
     * 
     * @param newPoint 
     */
    public void setPoint2(Point newPoint){
        p2 = newPoint;
    }
    
    /**
     * returns the first reference point
     * @return 
     */
    public Point getPoint1() {
        return p1;
    }
    
    /**
     * returns the second reference point
     * @return 
     */
    public Point getPoint2() {
        return p2;
    }
}
