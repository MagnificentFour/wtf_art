package processing_test;

/**
 *
 * @author nikla_000
 */
public enum State {
    
    DOTREP("Dotrep"), PXLATION("Pxlation"), CLEAR("Clear"), MAPTO("3D"), 
    IMPORT("Import"), NOACTION("No action"), SETPOINTS("setPoints"), CLONE("Clone");
    
    public String state;
    
    State(String state) {
        this.state = state;
    }
    
    public String getState() {
        return state;
    }
    
}
