package processing_test;

/**
 *
 * @author nikla_000
 */
public enum State {
    
    DOTREP("Dotrep"), PXLATION("Pxlation"), CLEAR("Clear"), IMPORT("Import");
    
    public String state;
    
    State(String state) {
        this.state = state;
    }
    
    public String getState() {
        return state;
    }
    
}