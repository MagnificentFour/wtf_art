package processing_test;

import java.util.ArrayList;

/**
 *
 * @author nikla_000
 */
public class ChangeTracker {

    private final ArrayList<StateCapture> changeList;
    private int currentIndex;

    public ChangeTracker() {
        changeList = new ArrayList<>();
        currentIndex = -1;
    }

    /**
     *
     *
     * @param prevState Previous state
     */
    public void addChange(StateCapture prevState) {
        changeList.add(prevState);

        if (changeList.size() > 5) {
            changeList.remove(0);
        } else if(changeList.size() > 0){
            currentIndex = changeList.size() - 1;
        }

    }

    public StateCapture getPrevEntry() {
        if (currentIndex > 0) {
            currentIndex--;
        }
        StateCapture returnObject = changeList.get(currentIndex);

        return returnObject;
    }
    
    public StateCapture getNextEntry() {
        
        if(currentIndex < changeList.size() - 1) {
            currentIndex++;
        }
        
        StateCapture returnObject = changeList.get(currentIndex);
        
        return returnObject;
    }
    
    public boolean hasNext() {
        return currentIndex < changeList.size() - 1;
    }
    
    public boolean hasPrev() {
        return currentIndex > 0;
    }
    
    public boolean hasChanged() {
        return changeList.size() > 0;
    }
}
