package src;

import queryBuilders.*;

import java.sql.*;
import java.util.*;
import static src.ConnectionSingleton.*;
public class Table {

  private final String tableName;
  private final ArrayList<String> colNames;
  private final ArrayList<String> colTypes;
  private final String LINE_SEPARATOR = ", \n";
  private final List<String> foreignKeyConstraints;
  private final List<Integer> autoIncColIndices;
  private final Map<Integer, String> defaultValues;
  private final List<String> uniqueColStatements;
  private final List<String> checkStatements;
  private final int colsCount;
  private String primaryKeyColName;
  private List<Integer> notNullColIndices;

  public Table(final String tableName, final String[] colNames,
               final String[] colTypes) throws Exception {
    final int length = colNames.length;
    if (length != colTypes.length) {
      final String exceptionStr = "Number of column names should be equal to" +
          " number of column types";
      throw new Exception(exceptionStr);
    }
    this.tableName = tableName;
    this.colNames = new ArrayList<>(Arrays.asList(colNames));
    this.colTypes = new ArrayList<>(Arrays.asList(colTypes));
    this.colsCount = colNames.length;
    this.foreignKeyConstraints = new ArrayList<>(colNames.length);
    this.notNullColIndices = new ArrayList<>(colNames.length);
    this.autoIncColIndices = new ArrayList<>(colNames.length);
    this.defaultValues = new HashMap<>(colNames.length);
    this.uniqueColStatements = new ArrayList<>(colNames.length);
    this.checkStatements = new ArrayList<>(colNames.length);
  }

  public void create() throws SQLException {
    try (final Statement stmt = getConn().createStatement()) {
      final String sqlStr = this.getCreateTableStatement();
      System.out.println(sqlStr);
      if (stmt.execute(sqlStr)) {
        System.out.println("table successfully added to the database");
      }
    }
  }

  private String getCreateTableStatement() {
    final String checkStr = this.getConstraintStr(this.checkStatements);
    final String foreignKeyStr = this.getConstraintStr(this.foreignKeyConstraints);
    final String uniqueStr = this.getConstraintStr(this.uniqueColStatements);
    return "CREATE TABLE IF NOT EXISTS " + this.tableName + " (" +
        this.createColumnsStr() + checkStr + uniqueStr +
        this.getPrimaryKeyStr() + foreignKeyStr + ");";
  }

  private String getConstraintStr(final List<String> constraintsList) {
    final boolean constraintExists = !constraintsList.isEmpty();
    return constraintExists ? LINE_SEPARATOR +
        String.join(LINE_SEPARATOR, constraintsList) : "";
  }

  public UpdateQueryBuilder update() {
    return new UpdateQueryBuilder(this.tableName, this.colsCount);
  }

  public DeleteQueryBuilder delete() {
    return new DeleteQueryBuilder(this.tableName);
  }

  public void drop() throws SQLException {
    try (final Statement stmt = getConn().createStatement()) {
      final String sqlStr = "DROP TABLE IF EXISTS " + this.tableName;
      System.out.println(sqlStr);
      if (stmt.execute(sqlStr)) {
        System.out.println("table successfully dropped from the database");
      }
    }
  }
  public ColumnController column() {
    return new ColumnController(this.tableName, this.colNames, this.colTypes);
  }

  public void addCheck(final String colName, final String operator,
                       final String value) throws SQLException {
    if (this.colNames.contains(colName)) {
      final String alterStr = "ALTER TABLE " + this.tableName +
          " ADD CHECK (" + colName + operator + value + ")";
      this.executeStatement(alterStr);
    }
  }

  public void createIndex(final String idxName, final boolean isUnique,
                          final String... colNames) throws SQLException {
    final String isUniqueStr = isUnique ? "UNIQUE" : "";
    final String colNamesStr = String.join(", ", colNames);
    final String createIdxString = "CREATE " + isUniqueStr + " INDEX " +
        idxName + " ON " + this.tableName + " (" + colNamesStr + ")";
    this.executeStatement(createIdxString);
  }

  public void dropIndex(final String idxName) throws SQLException {
    final String dropIdxString = "ALTER TABLE " + this.tableName + " DROP INDEX " + idxName;
    this.executeStatement(dropIdxString);
  }

  private void executeStatement(final String stmtString)
      throws SQLException {
    try (final Statement stmt = getConn().createStatement()) {
      stmt.execute(stmtString);
      System.out.println("Successfully altered table " +
          this.tableName);
    }
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
    for (final String insertCol : insertColNames) {
      System.out.println(insertCol);
    }
    for (int i = 0; i < insertColNames.size(); i++) {
      rs.updateObject(insertColNames.get(i), values[i]);
    }
  }

  private List<String> getInsertCols() {
    final List<String> insertColNames = new ArrayList<>(this.colsCount);
    for (int i = 0; i < this.colsCount; i++) {
      if (!this.autoIncColIndices.contains(i + 1)) {
        insertColNames.add(this.colNames.get(i));
      }
    }
    return insertColNames;
  }

