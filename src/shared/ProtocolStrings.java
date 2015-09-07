package shared;

/**
 * @author Lars Mortensen
 */
public class ProtocolStrings {
  public static final String STOP = "##STOP##";
  
  public static final String Connect = "USER#";
  
  public String connect(String input) {
      return "USER#{" + input + "}";
  }
  
  public String msg(String input) {
      return ""; 
  }
}
