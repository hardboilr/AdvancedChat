package echoserver;

import echoclient.EchoClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;

/**
 * @author Tobias Jacobsen
 */
public class ClientHandler extends Thread {

    private Scanner input;
    private PrintWriter writer;
    private Socket socket;
    private EchoServer echoserver;
    private List<String> msgRecievers;
    private String username;
    private Scanner scan;
    private String message;

    public ClientHandler(Socket socket, EchoServer echoserver) throws IOException {
        this.socket = socket;
        this.echoserver = echoserver;
        msgRecievers = new ArrayList<>();
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        message = "";
    }

    public void send(String message) {
        writer.println("MSG#" + this.username + "#" + message);
    }

    @Override
    public void run() {
        try {
            String msg = input.nextLine(); //IMPORTANT blocking call
            Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", msg));
            while (!msg.equals(ProtocolStrings.STOP)) {
                System.out.println("msg is: " + msg);
                parseCmd(msg);
                echoserver.send(msgRecievers, message);
                Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", msg.toUpperCase()));
                msg = input.nextLine(); //IMPORTANT blocking call
                msgRecievers.clear();
            }
            System.out.println("Stopped");
            writer.println(ProtocolStrings.STOP);//Echo the stop message back to the client for a nice closedown
            socket.close();
            echoserver.removeHandler(this);
            Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, "Closed a Connection");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseCmd(String input) {
        String cmd = input.substring(0, input.indexOf("#"));
        String rest = input.substring(input.indexOf("#") + 1);
        System.out.println("cmd is: " + cmd);
        System.out.println("rest is: " + rest);

        switch (cmd) {
            case "USER":
                username = input.substring(input.indexOf("#") + 1);
                System.out.println("username is: " + username);
                break;
            case "MSG":
                String names = rest.substring(0, rest.indexOf("#"));
                scan = new Scanner(names);
                scan.useDelimiter(",");
                while (scan.hasNext()) {
                    String name = scan.next();
                    System.out.println("name is: " + name);
                    msgRecievers.add(name);
                    //add this client to receivers
                    msgRecievers.add(this.username);
                }
                scan.close();
                message = rest.substring(rest.indexOf("#") + 1);
                System.out.println("message is: " + message);
                break;
            default:
                System.out.println("default");
                break;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    

}
