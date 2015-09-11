package TestSuite;

import ClientTest.TestClient;
import ParseCommandsTest.TestParseCommands;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author tobias
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestParseCommands.class, TestClient.class})
public class TestSuite {
    
}
