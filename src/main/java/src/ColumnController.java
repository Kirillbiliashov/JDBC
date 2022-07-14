package src;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ColumnController {
  private final String tableName;
  private final List<String> colNames;
  private final List<String> colTypes;
  private final Connection conn;

  public ColumnController(final String tableName, final List<String> colNames,
                          final List<String> colTypes, final Connection conn) {
    this.tableName = tableName;
    this.colNames = colNames;
    this.colTypes = colTypes;
    this.conn = conn;

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
    try (final Statement stmt = this.conn.createStatement()) {
      stmt.execute(stmtString);
      System.out.println("Successfully altered table " +
          this.tableName);
    }
  }
}
