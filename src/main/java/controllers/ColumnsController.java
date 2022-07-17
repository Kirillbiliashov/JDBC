package controllers;

import src.Column;

import java.sql.*;
import java.util.List;

import static src.StatementExecutor.*;

public final class ColumnsController {

  private final String tableName;
  private final List<Column> columnsList;

  public ColumnsController(final String tableName,
                           final List<Column> columnsList) {
    this.tableName = tableName;
    this.columnsList = columnsList;
  }


  public void add(final String colName, final String dataType)
      throws SQLException {
    final Column newCol = new Column(colName, dataType);
    this.columnsList.add(newCol);
    final String addColumnStr = "ALTER TABLE " + this.tableName + " ADD " +
        colName + " " + dataType;
    final String successMessage = "added column " + colName + " to " +
        this.tableName + " table";
    executeStatement(addColumnStr, successMessage);
  }

  public void drop(final String colName) throws SQLException {
    for (final Column column : this.columnsList) {
      if (!column.getName().equals(colName)) continue;
      this.columnsList.remove(column);
      final String dropColumnStr = "ALTER TABLE " + this.tableName +
          " DROP COLUMN " + colName;
      final String successMessage = "drop column " + colName + " from " +
          this.tableName + " table";
      executeStatement(dropColumnStr, successMessage);
      break;
    }
  }

  public void modify(final String colName, final String newDataType)
      throws SQLException {
    for (final Column column : this.columnsList) {
      if (!column.getName().equals(colName)) continue;
      column.setDataType(newDataType);
      final String alterStr = "ALTER TABLE " + this.tableName +
          " MODIFY COLUMN " + colName + " " + newDataType;
      final String successMessage = "modified column " + colName +
          " for table " + this.tableName;
      executeStatement(alterStr, successMessage);
      break;
    }
  }

  public void addCheck(final String colName, final String operator,
                       final String value) throws SQLException {
    for (final Column column : this.columnsList) {
      if (!column.getName().equals(colName)) continue;
      final String addCheckStr = "ALTER TABLE " + this.tableName +
          " ADD CHECK (" + colName + operator + value + ")";
      final String successMessage = "added check to table " + this.tableName;
      executeStatement(addCheckStr, successMessage);
      break;
    }
  }

  public void createIndex(final String idxName, final boolean isUnique,
                          final String... colNames) throws SQLException {
    final String isUniqueStr = isUnique ? "UNIQUE " : "";
    final String colNamesStr = String.join(", ", colNames);
    final String createIdxString = "CREATE " + isUniqueStr + "INDEX " +
        idxName + " ON " + this.tableName + " (" + colNamesStr + ")";
    final String successMessage = "created index " + idxName + " for table " +
        this.tableName;
    executeStatement(createIdxString, successMessage);
  }

  public void dropIndex(final String idxName) throws SQLException {
    final String dropIdxString = "ALTER TABLE " + this.tableName +
        " DROP INDEX " + idxName;
    final String successMessage = "dropped index" + idxName + " from table " +
        this.tableName;
    executeStatement(dropIdxString, successMessage);
  }

}
