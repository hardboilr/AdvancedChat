/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import echoserver.ClientHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author sebastiannielsen
 */
public class ParseMsg {
    private String msg;
    private Scanner scan;
    private List<String> clients;
    
    public ParseMsg(String input){
        msg = input;
        clients = new ArrayList<>();
    }

    public void parseCmd(String input, ClientHandler client) {
        String cmd = input.substring(0, input.indexOf("#"));
        String rest = input.substring(input.indexOf("#") + 1);
        System.out.println("cmd is: " + cmd);
        System.out.println("rest is: " + rest);

//        switch (cmd) {
//            case "USER":
//                client.setUsername(input.substring(input.indexOf("#") + 1));
//                break;
//            case "MSG":
//                String names = rest.substring(0, rest.indexOf("#"));
//                scan = new Scanner(names);
//                scan.useDelimiter(",");
//                while (scan.hasNext()) {
//                    String name = scan.next();
//                    System.out.println("name is: " + name);
//                    msgRecievers.add(name);
//                    //add this client to receivers
//                    msgRecievers.add(this.username);
//                }
//                scan.close();
//                message = rest.substring(rest.indexOf("#") + 1);
//                System.out.println("message is: " + message);
//                break;
//            default:
//                System.out.println("default");
//                break;
//        }
    }

}
