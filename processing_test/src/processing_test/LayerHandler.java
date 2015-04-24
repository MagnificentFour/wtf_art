package processing_test;

import java.util.ArrayList;
import processing.core.PGraphics;

/**
 * Layerhandler
 * @author nikla_000
 */
public class LayerHandler {

    private final ArrayList<Layer> layers;
    private int dottingIndex;
    private int bigPixIndex;
    private int mapIndex;
    private int wrappingIndex;
    private LayerView layerView;

    public LayerHandler() {
        layers = new ArrayList<>();
        dottingIndex = -1;
        bigPixIndex = -1;
        mapIndex = -1;
        wrappingIndex = -1;
        layerView = new LayerView();
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
            layers.add(layers.size() - 1, layer);
        }
        layerView.addLayerView(layer);
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
                break;
            case "3D":
                mapIndex = layers.indexOf(layer);
                break;
            case "Wrapping":
                wrappingIndex = layers.indexOf(layer);
                break;
        }
    }
    
    /**
     * Edits a layer and refreshing the layer view.
     * @param index Index of the edited layer.
     * @param graphic Updated graphics.
     */
    public void editLayer(int index, PGraphics graphic) {
        
        layers.get(index).setGraphics(graphic);
        refreshLayerView();
        
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
        } else if (layers.indexOf(layer) == mapIndex) {
            removeFunc("3D");
        } else if (layers.indexOf(layer) == wrappingIndex) {
            removeFunc("Wrapping");
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
                break;
            case "3D":
                layers.remove(mapIndex);
                mapIndex = -1;
                break;
            case "Wrapping":
                layers.remove(wrappingIndex);
                wrappingIndex = -1;
                break;
        }
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
     * Kommentera ut for � se om merge fungerer
     *
     * @param func
     * @param stat
     */
    public void setFuncStat(String func, boolean stat) {

        switch (func) {

            case "Dotting":
           //     hasDotting = true;
                break;
            case "BigPix":
            //    hasBigPix = true;
                break;
            case  "3D":
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
            case "3D":
                return mapIndex;
            case "Wrapping":
                return wrappingIndex;
                 
        }
        return -1;
    }
    
    public void refreshLayerView() {
        layerView.refresh();
    }
    
    public LayerView getLayerView() {
        return layerView;
    }
}
