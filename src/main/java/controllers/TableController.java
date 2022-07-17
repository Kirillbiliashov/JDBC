package controllers;

import src.Table;

import java.sql.*;


public abstract class TableController {

  protected Table table;
  public abstract TableController instantiateTable() throws Exception;

  protected abstract String getTableName();

  protected abstract String[] getColNames();

  protected abstract String[] getColTypes();

  protected abstract String[][] getData();

  public TableController populateTable() throws SQLException {
    final String[][] data = this.getData();
    for (final String[] row : data) {
      this.table.insert(row);
    }
    return this;
  }

  public Table getTable() throws Exception {
    if (this.table == null) {
      this.instantiateTable();
    }
    return this.table;
  }

}
