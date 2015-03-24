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
    private Point p1;
    private Point p2;
    private int radius;
    public CloneTool() {
        radius = 5;
    }
    
    public void setPoint1(Point newPoint){
        p1 = newPoint;
        System.out.println(p1);

    }
    
    public void setPoint2(Point newPoint){
        p2 = newPoint;
        System.out.println(p2);

    }
    
    public void setRadius(int newRadius) {
        radius = newRadius;
    }
    
    public Point getPoint1() {
        return p1;
    }
    
    public Point getPoint2() {
        return p2;
    }
    
    public int getRadius(){
        return radius;
    }
}
