import java.sql.*;
import java.util.function.Consumer;

public class SelectBuilder {
  private final Connection conn;
  private final String tableName;
  private final StringBuilder constraintsSB = new StringBuilder();
  private final String[] selectColNames;
  private boolean isOrdered;

  public SelectBuilder(final Connection conn, final String tableName,
                       final String[] selectColNames) {
    this.conn = conn;
    this.tableName = tableName;
    this.selectColNames = selectColNames;
  }

  public SelectBuilder where(final String colName, final String operator,
                             final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(" WHERE ").append(expression);
    return this;
  }

  public SelectBuilder as(final String colName, final String alias) {
    for (int i = 0; i < this.selectColNames.length; i++) {
      if (this.selectColNames[i].equals(colName)) {
        this.selectColNames[i] += " AS " + alias;
      }
    }
    return this;
  }

  public SelectBuilder whereNot(final String colName, final String operator,
                                final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(" WHERE NOT ").append(expression);
    return this;
  }

  public SelectBuilder and(final String colName, final String operator,
                           final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(" AND ").append(expression);
    return this;
  }

  public SelectBuilder andNot(final String colName, final String operator,
                              final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(" AND NOT").append(expression);
    return this;
  }

  public SelectBuilder or(final String colName, final String operator,
                          final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(" OR ").append(expression);
    return this;
  }

  public SelectBuilder orderBy(final String colName, final boolean isAscending) {
    final String orderCol = colName + (!isAscending ? " DESC" : "");
    if (!this.isOrdered) {
      this.constraintsSB.append(" ORDER BY ").append(orderCol);
      this.isOrdered = true;
    } else {
      this.constraintsSB.append(", ").append(orderCol);
    }
    return this;
  }

  public SelectBuilder limit(final int limitCount) {
    this.constraintsSB.append(" LIMIT ").append(limitCount);
    return this;
  }

  private String buildExpression(final String colName, final String operator,
                                 final String value) {


    return colName + " " + operator + " " + value;
  }

  private String buildSelectStatement() {
    final StringBuilder selectStrBuilder = new StringBuilder("SELECT ");
    final String colNamesSequence = String.join(", ", this.selectColNames);
    return selectStrBuilder.append(colNamesSequence).append(" FROM ")
        .append(this.tableName).append(constraintsSB).toString();
  }

  public void execute(final Consumer<ResultSet> fn) {
    try (final Statement stmt = this.conn.createStatement()) {
      final String selectStr = this.buildSelectStatement();
      final ResultSet rs = stmt.executeQuery(selectStr);
      fn.accept(rs);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
