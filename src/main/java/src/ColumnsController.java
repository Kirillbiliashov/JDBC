package src;

import java.sql.*;
import java.util.List;

import static src.ConnectionSingleton.*;

public class ColumnsController {

  private String tableName;
  private List<Column> columnsList;

  public ColumnsController(final String tableName, final List<Column> columnsList) {
    this.tableName = tableName;
    this.columnsList = columnsList;
  }


  public void add(final String colName, final String dataType)
      throws SQLException {
    final Column newCol = new Column(colName, dataType);
    this.columnsList.add(newCol);
    final String addColumnStr = "ALTER TABLE " + this.tableName + " ADD " +
        colName + " " + dataType;
    this.executeStatement(addColumnStr);
  }

  public void drop(final String colName) throws SQLException {
    for (final Column column : this.columnsList) {
      if (column.getName().equals(colName)) {
        this.columnsList.remove(column);
        final String dropColumnStr = "ALTER TABLE " + this.tableName +
            " DROP COLUMN " + colName;
        this.executeStatement(dropColumnStr);
        return;
      }
    }
  }

  public void modify(final String colName, final String newDataType)
      throws SQLException {
    for (final Column column : this.columnsList) {
      if (column.getName().equals(colName)) {
        column.setDataType(newDataType);
        final String alterStr = "ALTER TABLE " + this.tableName +
            " MODIFY COLUMN " + colName + " " + newDataType;
        this.executeStatement(alterStr);
        return;
      }
    }
  }

  public void addCheck(final String colName, final String operator,
                       final String value) throws SQLException {
    for (final Column column : this.columnsList) {
      if (column.getName().equals(colName)) {
        final String addCheckStr = "ALTER TABLE " + this.tableName +
            " ADD CHECK (" + colName + operator + value + ")";
        this.executeStatement(addCheckStr);
        break;
      }
    }
  }

  public void createIndex(final String idxName, final boolean isUnique,
                          final String... colNames) throws SQLException {
    final String isUniqueStr = isUnique ? "UNIQUE " : "";
    final String colNamesStr = String.join(", ", colNames);
    final String createIdxString = "CREATE " + isUniqueStr + "INDEX " +
        idxName + " ON " + this.tableName + " (" + colNamesStr + ")";
    this.executeStatement(createIdxString);
  }

  public void dropIndex(final String idxName) throws SQLException {
    final String dropIdxString = "ALTER TABLE " + this.tableName +
        " DROP INDEX " + idxName;
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

}
