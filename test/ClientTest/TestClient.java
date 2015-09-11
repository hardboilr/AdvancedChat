package ClientTest;

import echoclient.EchoClient;
import echoserver.EchoServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestClient {

    /**
     * Tests Client's and Server's communication methods OBS. comment "run();"
     * in EchoClient -> Connect(..) before running the tests
     */
    public TestClient() {
    }

    @BeforeClass
    public static void setUpClass() throws IOException, InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EchoServer.main(null);
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        EchoServer.stopServer();
    }

    @Test
    public void TestA__sendUsernameAndReceiveUserlist() throws IOException, InterruptedException {
        Thread.sleep(3000);
        EchoClient tester = new EchoClient();
        Thread t1 = new Thread(tester);
        t1.start();
        t1.sleep(1000);
        tester.connect("localhost", 9090);
        tester.send("USER#Sebastian");
        BufferedReader input = tester.getInput();
        assertEquals("USERLIST#Sebastian", input.readLine());
        tester.disconnect();
    }

    @Test
    public void TestB_receiveNewUserListWhenClientsDisconnect() throws IOException, InterruptedException {
        Thread.sleep(3000);
        EchoClient tester1 = new EchoClient();
        EchoClient tester2 = new EchoClient();

        Thread t1 = new Thread(tester1);
        Thread t2 = new Thread(tester2);

        t1.start();
        t2.start();

        t1.sleep(500);
        t2.sleep(500);

        tester1.connect("localhost", 9090);
        tester2.connect("localhost", 9090);
        tester1.send("USER#Jonas");
        tester2.send("USER#Sebastian");
        
        Thread.sleep(500);

        BufferedReader input1 = tester1.getInput();
        BufferedReader input2 = tester2.getInput();

        assertEquals("USERLIST#Jonas,Sebastian", input2.readLine());

        tester1.disconnect();
        t2.sleep(1000);
        assertEquals("USERLIST#Sebastian", input2.readLine());
        tester2.disconnect();
    }

    @Test
    public void TestC_sendMsgToOneClient() throws IOException, InterruptedException {
        Thread.sleep(3000);
        EchoClient tester1 = new EchoClient();
        EchoClient tester2 = new EchoClient();

        Thread t1 = new Thread(tester1);
        Thread t2 = new Thread(tester2);

        t1.start();
        t2.start();

        t1.sleep(500);
        t2.sleep(500);

        tester1.connect("localhost", 9090);
        tester2.connect("localhost", 9090);

        tester1.send("USER#Jonas");
        tester2.send("USER#Sebastian");

        BufferedReader input1 = tester1.getInput();
        BufferedReader input2 = tester2.getInput();

        String userlist = input2.readLine();
        assertEquals("USERLIST#Jonas,Sebastian", userlist);

        tester1.send("MSG#Sebastian#Hey");
        String msg = input2.readLine();

        assertEquals("Got this: " + msg, "MSG#Jonas#Hey", msg);

        tester1.disconnect();
        tester2.disconnect();
    }

    @Test
    public void TestD_sendMsgToTwoClients() throws IOException, InterruptedException {
        Thread.sleep(3000);
        EchoClient tester1 = new EchoClient();
        EchoClient tester2 = new EchoClient();
        EchoClient tester3 = new EchoClient();

        Thread t1 = new Thread(tester1);
        Thread t2 = new Thread(tester2);
        Thread t3 = new Thread(tester3);

        t1.start();
        t2.start();
        t3.start();

        t1.sleep(200);
        t2.sleep(200);
        t3.sleep(200);

        tester1.connect("localhost", 9090);
        tester2.connect("localhost", 9090);
        tester3.connect("localhost", 9090);

        BufferedReader input1 = tester1.getInput();
        BufferedReader input2 = tester2.getInput();
        BufferedReader input3 = tester3.getInput();

        tester1.send("USER#Jonas");
        tester2.send("USER#Sebastian");
        tester3.send("USER#Tobias");

        Thread.sleep(500);
        
        String userlistForTester2 = input2.readLine();
        userlistForTester2 = input2.readLine();
        String userlistForTester3 = input3.readLine();

        tester1.send("MSG#Sebastian,Tobias#Hey");

        Thread.sleep(500);
        
        assertEquals("MSG#Jonas#Hey", input2.readLine());
        assertEquals("MSG#Jonas#Hey", input3.readLine());

        tester1.disconnect();
        tester2.disconnect();
        tester3.disconnect();
    }

    @Test
    public void TestE_sendMsgToAllClients() throws IOException, InterruptedException {
        Thread.sleep(3000);
        EchoClient jonas = new EchoClient();
        EchoClient sebastian = new EchoClient();
        EchoClient tobias = new EchoClient();

        Thread t1 = new Thread(jonas);
        Thread t2 = new Thread(sebastian);
        Thread t3 = new Thread(tobias);

        t1.start();
        t2.start();
        t3.start();

        t1.sleep(200);
        t2.sleep(200);
        t3.sleep(200);

        jonas.connect("localhost", 9090);
        sebastian.connect("localhost", 9090);
        tobias.connect("localhost", 9090);

        BufferedReader input1 = jonas.getInput();
        BufferedReader input2 = sebastian.getInput();
        BufferedReader input3 = tobias.getInput();

        jonas.send("USER#Jonas"); //line 1
        Thread.sleep(500);
        sebastian.send("USER#Sebastian"); //line 2
        Thread.sleep(500);
        tobias.send("USER#Tobias"); //line 3
        Thread.sleep(500);
        
        String userListForSebastian = input2.readLine(); //
        Thread.sleep(500);
        userListForSebastian = input2.readLine();
        
        String userlistForTobias = input3.readLine();

        jonas.send("MSG#*#Hey");
        
        Thread.sleep(500);
        
        assertEquals("MSG#Jonas#Hey", input2.readLine());
        assertEquals("MSG#Jonas#Hey", input3.readLine());

        jonas.disconnect();
        sebastian.disconnect();
        tobias.disconnect();
    }
}
