package processing_test;

import java.util.ArrayList;

/**
 * Layerhandler
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
     * TODO Document
     *
     * @param layer
     */
    public void setBackground(Layer layer) {

        layer.isBackground(true);

        if (layers.size() < 1) {
            layers.add(layer);
        } else {
            Layer bgLayer = layers.get(0);
            bgLayer.setGraphics(layer.getGraphics());
        }

    }

    /**
     * TODO Document
     *
     * @param layer
     */
    public void addLayer(Layer layer) {
        if (layers.isEmpty()) {
            layers.add(layer);
        } else {
            layers.add(layers.size() - 2, layer);
        }
    }

    /**
     * Adds a cursorlayer
     *
     * @param layer The layer in which the cursor shall be used
     */
    public void addCursorLayer(Layer layer) {
        layers.add(layer);
    }

    /**
     * TODO Document ples
     *
     * @param layer
     * @param func
     */
    public void addLayer(Layer layer, String func) {

        addLayer(layer);

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
     * TODO Document ples
     *
     * @param layer
     */
    public void removeLayer(Layer layer) {

        if (layers.indexOf(layer) == dottingIndex) {
            removeFunc("Dotting");
        } else if (layers.indexOf(layer) == bigPixIndex) {
            removeFunc("BigPix");
        } else {
            layer.remove(true);
            layers.remove(layer);
        }
    }

    /**
     * TODO Document ples
     *
     * @param index
     */
    public void removeLayer(int index) {
        layers.get(index).remove(true);
        layers.remove(index);
    }

    /**
     * TODO Document ples
     *
     * @param func
     */
    public void removeFunc(String func) {
        switch (func) {
            case "BigPix":
                layers.remove(bigPixIndex);
                bigPixIndex = -1;
                break;
            case "Dotting":
                layers.remove(dottingIndex);
                dottingIndex = -1;
        }
        setFuncStat(func, false);
    }

    /**
     * TODO Document ples
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
     * TODO Document ples
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
     * TODO Document ples
     *
     * @return
     */
    public Layer getBackgroud() {

        return layers.get(0);

    }

    /**
     * TODO Document ples
     *
     * @return
     */
    public ArrayList<Layer> getLayers() {

        return layers;

    }

    /**
     * TODO Document ples
     *
     * @param func
     * @param stat
     */
    public void setFuncStat(String func, boolean stat) {

        switch (func) {

            case "Dotting":
                hasDotting = true;
                break;
            case "BigPix":
                hasBigPix = true;
        }

    }

    /**
     * TODO Document ples
     *
     * @param func
     * @return
     */
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
