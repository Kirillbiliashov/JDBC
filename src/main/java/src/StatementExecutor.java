package src;

import java.sql.*;

import static src.ConnectionSingleton.*;

public final class StatementExecutor {

  public static void executeStatement(final String sqlStr, final String printStr)
      throws SQLException {
    try (final Statement stmt = getConn().createStatement()) {
      if (stmt.execute(sqlStr)) {
        System.out.println(printStr);
      }
    }
  }

  public static ResultSet executeQueryStatement(final String queryStr)
      throws SQLException {
    try (final Statement stmt = getConn().createStatement(
        ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
      return stmt.executeQuery(queryStr);
    }
  }

  public static int executeUpdateStatement(final String updateStr)
      throws SQLException {
    try (final Statement stmt = getConn().createStatement()) {
      return stmt.executeUpdate(updateStr);
    }
  }

}
