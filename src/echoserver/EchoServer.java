package echoserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

/**
 * @author Tobias Jacobsen Sebastian Nielsen Jonas Rafn
 */
public class EchoServer {

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = Utils.initProperties("server.properties");

    private List<ClientHandler> clientHandlerList = new ArrayList();

    public static void stopServer() {
        keepRunning = false;
    }

    private void handleClient(Socket socket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(socket, this);
        clientHandler.start();
    }

    private void runServer() {
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");
        Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, "Sever started. Listening on: " + port + ", bound to: " + ip);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, "Connected to a client");
                handleClient(socket);
            } while (keepRunning);
            System.out.println("Nu stopper serveren...");
        } catch (IOException ex) {
            Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addHandler(ClientHandler clienthandler) {
        clientHandlerList.add(clienthandler);
    }

    public void removeHandler(ClientHandler ch) {
        clientHandlerList.remove(ch);
    }

    public void send(Map<String, MessagePackage> receivers) {
        MessagePackage messagepackage;
        if (receivers.containsKey("*")) {
            for (ClientHandler clientHandler : clientHandlerList) {
                messagepackage = receivers.get("*");
                clientHandler.send("MSG#" + messagepackage.getSender() + "#" + messagepackage.getMessage());
                Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, "Returned to receiver(s): MSG#" + messagepackage.getSender() + "#" + messagepackage.getMessage());
            }
        } else {
            for (ClientHandler clientHandler : clientHandlerList) {
                if (receivers.containsKey(clientHandler.getUsername())) {
                    messagepackage = receivers.get(clientHandler.getUsername());
                    clientHandler.send("MSG#" + messagepackage.getSender() + "#" + messagepackage.getMessage());
                    Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, "Returned to receiver(s):MSG#" + messagepackage.getSender() + "#" + messagepackage.getMessage());
                }
            }
        }
    }

    public void updateUserList() {
        List<String> list = new ArrayList();
        //add all users to list
        for (ClientHandler clientHandler : clientHandlerList) {
            list.add(clientHandler.getUsername());
        }
        
        //add list to all users
        for (ClientHandler clientHandler : clientHandlerList) {
            clientHandler.sendUserList(list);
        }
        Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, " an updated userlist was send to client(s)");
    }

    public static void main(String[] args) {
        try {
            String logFile = properties.getProperty("logFile");
            Utils.setLogFile(logFile, EchoServer.class.getName());
            new EchoServer().runServer();
        } finally {
            Utils.closeLogger(EchoServer.class.getName());
        }
    }
}
