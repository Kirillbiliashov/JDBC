package src;

import java.sql.*;

import static src.ConnectionSingleton.*;

public class Helpers {

  public static void executeStatement(final String sqlStr, final String printStr)
      throws SQLException {
    try (final Statement stmt = getConn().createStatement()) {
      System.out.println(sqlStr);
      if (stmt.execute(sqlStr)) {
        System.out.println(printStr);
      }
    }
  }

}
