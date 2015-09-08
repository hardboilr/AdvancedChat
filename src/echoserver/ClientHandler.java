package echoserver;

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

    private List<String> users = new ArrayList<>();
    private String parsedCmd;
    private String reciever;
    private String userName;

    public ClientHandler(Socket socket, EchoServer echoserver) throws IOException {
        this.socket = socket;
        this.echoserver = echoserver;
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void send(String clientMessage) {
        int count = 0;
        for (int i = 0; i < clientMessage.length(); i++) {
            if (clientMessage.charAt(i) == '#') {
                count++;
            }
        }

        String msg = "";
        String cmd = "";
        String rcv = "";

        if (count == 1) {
            int indexCmd = clientMessage.indexOf("#");
            cmd = clientMessage.substring(1, indexCmd - 1);
            int indexMsg = clientMessage.indexOf("#");
            msg = clientMessage.substring(indexMsg + 1, clientMessage.length() - 1);
        } else if (count == 2) {
            int indexCmd = clientMessage.indexOf("#");
            int indexRcv = clientMessage.indexOf("#");
            int indexMsg = clientMessage.lastIndexOf("#");
            cmd = clientMessage.substring(1, indexCmd - 1);
            rcv = clientMessage.substring(indexRcv + 1, indexMsg - 2);
            msg = clientMessage.substring(indexMsg + 1, clientMessage.length() - 1);
        }

        switch (cmd) {
            case "USER":
                userName = msg;
               
                break;
            case "MSG":
              
                break;
            case "STOP":
                
                break;
            default:
                System.out.println("No such command");
                break;

        }

    }

    @Override
    public void run() {
        try {
            String message = input.nextLine(); //IMPORTANT blocking call
            Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
            while (!message.equals(ProtocolStrings.STOP)) {
                echoserver.send(message);
                Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
                message = input.nextLine(); //IMPORTANT blocking call
            }
            writer.println(ProtocolStrings.STOP);//Echo the stop message back to the client for a nice closedown
            socket.close();
            echoserver.removeHandler(this);
            Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, "Closed a Connection");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUserName() {
        return userName;
    }
    
    

}