  private String createColumnsStr() {
    final List<String> colDeclarationsList = new ArrayList<>(this.colsCount);
    for (int i = 0; i < this.colsCount; i++) {
      colDeclarationsList.add(this.getColStr(i));
    }
    return String.join(LINE_SEPARATOR, colDeclarationsList);
  }

  private String getColStr(final int idx) {
    return this.colNames.get(idx) + " " + this.colTypes.get(idx)
        + this.getNotNullStr(idx + 1) + this.getAutoIncStr(idx + 1)
        + this.getDefaultValueStr(idx + 1);
  }

  private String getNotNullStr(final int idx) {
    final boolean isNotNull = this.notNullColIndices.contains(idx);
    return isNotNull ? " NOT NULL" : "";
  }

  private String getAutoIncStr(final int idx) {
    final boolean isAutoInc = this.autoIncColIndices.contains(idx);
    return isAutoInc ? " AUTO_INCREMENT" : "";
  }

  private String getDefaultValueStr(final int colIdx) {
    final String defaultValue = this.defaultValues.get(colIdx);
    return defaultValue == null ? "" : " DEFAULT " + defaultValue;
  }

  private String getPrimaryKeyStr() {
    if (this.primaryKeyColName == null) return "";
    return LINE_SEPARATOR + "PRIMARY KEY (" + this.primaryKeyColName + ")";
  }

  public Table setPrimaryKeyField(final String... colNames) {
    this.primaryKeyColName = String.join(", ", colNames);
    return this;
  }

  public Table setPrimaryKeyField(final int colIdx) {
    if (colIdx <= this.colsCount) {
      this.primaryKeyColName = this.colNames.get(colIdx - 1);
    }
    return this;
  }

  public Table setForeignKey(final String colName, final String foreignTable,
                             final String foreignColName,
                             final boolean isCascadeDelete) {
    if (this.colNames.contains(colName)) {
      final String cascadeDeleteStr = isCascadeDelete ? " ON DELETE CASCADE" :
          "";
      final String constraintStr = "FOREIGN KEY (" + colName + ") REFERENCES " +
          foreignTable + "(" + foreignColName + ")" + cascadeDeleteStr;
      this.foreignKeyConstraints.add(constraintStr);
    }
    return this;
  }

  public Table setForeignKey(final int colIdx, final String foreignTable,
                             final String foreignColName) {
    if (colIdx <= this.colsCount) {
      final String constraintStr = "FOREIGN KEY (" + this.colNames.get(colIdx - 1) +
          ") REFERENCES " + foreignTable + "(" + foreignColName + ")";
      this.foreignKeyConstraints.add(constraintStr);
    }
    return this;
  }

  public Table setNotNullColumns() {
    for (int i = 0; i < this.colsCount; i++) {
      this.notNullColIndices.add(i + 1);
    }
    return this;
  }

  public Table setNotNullColumns(final Integer... cols) {
    this.notNullColIndices = Arrays.asList(cols);
    return this;
  }

  public Table setNotNullColumns(final String... colNames) {
    for (final String colName : colNames) {
      final int idx = this.colNames.indexOf(colName);
      if (!(idx == -1 || this.notNullColIndices.contains(idx + 1))) {
        this.notNullColIndices.add(idx + 1);
      }
    }
    return this;
  }

  public Table setDefaultValue(final int colIdx, final String defaultVal) {
    this.defaultValues.put(colIdx, defaultVal);
    return this;
  }

  public Table setDefaultValue(final String colName, final String defaultVal) {
    final int idx = this.colNames.indexOf(colName);
    if (idx != -1) {
      this.defaultValues.put(idx + 1, defaultVal);
    }
    return this;
  }

  public Table setAutoIncCol(final int colIdx) {
    if (colIdx <= this.colsCount && !this.autoIncColIndices.contains(colIdx)) {
      this.autoIncColIndices.add(colIdx);
    }
    return this;
  }

  public Table setAutoIncCol(final String colName) {
    final int idx = this.colNames.indexOf(colName);
    if (idx != -1 && !this.autoIncColIndices.contains(idx + 1)) {
      this.autoIncColIndices.add(idx + 1);
    }
    return this;
  }

  public Table setUniqueCol(final int colIdx) {
    if (colIdx <= this.colsCount) {
      this.uniqueColStatements.add("UNIQUE (" + this.colNames.get(colIdx - 1) +
          ")");
    }
    return this;
  }

  public Table setUniqueCol(final String colName) {
    if (this.colNames.contains(colName) &&
        !this.uniqueColStatements.contains(colName)) {
      this.uniqueColStatements.add("UNIQUE (" + colName + ")");
    }
    return this;
  }

  public Table setCheck(final String colName, final String operator,
                        final String value) {
    final String checkStr = "CHECK (" + colName + operator + value + ")";
    if (!this.checkStatements.contains(checkStr)) {
      this.checkStatements.add(checkStr);
    }
    return this;
  }

}
