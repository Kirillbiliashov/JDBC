package src;

import controllers.*;

import static src.ConnectionSingleton.*;

public class Demo {

  private final static TableController[] controllers = {
      new GroupsTableController(),
      new DisciplinesTableController(),
      new StudentsTableController(),
      new GroupDisciplineTableController()
  };

  public static void main(String[] args) {
    try {
      getConn().setAutoCommit(false);
      for (final TableController controller : controllers) {
        controller.instantiateTable();
        controller.populateTable();
      }
      getConn().commit();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeConn();
    }
  }

}
