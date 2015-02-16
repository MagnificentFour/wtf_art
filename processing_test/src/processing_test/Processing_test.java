/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processing_test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author nikla_000
 */
public class Processing_test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //new DisplayFrame().setVisible(true);
        BufferedImage in;
        try {
            in = ImageIO.read(new File("E:/image.jpg"));
            CannyEdgeDetector ced = new CannyEdgeDetector(in, 40, 80);
            ImageIO.write(ced.filter(), "jpg", new File("E:/newimage.jpg"));
        } catch (IOException ex) {
            System.out.println("No good");
        }
    }
    
}
