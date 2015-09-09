package echoclient;

import java.util.HashMap;

/**
 * @author Sebastian Nielsen Jonas Rafn Tobias Jacobsen
 */
public interface ObserverInterface {

    public void updateUserlist(HashMap<String, String> users);

    public void updateMessages(HashMap<String, String> message);

}
