package src;

import controllers.*;

import java.sql.*;

public class Demo {

  private final static TableController[] controllers = {
      new GroupsTableController(),
      new DisciplinesTableController(),
      new StudentsTableController(),
      new GroupDisciplineTableController()
  };

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(ConnectionProperties.URL,
        ConnectionProperties.USERNAME, ConnectionProperties.PASSWORD);
  }

  public static void main(String[] args) {
    try (final Connection conn = Demo.getConnection()) {
      conn.setAutoCommit(false);
      for (final TableController controller : controllers) {
        controller.instantiateTable(conn);
        controller.populateTable();
      }
      conn.commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
