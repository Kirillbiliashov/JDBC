package queryBuilders;

import static src.StatementExecutor.*;

import java.sql.*;

public final class DeleteQueryBuilder extends QueryBuilder<Integer> {
  public DeleteQueryBuilder(final String tableName) {
    super(tableName);
  }

  protected String getSqlString() {
    return "DELETE FROM " + this.tableName + this.constraintsSB;
  }

  protected Integer getOperationRes(final String queryStr) throws SQLException {
    return executeUpdateStatement(queryStr);
  }

}
