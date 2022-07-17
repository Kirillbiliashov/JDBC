package queryBuilders;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public final class UpdateQueryBuilder extends QueryBuilder<Integer> {

  private final Map<String, String> updatesMap;

  public UpdateQueryBuilder(final String tableName, final int colsCount) {
    super(tableName);
    this.updatesMap = new HashMap<>(colsCount);
  }

  protected String getStatement() {
    return "UPDATE " + this.tableName + " SET " + this.getSetExpression() +
        this.constraintsSB;
  }

  protected Integer getOperationRes(Statement stmt, String queryStr)
      throws SQLException {
    return stmt.executeUpdate(queryStr);
  }

  public UpdateQueryBuilder set(final String colName, final String colValue) {
    this.updatesMap.put(colName, colValue);
    return this;
  }

  private String getSetExpression() {
    final List<String> parsedUpdateList = updatesMap.entrySet().stream().
        map(e -> e.getKey() + " = " + e.getValue()).toList();
    return String.join(DELIMITER, parsedUpdateList);
  }

}
