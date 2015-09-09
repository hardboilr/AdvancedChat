package shared;

import echoserver.ClientHandler;
import echoserver.MessagePackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Tobias Jacobsen
 */
public class ParseCommands {

    public String parseUser(String input) {
        return input.substring(input.indexOf("#") + 1);
    }

    public Map parseClientMessage(String input, String userName) {
        Map<String, MessagePackage> map = new HashMap();
        String remaining = input.substring(input.indexOf("#") + 1);
        String names = remaining.substring(0, remaining.indexOf("#"));
        String message = remaining.substring(remaining.indexOf("#") + 1);
        try (Scanner scan = new Scanner(names)) {
            scan.useDelimiter(",");
            MessagePackage messagePackage = new MessagePackage();
            messagePackage.setMessage(message);
            messagePackage.setSender(userName);
            while (scan.hasNext()) {
                String name = scan.next();
                map.put(name, messagePackage);
            }
            //Experimental!!
            // we put "ourselves" into the map,
            //to get the message in our own client as well
            map.put(userName, messagePackage);
        }
        return map;
    }

    public Map parseServerMessage(String input) {
        Map<String, String> map = new HashMap();
        Scanner scan;
        String command = input.substring(0, (input.indexOf("#")-1));
        if (command.equals("USERLIST")) {
            String users = input.substring(input.lastIndexOf("#")+1);
            scan = new Scanner(users);
            scan.useDelimiter(",");
            String message = "USER#";
            while (scan.hasNext()) {
                String name = scan.next();
                map.put(name, message);
            }
        }    
//        } else{
//            String message = input.substring(input.lastIndexOf("#") + 1);
//            System.out.println("mesa");
//            String sender = input.substring(input.indexOf("#")+1, input.lastIndexOf("#"));
//            map.put(sender, message);
//        }
//        
            
        return map;
    }

}
