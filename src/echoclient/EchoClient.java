package echoclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private BufferedReader input;
    private PrintWriter output;
    private String msg = "";
    private ParseCommands parseCommands;
    private List<ObserverInterface> observers;

    public EchoClient() {
        parseCommands = new ParseCommands();
        observers = new ArrayList<>();
    }

    public void connect(String address, int port) throws UnknownHostException, IOException {
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        run();
    }

    public void send(String msg) {
        output.println(msg);
    }

    public void disconnect() {
        output.println(ProtocolStrings.STOP);
    }

    public void addObserver(ObserverInterface observer) {
        observers.add(observer);
    }

    public void notifyObservers(HashMap<String, String> msg) {
        if (msg.containsValue("USER#")) {
            for (ObserverInterface observerinterface : observers) {
                observerinterface.updateUserlist(msg);
            }
        } else {
            for (ObserverInterface observerinterface : observers) {
                observerinterface.updateMessages(msg);
            }
        }

    }

    @Override
    public void run() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap();
                while (true) {
                    try {
                        msg = input.readLine();
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
                    } catch (IOException ex) {
                        Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();
    }

    public BufferedReader getInput() {
        return input;
    }

}
