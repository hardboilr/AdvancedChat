package shared;


public class ProtocolStrings {
  public static final String STOP = "STOP#";
  
  public static String connect(String input) {
      return "USER#{" + input + "}";
  }
  
  public String msg(String input) {
      return ""; 
  }
}
