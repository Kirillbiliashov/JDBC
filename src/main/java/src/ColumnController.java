package src;

import java.sql.*;
import java.util.List;
import static src.ConnectionSingleton.*;
public class ColumnController {
  private final String tableName;
  private final List<String> colNames;
  private final List<String> colTypes;

  public ColumnController(final String tableName, final List<String> colNames,
                          final List<String> colTypes) {
    this.tableName = tableName;
    this.colNames = colNames;
    this.colTypes = colTypes;

  }

  public void add(final String colName, final String dataType)
      throws SQLException {
    if (!this.colNames.contains(colName)) {
      this.colNames.add(colName);
      this.colTypes.add(dataType);
      final String alterStr = "ALTER TABLE " + this.tableName + " ADD " +
          colName + " " + dataType;
      System.out.println(alterStr);
      this.executeStatement(alterStr);
    }
  }

  public void drop(final String colName) throws SQLException {
    final int idx = this.colNames.indexOf(colName);
    if (idx != -1) {
      this.colNames.remove(idx);
      this.colTypes.remove(idx);
      final String alterStr = "ALTER TABLE " + this.tableName +
          " DROP COLUMN " + colName;
      this.executeStatement(alterStr);
    }
  }

  public void modify(final String colName, final String newDataType)
      throws SQLException {
    final int idx = this.colNames.indexOf(colName);
    if (idx != -1) {
      this.colTypes.remove(idx);
      this.colTypes.add(idx, newDataType);
      final String alterStr = "ALTER TABLE " + this.tableName +
          " MODIFY COLUMN " + colName + " " + newDataType;
      this.executeStatement(alterStr);
    }
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
