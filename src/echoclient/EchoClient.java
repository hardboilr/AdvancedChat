package echoclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ParseCommands;
import shared.ProtocolStrings;

/**
 * @author Tobias Jacobsen
 */
public class EchoClient implements Runnable {

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    private String msg = "";
    private ParseCommands parseCommands;
    private List<ObserverInterface> observers;
    
    public EchoClient(){
        parseCommands = new ParseCommands();
        observers = new ArrayList<>();
    }
    

    public void connect(String address, int port) throws UnknownHostException, IOException {
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        run();
    }

    public void send(String msg) {
        output.println(msg);
    }

    public void stop() throws IOException {
        output.println(ProtocolStrings.STOP);
    }
    
    public void addObserver(ObserverInterface observer){
        observers.add(observer);
    }
    
    public void notifyObservers(HashMap<String, String> msg){
        if(msg.containsValue("USER#")){
           for(ObserverInterface observerinterface: observers){
               observerinterface.updateUserlist(msg);
            }
        } else {
            for(ObserverInterface observerinterface: observers){
               observerinterface.updateMessages(msg);
            }
        }
        
    }

    @Override
    public void run() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap <String, String> map = new HashMap();
                while (true) {
                    msg = input.nextLine();
                    if (msg.equals(ProtocolStrings.STOP)) {
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        System.out.println("Msg is: " + msg);
                        map = parseCommands.parseServerMessage(msg);
                        
                    }
                    notifyObservers(map);
                }
            }
        });
        t.start();
    }
}
