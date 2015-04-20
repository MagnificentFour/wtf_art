package processing_test;

import java.util.ArrayList;

/**
 *
 * @author nikla_000
 */
public class LayerHandler {

    private final ArrayList<Layer> layers;
    private boolean hasDotting;
    private int dottingIndex;
    private int bigPixIndex;
    private boolean hasBigPix;

    public LayerHandler() {
        layers = new ArrayList<>();
        hasDotting = false;
        hasBigPix = false;
        dottingIndex = -1;
        bigPixIndex = -1;
    }

    /**
     *
     * @param layer
     */
    public void setBackground(Layer layer) {

        layer.isBackground(true);

        if (layers.size() < 1) {
            layers.add(layer);
        } else {
            layers.remove(0);
            layers.add(0, layer);
        }

    }

    /**
     *
     * @param layer
     */
    public void addLayer(Layer layer) {

        layers.add(layer);

    }

    /**
     *
     * @param layer
     * @param func
     */
    public void addLayer(Layer layer, String func) {
        
        layers.add(layer);

        switch (func) {
            case "BigPix":
                bigPixIndex = layers.indexOf(layer);
                break;
            case "Dotting":
                dottingIndex = layers.indexOf(layer);
        }

        setFuncStat(func, true);
    }

    /**
     *
     * @param layer
     */
    public void removeLayer(Layer layer) {

        layer.remove(true);
        layers.remove(layer);

    }

    /**
     *
     * @param index
     */
    public void removeLayer(int index) {

        layers.get(index).remove(true);
        layers.remove(index);

    }

    /**
     *
     * @param index
     * @param layer
     */
    public boolean replaceLayer(int index, Layer layer) {

        if (index < 0 || index > layers.size()) {
            return false;
        } else {
            layers.get(index).remove(true);
            layers.remove(index);
            layers.add(index, layer);
            return true;
        }

    }

    /**
     *
     * @param layerToReplace
     * @param replacementLayer
     * @return
     */
    public boolean replaceLayer(Layer layerToReplace, Layer replacementLayer) {

        int index = 0;

        if (layers.contains(layerToReplace)) {
            index = layers.indexOf(layerToReplace);
            layerToReplace.remove(true);
            layers.remove(layerToReplace);
        } else {
            return false;
        }

        layers.add(index, replacementLayer);
        return true;
    }

    /**
     *
     * @return
     */
    public Layer getBackgroud() {

        return layers.get(0);

    }

    public ArrayList<Layer> getLayers() {

        return layers;

    }

    public void setFuncStat(String func, boolean stat) {

        switch (func) {

            case "Dotting":
                hasDotting = true;
                break;
            case "BigPix":
                hasBigPix = true;
        }

    }

    public int checkFuncStat(String func) {

        switch (func) {

            case "Dotting":
                return dottingIndex;
            case "BigPix":
                return bigPixIndex;
        }

        return -1;

    }
}
