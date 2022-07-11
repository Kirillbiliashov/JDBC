package controllers;

import src.Table;

import java.sql.Connection;

public class DisciplinesTableController extends TableController {

  public void instantiateTable(final Connection conn) throws Exception {
    final Table disciplines = new Table("Disciplines", this.getColNames(),
        this.getColTypes(), conn);
    disciplines.setPrimaryKeyField(1).setNotNullColumns().create();
    this.table = disciplines;
  }

  protected String getTableName() {
    return "Disciplines";
  }

  protected String[] getColNames() {
    return new String[]{"Name", "Length"};
  }

  protected String[] getColTypes() {
    return new String[]{"varchar(50)", "int"};
  }

  protected String[][] getData() {
    return new String[][]{
        {"Back-end Development", "2"}, {"Basics Of Programming", "2"},
        {"Devops Basics", "2"}, {"English", "8"}, {"History", "1"},
        {"Linux", "1"}, {"Mobile Development", "1"}, {"Philosophy", "1"},
        {"Theory of Probability", "1"}
    };
  }

}
