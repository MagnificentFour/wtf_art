package processing_test;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author nikla_000
 */
public class LayerViewItem extends JPanel {

    private ImageIcon layerIcon;
    private final Box hBox;
    private final JButton b;
    private final JCheckBox checkBox;
    private final JLabel iconLabel;
    private Integer layerNum;

    /**
     * Constructor for instances of LayerView. Creates a JPanel witch shows
     * a layer with layer number, icon, check box for show and remove button.
     *
     * @param layerIcon An icon showing the current look of the layer
     * @param button    A button to remove the layer.
     * @param layerNum  The number of the layer, 1 being the background.
     * @param checkBox  A check box which toggles whether or not the layer is viewed.
     */
    public LayerViewItem(ImageIcon layerIcon, JButton button,
                     int layerNum, JCheckBox checkBox) {
        this.layerNum = layerNum;
        this.checkBox = checkBox;
        b = button;
        iconLabel = new JLabel();
        hBox = Box.createHorizontalBox();

        setLayerIcon(layerIcon);

        addComponents();
    }

    /**
     * Adds all the components to their container.
     */
    private void addComponents() {

        hBox.add(new JLabel(layerNum.toString()));
        hBox.add(Box.createHorizontalStrut(3));
        hBox.add(iconLabel);
        hBox.add(checkBox);
        hBox.add(b);
        hBox.add(Box.createVerticalStrut(100));

        add(hBox);

    }

    /**
     * Updates the icon.
     *
     * @param icon New icon representing the layer.
     */
    public void setLayerIcon(ImageIcon icon) {
        layerIcon = icon;
        iconLabel.setIcon(layerIcon);
    }


}
