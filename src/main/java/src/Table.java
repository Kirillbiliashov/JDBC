package src;

import queryBuilders.*;

import java.sql.*;
import java.util.*;

import static src.ConnectionSingleton.*;
import static src.Helpers.*;

public class Table {

  private final String tableName;
  private final String LINE_SEPARATOR = ", \n";
  private final int colsCount;
  private final List<Column> columnsList;
  private Column currColumn;

  public Table(final String tableName, final String[] colNames,
               final String[] colTypes) throws Exception {
    final int length = colNames.length;
    if (length != colTypes.length) {
      final String exceptionStr = "Number of column names should be equal to" +
          " number of column types";
      throw new Exception(exceptionStr);
    }
    this.tableName = tableName;
    this.colsCount = length;
    this.columnsList = new ArrayList<>(length);
    this.populateColsList(colNames, colTypes);
  }

  private void populateColsList(final String[] colNames,
                                final String[] colTypes) {
    for (int i = 0; i < this.colsCount; i++) {
      this.columnsList.add(new Column(colNames[i], colTypes[i]));
    }
  }

  public void create() throws SQLException {
    final String sqlStr = "CREATE TABLE IF NOT EXISTS " + this.tableName +
        " (" + this.getColumnsStr() + ")";
    final String successMessage = "table successfully added to the database";
    executeStatement(sqlStr, successMessage);
  }

  public UpdateQueryBuilder update() {
    return new UpdateQueryBuilder(this.tableName, this.colsCount);
  }

  public DeleteQueryBuilder delete() {
    return new DeleteQueryBuilder(this.tableName);
  }

  public void drop() throws SQLException {
    final String sqlStr = "DROP TABLE IF EXISTS " + this.tableName;
    final String successMessage = "table successfully dropped from the database";
    executeStatement(sqlStr, successMessage);
  }

  public ColumnsController columns() {
    return new ColumnsController(this.tableName, this.columnsList);
  }


  public void insert(final Object... values) throws SQLException {
    final String[] stringValues = new String[values.length];
    for (int i = 0; i < values.length; i++) {
      stringValues[i] = values[i].toString();
    }
    this.insert(stringValues);
  }

  public void insert(final String[] values) throws SQLException {
    try (final Statement stmt = getConn().createStatement(
        ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
      final ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
      rs.moveToInsertRow();
      this.createNewRow(values, rs);
      rs.insertRow();
    }
  }

  public SelectQueryBuilder select() {
    final String ALL_COLS_SYMBOL = "*";
    return new SelectQueryBuilder(this.tableName,
        new String[]{ALL_COLS_SYMBOL});
  }

  public SelectQueryBuilder select(final String... colNames) {
    return new SelectQueryBuilder(this.tableName, colNames);
  }

  private void createNewRow(final String[] values, final ResultSet rs)
      throws SQLException {
    final List<String> insertColNames = this.getInsertCols();
    for (int i = 0; i < insertColNames.size(); i++) {
      rs.updateObject(insertColNames.get(i), values[i]);
    }
  }

  private List<String> getInsertCols() {
    final List<String> insertColNames = new ArrayList<>(this.colsCount);
    for (final Column column : this.columnsList) {
      if (column.isAutoIncrement()) {
        insertColNames.add(column.getName());
      }
    }
    return insertColNames;
  }

  private String getColumnsStr() {
    final List<String> colStrList = this.columnsList.stream().
        map(Object::toString).toList();
    return String.join(LINE_SEPARATOR, colStrList);
  }

  public Table setNotNullColumns() {
    for (final Column column : this.columnsList) {
      column.setNotNull();
    }
    return this;
  }

  public Table column(final String colName) {
    for (final Column column : this.columnsList) {
      if (column.getName().equals(colName)) this.currColumn = column;
    }
    return this;
  }

  public Table primaryKey() {
    this.currColumn.setPrimaryKey();
    return this;
  }

  public Table notNull() {
    this.currColumn.setNotNull();
    return this;
  }

  public Table defaultValue(final String defaultValue) {
    this.currColumn.setDefaultValue(defaultValue);
    return this;
  }

  public Table reference(final String tableName, final String colName,
                         final boolean isCascadeDelete) {
    this.currColumn.setReferenceCol(tableName, colName, isCascadeDelete);
    return this;
  }

  public Table unique() {
    this.currColumn.setUnique();
    return this;
  }

  public Table autoIncrement() {
    this.currColumn.setAutoIncrement();
    return this;
  }

  public Table check(final String operator, final String value) {
    this.currColumn.setCheck(operator, value);
    return this;
  }

}
