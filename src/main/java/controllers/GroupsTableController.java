package controllers;

import src.Table;

import java.sql.Connection;

public final class GroupsTableController extends TableController {

  public GroupsTableController instantiateTable() throws Exception {
    final Table groupsTable = new Table(this.getTableName(), this.getColNames(),
        this.getColTypes());
    groupsTable.column("Name").primaryKey().setNotNullColumns().create();
    this.table = groupsTable;
    return this;
  }

  protected String getTableName() {
    return "UniversityGroups";
  }

  protected String[] getColNames() {
    return new String[]{"Name", "Course", "Semester"};
  }

  protected String[] getColTypes() {
    return new String[]{"char(4)", "int", "int"};
  }

  protected String[][] getData() {
    return new String[][]{
        {"IA11", "1", "2"}, {"IA12", "1", "2"}, {"IA13", "1", "2"},
        {"IA14", "1", "2"}, {"IK11", "1", "2"}, {"IM01", "2", "4"},
        {"IM11", "1", "2"}, {"IM12", "1", "2"}, {"IM13", "1", "2"},
        {"IO81", "4", "8"}, {"IP01", "2", "4"}, {"IP02", "2", "4"},
        {"IP03", "2", "4"}, {"IP12", "1", "2"}, {"IP92", "3", "6"}
    };
  }

}
