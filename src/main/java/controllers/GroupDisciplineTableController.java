package controllers;

import src.Table;

import java.sql.Connection;

public final class GroupDisciplineTableController extends TableController {

  public void instantiateTable(final Connection conn) throws Exception {
    final Table groupDisciplineTable = new Table(this.getTableName(),
        this.getColNames(), this.getColTypes(), conn);
    groupDisciplineTable.setNotNullColumns()
        .setForeignKey("GroupName", "UniversityGroups", "Name", true)
        .setForeignKey("DisciplineName", "Disciplines", "Name", true)
        .setPrimaryKeyField("GroupName", "DisciplineName")
        .create();
    this.table = groupDisciplineTable;
  }

  protected String getTableName() {
    return "GroupDiscipline";
  }

  protected String[] getColNames() {
    return new String[]{"GroupName", "DisciplineName"};
  }

  protected String[] getColTypes() {
    return new String[]{"char(4)", "varchar(50)"};
  }

  protected String[][] getData() {
    return new String[][]{
        {"IO81", "Back-end Development"}, {"IP92", "Back-end Development"},
        {"IM11", "Basics Of Programming"}, {"IM12", "Basics Of Programming"},
        {"IM13", "Basics Of Programming"}, {"IP12", "Basics Of Programming"},
        {"IP92", "Devops Basics"}, {"IM11", "English"}, {"IM12", "English"},
        {"IM13", "English"}, {"IO81", "English"}, {"IP02", "English"},
        {"IP12", "English"}, {"IP92", "English"}, {"IM11", "History"},
        {"IM12", "History"}, {"IM13", "History"}, {"IP12", "History"},
        {"IP02", "Linux"}, {"IO81", "Mobile Development"},
        {"IP02", "Philosophy"}, {"IP02", "Theory of Probability"}
    };
  }

}
