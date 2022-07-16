package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionSingleton {

  private static final String USERNAME = System.getenv("dbUsername");
  private static final String PASSWORD = System.getenv("dbPassword");
  private static final String URL = System.getenv("dbUrl");
  private static Connection conn;

  public static Connection getConn() throws SQLException {
    if (conn != null) return conn;

    conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    return conn;
  }

  public static void closeConn() throws SQLException {
    if (!conn.isClosed()) conn.close();
  }

}
