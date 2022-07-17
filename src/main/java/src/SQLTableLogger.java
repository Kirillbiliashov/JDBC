package src;

import java.sql.*;
import java.util.*;

public final class SQLTableLogger {

  private final ResultSet rs;
  private final ResultSetMetaData metadata;
  private final int cols;
  private final String COL_DELIMITER = "|";
  private final String ROW_DELIMITER = "-";

  private SQLTableLogger(final ResultSet rs) throws SQLException {
    this.rs = rs;
    this.metadata = rs.getMetaData();
    this.cols = this.metadata.getColumnCount();
  }

  public static SQLTableLogger create(final ResultSet rs) throws SQLException {
    return new SQLTableLogger(rs);
  }

  public void printTable() throws SQLException {
    final List<String> colNames = this.getColNames();
    final List<Integer> colMaxSizes = this.getColMaxSizes();
    final String tableHeader = this.getTableHeader(colNames, colMaxSizes);
    final String borderStr = this.getTableBorder(tableHeader.length());
    System.out.println(borderStr);
    System.out.println(tableHeader);
    while (this.rs.next()) this.printRowData(this.rs, colNames, colMaxSizes);
    System.out.println(borderStr);
  }

  private List<String> getColNames() throws SQLException {
    final List<String> colNames = new ArrayList<>(this.cols);
    for (int i = 0; i < this.cols; i++) {
      colNames.add(this.metadata.getColumnName(i + 1));
    }
    return colNames;
  }

  private List<Integer> getColMaxSizes() throws SQLException {
    final List<Integer> colMaxSizes = new ArrayList<>(this.cols);
    for (int i = 0; i < cols; i++) {
      colMaxSizes.add(this.metadata.getColumnDisplaySize(i + 1));
    }
    return colMaxSizes;
  }

  private String getTableHeader(final List<String> colNames,
                                final List<Integer> colMaxSizes) {
    final StringBuilder tableHeaderBuilder = new StringBuilder();
    for (final String colName : colNames) {
      final int idx = colNames.indexOf(colName);
      final int colMaxSize = colMaxSizes.get(idx);
      tableHeaderBuilder.append(this.getPaddedString(colMaxSize, colName))
          .append(COL_DELIMITER);
    }
    return tableHeaderBuilder.toString();
  }

  private String getPaddedString(final int newStrLength, final String str) {
    final int ACTUAL_LENGTH = str.length();
    return str + " ".repeat(Math.max(0, newStrLength - ACTUAL_LENGTH));
  }

  private String getTableBorder(final int rowLength) {
    return ROW_DELIMITER.repeat(rowLength);
  }

  private void printRowData(final ResultSet rs, final List<String> colNames,
                            final List<Integer> colMaxSizes) throws SQLException {
    StringBuilder rowStr = new StringBuilder();
    for (int i = 0; i < this.cols; i++) {
      final int colMaxSize = colMaxSizes.get(i);
      final int newStrLength = Math.max(colMaxSize, colNames.get(i).length());
      final String paddedStr = getPaddedString(newStrLength,
          String.valueOf(rs.getObject(i + 1)));
      rowStr.append(paddedStr).append(COL_DELIMITER);
    }
    System.out.println(rowStr);
  }

}
