package processing_test;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;
 
/* ColorChooserDemo.java requires no other files. */
public class ColorChooserDemo extends JPanel
                              implements ChangeListener {
 
    protected JColorChooser tcc;
    protected JLabel banner;
    Color color = Color.yellow;
    public ColorChooserDemo() {
        super(new BorderLayout());
 
        //Set up the banner at the top of the window
        banner = new JLabel("Welcome to the Tutorial Zone!",
                           JLabel.CENTER);
        banner.setForeground(Color.yellow);
//        banner.setBackground(Color.blue);
//        banner.setOpaque(true);
//        banner.setFont(new Font("SansSerif", Font.BOLD, 24));
//        banner.setPreferredSize(new Dimension(200, 65));
// 
//        JPanel bannerPanel = new JPanel(new BorderLayout());
        //bannerPanel.add(banner, BorderLayout.CENTER);
        //bannerPanel.setBorder(BorderFactory.createTitledBorder("Banner"));
 
        //Set up color chooser for setting text color
        tcc = new JColorChooser(banner.getForeground());
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder(
                                             "Choose Text Color"));
 
        //add(bannerPanel, BorderLayout.CENTER);
        add(tcc, BorderLayout.PAGE_START);
    }
 
    public void stateChanged(ChangeEvent e) {
        Color newColor = tcc.getColor();
        color = tcc.getColor();
        banner.setForeground(newColor);
    }
    
    public Color getColor() {
        return color;
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
}