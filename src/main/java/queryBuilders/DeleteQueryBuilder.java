package queryBuilders;

import java.sql.*;

public final class DeleteQueryBuilder extends QueryBuilder<Integer> {
  public DeleteQueryBuilder(final String tableName) {
    super(tableName);
  }

  protected String getStatement() {
    return "DELETE FROM " + this.tableName + this.constraintsSB;
  }

  protected Integer getOperationRes(Statement stmt, String queryStr)
      throws SQLException {
    return stmt.executeUpdate(queryStr);
  }
}
