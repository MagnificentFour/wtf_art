/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing_test;

import ij.ImagePlus;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.Clock;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author nikla_000
 */
public class DisplayFrame extends JFrame implements ActionListener {

    processing.core.PApplet sketch;
    JButton fileChooseButton;
    JButton button;
    JButton clearButton;
    ImagePlus img;

    public DisplayFrame() {
        this.setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        button = new JButton("Start");
        fileChooseButton = new JButton("Velg fil");
        clearButton = new JButton("Clear Canvas");

        setLayout(new FlowLayout());
        panel.setBounds(20, 20, 600, 600);
        sketch = new CircleSketch();
        panel.add(sketch);
        this.add(panel);
        this.add(button);
        this.add(fileChooseButton);
        this.add(clearButton);

        button.addActionListener(this);
        fileChooseButton.addActionListener(this);
        clearButton.addActionListener(this);

        sketch.init(); //this is the function used to start the execution of the sketch
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            sketch.start();
            try {
            img.show();
            } catch (Exception imgError) {
                System.out.println("Fuck you " + imgError);
            }
        } else if (e.getSource() == fileChooseButton) {
            chooseFile();
        } else if (e.getSource() == clearButton) {
            sketch.clear();
        }
    }

    public void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG, GIF & PNG", "jpg", "gif", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            img = new ImagePlus(chooser.getSelectedFile().getAbsolutePath());
        }
    }
}
