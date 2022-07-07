import java.sql.*;
import java.util.*;

public class Table {
  private final String tableName;
  private final String[] colNames;
  private final String[] colTypes;
  private final Connection conn;
  private final String LINE_SEPARATOR = ", \n";
  private final List<String> foreignKeyConstraints;
  private final List<Integer> autoIncColIndices;
  private final Map<String, String> defaultValues;
  private final List<String> uniqueColNames;
  private String primaryKeyColName;
  private int primaryKeyColIdx;
  private List<Integer> notNullColsIndices;

  public Table(final String tableName, final String[] colNames,
               final String[] colTypes, final Connection conn) throws Exception {
    final int length = colNames.length;
    if (length != colTypes.length) {
      final String exceptionStr = "Number of column names should be equal to" +
          " number of column types";
      throw new Exception(exceptionStr);
    }
    this.tableName = tableName;
    this.colNames = colNames;
    this.colTypes = colTypes;
    this.foreignKeyConstraints = new ArrayList<>(colNames.length);
    this.notNullColsIndices = new ArrayList<>(colNames.length);
    this.autoIncColIndices = new ArrayList<>(colNames.length);
    this.defaultValues = new HashMap<>(colNames.length);
    this.uniqueColNames = new ArrayList<>(colNames.length);
    this.conn = conn;
  }

  public void create() {
    try (final Statement stmt = this.conn.createStatement()) {
      final String sqlStr = "CREATE TABLE IF NOT EXISTS " + this.tableName +
          " (" + this.createColumnsStr() + this.getUniqueKeyStr() +
          this.getPrimaryKeyStr() + this.getForeignKeyStr() + ");";
      System.out.println(sqlStr);
      if (stmt.execute(sqlStr)) {
        System.out.println("Table successfully added to the database");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String getForeignKeyStr() {
    final StringBuilder res = new StringBuilder(LINE_SEPARATOR);
    for (final String fkConstraint : foreignKeyConstraints) {
      res.append(fkConstraint).append(LINE_SEPARATOR);
    }
    return res.substring(0, res.length() - LINE_SEPARATOR.length());
  }

  private String getUniqueKeyStr() {
    final StringBuilder res = new StringBuilder(LINE_SEPARATOR);
    for (final String colName : this.uniqueColNames) {
      res.append("UNIQUE (").append(colName).append(")").append(LINE_SEPARATOR);
    }
    return res.substring(0, res.length() - LINE_SEPARATOR.length());
  }

  private String createColumnsStr() {
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < colNames.length; i++) {
      final String colName = this.colNames[i];
      final String notNullStr = this.getNotNullStr(i + 1);
      final String autoIncStr = this.getAutoIncStr(i + 1);
      final String defaultValueStr = this.getDefaultValueStr(colName);
      res.append(colName).append(" ").append(colTypes[i]).append(notNullStr)
          .append(autoIncStr).append(defaultValueStr).append(LINE_SEPARATOR);
    }
    return res.substring(0, res.length() - LINE_SEPARATOR.length());
  }

  private String getNotNullStr(final int idx) {
    final boolean isNotNull = this.notNullColsIndices.contains(idx);
    return isNotNull ? " NOT NULL" : "";
  }

  private String getAutoIncStr(final int idx) {
    final boolean isAutoInc = this.autoIncColIndices.contains(idx);
    return isAutoInc ? " AUTO_INCREMENT" : "";
  }

  private String getDefaultValueStr(final String colName) {
    final String defaultValue = this.defaultValues.get(colName);
    return defaultValue == null ? "" : " DEFAULT " + defaultValue;
  }


  private String getPrimaryKeyStr() {
    if (primaryKeyColName == null) {
      primaryKeyColName = this.colNames[primaryKeyColIdx - 1];
    }
    return LINE_SEPARATOR + "PRIMARY KEY (" + primaryKeyColName + ")";
  }


  public void setPrimaryKeyField(final String colName) {
    this.primaryKeyColName = colName;
  }

  public Table setPrimaryKeyField(final int colIdx) {
    this.primaryKeyColIdx = colIdx;
    return this;
  }

  public Table setForeignKey(final String colName, final String foreignTable,
                             final String foreignColName) {
    final String constraintStr = "FOREIGN KEY (" + colName + ") REFERENCES " +
        foreignTable + "(" + foreignColName + ")";
    foreignKeyConstraints.add(constraintStr);
    return this;
  }


  public Table setNotNullColumns() {
    for (int i = 0; i < colNames.length; i++) {
      notNullColsIndices.add(i + 1);
    }
    return this;
  }

  public Table setNotNullColumns(final Integer... cols) {
    this.notNullColsIndices = Arrays.asList(cols);
    return this;
  }

  public Table setDefaultValue(final int colIdx, final String defaultVal) {
    this.defaultValues.put(this.colNames[colIdx - 1], defaultVal);
    return this;
  }

  public Table setDefaultValue(final String colName, final String defaultVal) {
    this.defaultValues.put(colName, defaultVal);
    return this;
  }

  public Table setAutoIncCol(final int colIdx) {
    this.autoIncColIndices.add(colIdx);
    return this;
  }

  public Table setUniqueCol(final int colIdx) {
    this.uniqueColNames.add(this.colNames[colIdx - 1]);
    return this;
  }

  public Table setUniqueCol(final String colName) {
    this.uniqueColNames.add(colName);
    return this;
  }

}
