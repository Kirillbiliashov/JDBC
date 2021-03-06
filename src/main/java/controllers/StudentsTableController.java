package controllers;

import src.Table;

public final class StudentsTableController extends TableController {

  public StudentsTableController instantiateTable() throws Exception {
    final Table studentsTable = new Table(this.getTableName(),
        this.getColNames(), this.getColTypes());
    studentsTable.column("Id").primaryKey().autoIncrement()
        .column("UniversityGroup").reference("UniversityGroups", "Name", true)
        .setNotNullColumns().create();
    this.table = studentsTable;
    return this;
  }

  protected String getTableName() {
    return "Students";
  }

  protected String[] getColNames() {
    return new String[]{"Id", "LastName", "FirstName", "Gender", "BirthDate",
        "Address", "UniversityGroup"};
  }

  protected String[] getColTypes() {
    return new String[]{"int", "varchar(20)", "varchar(20)", "varchar(6)",
        "Date", "varchar(50)", "char(4)"};
  }

  protected String[][] getData() {
    return new String[][]{
        {"Johnson", "Dan", "Male", "2000-10-11", "abc street", "IM11"},
        {"Schweib", "Mark", "Male", "2000-08-02", "def street", "IP92"},
        {"Balor", "Finn", "Male", "2001-01-09", "ghi street", "IM01"},
        {"Wexler", "Kim", "Female", "2002-11-11", "jkl street", "IA13"},
        {"Goodman", "Saul", "Male", "2000-09-08", "mno street", "IP92"},
        {"Berg", "Paul", "Male", "2000-08-09", "pqr street", "IP92"},
        {"Alekseeva", "Marina", "Female", "2001-02-05", "stu street", "IP01"},
        {"Gonzalez", "Pedro", "Male", "2003-05-22", "xyz street", "IK11"}
    };
  }

}
