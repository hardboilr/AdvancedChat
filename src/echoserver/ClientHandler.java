package echoserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ParseCommands;
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
    private boolean isLoggedIn;
    private ParseCommands parseCmd;

    private List<String> users = new ArrayList<>();
    private String parsedCmd;
    private String reciever;
    private String userName;

    public ClientHandler(Socket socket, EchoServer echoserver) throws IOException {
        this.socket = socket;
        this.echoserver = echoserver;
        msgRecievers = new ArrayList<>();
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        message = "";
        parseCmd = new ParseCommands();
    }

    public void send(String message) {
        writer.println(message);
    }

    public void sendUserList(List<String> userList) {
        String message = "USERLIST#";
        for (String user : userList) {
            message += user + ",";
        }
        writer.println(message.substring(0, message.length() - 1)); //we don't want the last "," 
    }

    @Override
    public void run() {
        try {
            String msg = input.nextLine(); //IMPORTANT blocking call
            Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", msg));
            while (!msg.equals(ProtocolStrings.STOP)) {
                if (!isLoggedIn) {
                    username = parseCmd.parseUser(msg);
                    isLoggedIn = true;
                    echoserver.addHandler(this);
                    echoserver.updateUserList();
                } else {
                    echoserver.send(parseCmd.parseClientMessage(msg, username));
                }
                Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", msg.toUpperCase()));
                msg = input.nextLine(); //IMPORTANT blocking call
                msgRecievers.clear();
            }
            System.out.println("Stopped");
            writer.println(ProtocolStrings.STOP);//Echo the stop message back to the client for a nice closedown
            socket.close();
            echoserver.removeHandler(this);
            echoserver.updateUserList();
            Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, "Closed a Connection");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
