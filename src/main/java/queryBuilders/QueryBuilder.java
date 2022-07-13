package queryBuilders;

import java.sql.*;
import java.util.function.Consumer;

public abstract class QueryBuilder<T> {
  protected final StringBuilder constraintsSB = new StringBuilder();
  protected final String tableName;
  protected final Connection conn;
  protected final String DELIMITER = ", ";

  public QueryBuilder(final String tableName, final Connection conn) {
    this.tableName = tableName;
    this.conn = conn;
  }

  protected abstract String getStatement();

  protected abstract T getOperationRes(final Statement stmt,
                                       final String queryStr) throws SQLException;

  public QueryBuilder<T> where(final String colName, final String operator,
                            final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(" WHERE ").append(expression);
    return this;
  }

  private String buildExpression(final String colName, final String operator,
                                 final String value) {
    return colName + " " + operator + " " + value;
  }

  public QueryBuilder<T> whereNot(final String colName, final String operator,
                               final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(" WHERE NOT ").append(expression);
    return this;
  }

  public QueryBuilder<T> and(final String colName, final String operator,
                          final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(" AND ").append(expression);
    return this;
  }

  public QueryBuilder<T> andNot(final String colName, final String operator,
                             final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(" AND NOT ").append(expression);
    return this;
  }

  public QueryBuilder<T> or(final String colName, final String operator,
                         final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(" OR ").append(expression);
    return this;
  }

  public void execute(final Consumer<T> fn) {
    try (final Statement stmt = this.conn.createStatement()) {
      final String queryStr = this.getStatement();
      System.out.println(queryStr);
      final T res = this.getOperationRes(stmt, queryStr);
      fn.accept(res);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
