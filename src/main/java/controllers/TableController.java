package controllers;

import src.Table;

import java.sql.*;


public abstract class TableController {

  protected Table table;

  public abstract void instantiateTable(final Connection conn) throws Exception;

  protected abstract String getTableName();

  protected abstract String[] getColNames();

  protected abstract String[] getColTypes();

  protected abstract String[][] getData();

  public void populateTable() throws SQLException {
    final String[][] data = this.getData();
    for (final String[] row : data) {
      this.table.insert(row);
    }
  }

  public Table getTable() {
    return this.table;
  }

}
