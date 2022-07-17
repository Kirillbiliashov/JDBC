package queryBuilders;

import java.sql.*;
import java.util.function.Consumer;

public abstract class QueryBuilder<T> {

  protected final StringBuilder constraintsSB = new StringBuilder();
  protected final String tableName;
  protected final String DELIMITER = ", ";

  public QueryBuilder(final String tableName) {
    this.tableName = tableName;
  }

  protected abstract String getSqlString();

  protected abstract T getOperationRes(final String queryStr) throws SQLException;

  public QueryBuilder<T> where(final String colName, final String operator,
                               final String value) {
    this.buildConstraint(" WHERE ", colName, operator, value);
    return this;
  }

  private String buildExpression(final String colName, final String operator,
                                 final String value) {
    return colName + " " + operator + " " + value;
  }

  public QueryBuilder<T> whereNot(final String colName, final String operator,
                                  final String value) {
    this.buildConstraint(" WHERE NOT ", colName, operator, value);
    return this;
  }

  public QueryBuilder<T> and(final String colName, final String operator,
                             final String value) {
    this.buildConstraint(" AND ", colName, operator, value);
    return this;
  }

  public QueryBuilder<T> andNot(final String colName, final String operator,
                                final String value) {
    this.buildConstraint(" AND NOT ", colName, operator, value);
    return this;
  }

  public QueryBuilder<T> or(final String colName, final String operator,
                            final String value) {
    this.buildConstraint(" OR ", colName, operator, value);
    return this;
  }

  private void buildConstraint(final String keyword, final String colName,
                               final String operator, final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(keyword).append(expression);
  }

  public void execute(final Consumer<T> fn) throws SQLException {
      final String queryStr = this.getSqlString();
      System.out.println(queryStr);
      final T res = this.getOperationRes(queryStr);
      fn.accept(res);
  }

}
