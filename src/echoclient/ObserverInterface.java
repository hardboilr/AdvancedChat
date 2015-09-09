package echoclient;

import java.util.HashMap;

/**
 * @author Sebastian Nielsen Jonas Rafn Tobias Jacobsen
 */
public interface ObserverInterface {
    
    public void updateUserlist(HashMap users);
    
    public void updateMessages(HashMap message);
    
    
}
