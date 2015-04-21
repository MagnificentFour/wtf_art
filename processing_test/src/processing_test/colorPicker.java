/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author vidar
 */
public class colorPicker extends JFrame {
    
    ColorChooserDemo cs;

    public colorPicker() {
        setSize(640, 288);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setAlwaysOnTop (true);
        cs = new ColorChooserDemo();
        cs.setBounds(10, 10, 620, 200);
        add(cs);
        setVisible(true);
    }
    
    public Color getColor() {
       return cs.getColor(); 
    }
}
