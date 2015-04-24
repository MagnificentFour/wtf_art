package processing_test;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;
 
/* ColorChooserDemo.java requires no other files. */
public class ColorChooserDemo extends JPanel implements ChangeListener {
 
    protected JColorChooser tcc;
    protected JLabel banner;
    Color color = Color.yellow;
    public ColorChooserDemo() {
        super(new BorderLayout());
        banner = new JLabel("Welcome to the Tutorial Zone!",
                           JLabel.CENTER);
        banner.setForeground(Color.yellow);
        tcc = new JColorChooser(banner.getForeground());
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder(
                                             "Choose Text Color"));
 
        add(tcc, BorderLayout.CENTER);
    }
 
    public void stateChanged(ChangeEvent e) {
        Color newColor = tcc.getColor();
        color = tcc.getColor();
        banner.setForeground(newColor);
    }
    
    public Color getColor() {
        return color;
    }
    
    public JColorChooser getTCC() {
        return tcc;
    }
}