package ParseCommandsTest;

import echoserver.MessagePackage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import shared.ParseCommands;

/**
 *
 * @author Jonas
 */
public class TestParseCommands {

    public TestParseCommands() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void parseUserTest() {
        for (int j = 1; j <= 100; j++) {

            char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            String username = sb.toString();

            //Uncomment line below to print usernames in console
//            System.out.println("Username " + j + " is: " + username);
            String command = "USER#" + username;

            ParseCommands pc = new ParseCommands();

            String parsed = pc.parseUser(command);

            assertEquals(parsed, username);
        }
    }

    @Test
    public void parseClientMessageTest() {

        for (int j = 0; j < 100; j++) {

            char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            String username = sb.toString();

            char[] chars1 = "abcdefghijklmnopqrstuvwxyzæøå ".toCharArray();
            StringBuilder sb1 = new StringBuilder();
            Random random1 = new Random();
            for (int i = 0; i < 20; i++) {
                char c = chars1[random1.nextInt(chars1.length)];
                sb1.append(c);
            }
            String message = sb1.toString();

            String input = "MSG#" + username + "#" + message;

            ParseCommands pc = new ParseCommands();
            Map<String, MessagePackage> map = new HashMap();
            MessagePackage mp = new MessagePackage();

            map = pc.parseClientMessage(input, username);
            mp = map.get(username);

            String pm = mp.getMessage();
            String sender = mp.getSender();

            assertEquals(sender, username);
            assertEquals(message, pm);
        }
    }

    @Test
    public void parseServerMessageTest() {
        for (int j = 0; j < 10; j++) {

            char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            String username = sb.toString();

            char[] chars1 = "abcdefghijklmnopqrstuvwxyzæøå ".toCharArray();
            StringBuilder sb1 = new StringBuilder();
            Random random1 = new Random();
            for (int i = 0; i < 20; i++) {
                char c = chars1[random1.nextInt(chars1.length)];
                sb1.append(c);
            }
            String message = sb1.toString();

            String input = "MSG#" + username + "#" + message;

            ParseCommands pc = new ParseCommands();
            Map<String, String> map = new HashMap();

            map = pc.parseServerMessage(input);

            String pm = map.get(username);
//            System.out.println("pm: " + pm);

            assertEquals(message, pm);
        }
    }

}
