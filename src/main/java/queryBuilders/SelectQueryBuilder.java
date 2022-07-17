package queryBuilders;

import static src.StatementExecutor.*;

import java.sql.*;

public final class SelectQueryBuilder extends QueryBuilder<ResultSet> {

  private final String[] selectColNames;
  private boolean isOrdered;
  private boolean isSelfJoin;
  private String insertTable;
  private String[] insertTableColNames;

  public SelectQueryBuilder(final String tableName, final String[] selectColNames) {
    super(tableName);
    this.selectColNames = selectColNames;
  }

  protected String getSqlString() {
    final String colNamesSequence = String.join(DELIMITER, this.selectColNames);
    final String tableName = this.isSelfJoin ? this.tableName + " A, " +
        this.tableName + " B " : this.tableName;
    return this.getInsertStr() + "SELECT " + colNamesSequence + " FROM " +
        tableName + constraintsSB;
  }

  protected ResultSet getOperationRes(final String queryStr) throws SQLException {
    return executeQueryStatement(queryStr);
  }

  public SelectQueryBuilder where(final String colName, final String operator,
                                  final String value) {
    return (SelectQueryBuilder) super.where(colName, operator, value);
  }

  public SelectQueryBuilder whereNot(final String colName,
                                     final String operator,
                                     final String value) {
    return (SelectQueryBuilder) super.whereNot(colName, operator, value);
  }

  public SelectQueryBuilder and(final String colName, final String operator,
                                final String value) {
    return (SelectQueryBuilder) super.and(colName, operator, value);
  }

  public SelectQueryBuilder andNot(final String colName, final String operator,
                                   final String value) {
    return (SelectQueryBuilder) super.andNot(colName, operator, value);
  }

  public SelectQueryBuilder or(final String colName, final String operator,
                               final String value) {
    return (SelectQueryBuilder) super.or(colName, operator, value);
  }

  public SelectQueryBuilder having(final String colName, final String operator,
                                   final String value) {
    final String expression = this.buildExpression(colName, operator, value);
    this.constraintsSB.append(" HAVING ").append(expression);
    return this;
  }

  public SelectQueryBuilder whereExists(final SelectQueryBuilder selectBuilder) {
    final String selectStmt = selectBuilder.getSqlString();
    this.constraintsSB.append(" WHERE EXISTS (").append(selectStmt).append(")");
    return this;
  }

  public SelectQueryBuilder whereAny(final String colName,
                                     final String operator,
                                     final SelectQueryBuilder selectBuilder) {
    final String selectStmt = selectBuilder.getSqlString();
    this.constraintsSB.append(" WHERE ").append(colName).append(operator)
        .append("ANY (").append(selectStmt).append(")");
    return this;
  }

  public SelectQueryBuilder whereAll(final String colName,
                                     final String operator,
                                     final SelectQueryBuilder selectBuilder) {
    final String selectStmt = selectBuilder.getSqlString();
    this.constraintsSB.append(" WHERE ").append(colName).append(operator)
        .append("ALL (").append(selectStmt).append(")");
    return this;
  }


  public SelectQueryBuilder as(final String colName, final String alias) {
    for (int i = 0; i < this.selectColNames.length; i++) {
      if (this.selectColNames[i].equals(colName)) {
        this.selectColNames[i] += " AS " + alias;
      }
    }
    return this;
  }

  public SelectQueryBuilder groupBy(final String groupByCol) {
    this.constraintsSB.append(" GROUP BY ").append(groupByCol);
    return this;
  }

  public SelectQueryBuilder orderBy(final String colName,
                                    final boolean isAscending) {
    final String orderCol = colName + (isAscending ? "" : " DESC");
    this.constraintsSB.append(this.isOrdered ? DELIMITER : " ORDER BY ")
        .append(orderCol);
    if (!this.isOrdered) this.isOrdered = true;
    return this;
  }

  public SelectQueryBuilder limit(final int limitCount) {
    this.constraintsSB.append(" LIMIT ").append(limitCount);
    return this;
  }

  public SelectQueryBuilder join(final String joinTable,
                                 final String relatedCol,
                                 final String joinTableCol) {
    this.addJoinConstraint(" INNER ", joinTable, relatedCol, joinTableCol);
    return this;
  }

  public SelectQueryBuilder rightJoin(final String joinTable,
                                      final String relatedCol,
                                      final String joinTableCol) {
    this.addJoinConstraint(" RIGHT ", joinTable, relatedCol, joinTableCol);
    return this;
  }

  public SelectQueryBuilder leftJoin(final String joinTable,
                                     final String relatedCol,
                                     final String joinTableCol) {
    this.addJoinConstraint(" LEFT ", joinTable, relatedCol, joinTableCol);
    return this;
  }

  private void addJoinConstraint(final String joinType, final String joinTable,
                                 final String relatedCol,
                                 final String joinTableCol) {
    final String joinExpression = this.buildJoinExpression(relatedCol,
        joinTableCol, joinTable);
    this.constraintsSB.append(joinType).append("JOIN ").append(joinTable)
        .append(" ON ").append(joinExpression);
  }

  public SelectQueryBuilder crossJoin(final String joinTable) {
    this.constraintsSB.append(" CROSS JOIN ").append(joinTable);
    return this;
  }

  public SelectQueryBuilder selfJoin() {
    this.isSelfJoin = true;
    return this;
  }

  public SelectQueryBuilder insertInto(final String tableName,
                                       final String... colNames) {
    this.insertTable = tableName;
    this.insertTableColNames = colNames;
    return this;
  }


  private String buildJoinExpression(final String relatedColName,
                                     final String joinTableRelatedColName,
                                     final String joinTable) {
    return this.tableName + "." + relatedColName + " = " + joinTable + "." +
        joinTableRelatedColName;
  }


  private String buildExpression(final String colName, final String operator,
                                 final String value) {
    return colName + " " + operator + " " + value;
  }

  private String getInsertStr() {
    if (this.insertTable == null) return "";
    return "INSERT INTO " + this.insertTable + " (" +
        String.join(DELIMITER, this.insertTableColNames) + ") ";
  }

}
