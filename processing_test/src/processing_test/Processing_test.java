/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

import ij.ImagePlus;
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
        new DisplayFrame().setVisible(true);
//        ImagePlus img = new ImagePlus("E:/image.jpg");
//        ImagePlus out = new ImagePlus();
//        img.show();
////        Float[][] points = img.getFloatArray();
//
//        BufferedImage in;
////        try {
//        in = img.getBufferedImage();
//        CannyEdgeDetector ced = new CannyEdgeDetector(in, 40, 80);
//        out.setImage(ced.filter());
//        out.show();
////            ImageIO.write(ced.filter(), "jpg", new File("E:/newimage.jpg"));
////        } catch (IOException ex) {
////            System.out.println("No good");
////        }
    }

}
