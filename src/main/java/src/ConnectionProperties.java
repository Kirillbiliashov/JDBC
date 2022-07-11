package src;

public final class ConnectionProperties {
  public static final String USERNAME = System.getenv("dbUsername");
  public static final String PASSWORD = System.getenv("dbPassword");
  public static final String URL = System.getenv("dbUrl");
}
