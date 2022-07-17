package src;

import java.sql.*;

public final class ConnectionSingleton {

  private static final String USERNAME = System.getenv("dbUsername");
  private static final String PASSWORD = System.getenv("dbPassword");
  private static final String URL = System.getenv("dbUrl");
  private static Connection conn;

  public static Connection getConn() throws SQLException {
    if (conn == null) {
      conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    return conn;
  }

  public static void closeConn() {
    try {
      if (!conn.isClosed()) conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
