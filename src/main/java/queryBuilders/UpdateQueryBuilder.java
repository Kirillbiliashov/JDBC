package queryBuilders;

import java.sql.*;
import java.util.*;

public final class UpdateQueryBuilder extends QueryBuilder<Integer> {

  private final Map<String, String> updColNameValueMap;

  public UpdateQueryBuilder(final String tableName, final int colsCount,
                            final Connection conn) {
    super(tableName, conn);
    this.updColNameValueMap = new HashMap<>(colsCount);
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
    this.updColNameValueMap.put(colName, colValue);
    return this;
  }

  private String getSetExpression() {
    final String DELIMITER = ", ";
    final StringBuilder res = new StringBuilder();
    final Set<Map.Entry<String, String>> entrySet = updColNameValueMap.entrySet();
    for (final Map.Entry<String, String> keyValue : entrySet) {
      res.append(keyValue.getKey()).append(" = ").append(keyValue.getValue())
          .append(DELIMITER);
    }
    return res.subSequence(0, res.length() - DELIMITER.length()).toString();
  }

}
