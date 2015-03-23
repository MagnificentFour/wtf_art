package processing_test;

/**
 *
 * @author nikla_000
 */
public enum State {
    
    DOTREP("Dotrep"), PXLATION("Pxlation"), CLEAR("Clear"), IMPORT("Import"), CLONE("Clone"), SETPOINTS("setPoints");
    
    public String state;
    
    State(String state) {
        this.state = state;
    }
    
    public String getState() {
        return state;
    }
    
}
