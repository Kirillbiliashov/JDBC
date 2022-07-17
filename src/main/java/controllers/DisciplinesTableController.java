package controllers;

import src.Table;

public final class DisciplinesTableController extends TableController {

  public DisciplinesTableController instantiateTable() throws Exception {
    final Table disciplines = new Table(this.getTableName(), this.getColNames(),
        this.getColTypes());
    disciplines.column("Name").primaryKey().setNotNullColumns().create();
    this.table = disciplines;
    return this;
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
