import java.sql.*;
import java.sql.SQLException;

public class Demo {
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(ConnectionProperties.URL,
        ConnectionProperties.USERNAME, ConnectionProperties.PASSWORD);
  }

  public static void main(String[] args) {
    final String[] colNames = {"Id", "LastName", "FirstName", "Gender",
        "BirthDate", "Address", "UniversityGroup"};
    final String[] colTypes = {"int", "varchar(20)", "varchar(20)", "varchar(6)",
        "Date", "varchar(50)", "char(4)"};
    try (Connection conn = Demo.getConnection()) {
      final Table students = new Table("Students", colNames, colTypes, conn);
      students.setPrimaryKeyField(1)
          .setAutoIncCol("Id")
          .setForeignKey("UniversityGroup", "UniversityGroups", "Name")
          .setNotNullColumns()
          .setUniqueCol("LastName")
          .create();
      students.insert("Petrov", "Vasiliy", "Male", Date.valueOf("2000-12-10"), "some street", "IM13");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
