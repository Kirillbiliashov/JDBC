import java.sql.*;
import java.sql.SQLException;

public class Demo {
  private final static String[] LAST_NAMES = {"Johnson", "Schweib", "Balor",
      "Wexler", "Goodman", "Berg", "Alekseeva", "Gonzalez"};
  private final static String[] FIRST_NAMES = {"Dan", "Mark", "Finn", "Kim",
      "Saul", "Paul", "Marina", "Pedro"};
  private final static String[] GENDERS = {"Male", "Male", "Male", "Female",
      "Male", "Male", "Female", "Male"};
  private final static String[] BIRTH_DATES = {"2000-10-11", "2000-08-02",
      "2001-01-09", "2002-11-11", "2000-09-08", "2000-08-09", "2001-02-05",
      "2003-05-22"};
  private final static String[] ADDRESSES = {"abc street", "def street",
      "ghi street", "jkl street", "mno street", "pqr street", "stu street",
      "xyz street"};
  private final static String[] GROUPS = {"IM11", "IP92", "IM01", "IA13",
      "IP92", "IP92", "IP01", "IK11"};
  private final static String[] COL_NAMES = {"Id", "LastName", "FirstName",
      "Gender", "BirthDate", "Address", "UniversityGroup"};
  private final static String[] COL_TYPES = {"int", "varchar(20)", "varchar(20)",
      "varchar(6)", "Date", "varchar(50)", "char(4)"};

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(ConnectionProperties.URL,
        ConnectionProperties.USERNAME, ConnectionProperties.PASSWORD);
  }

  public static void main(String[] args) {

    try (Connection conn = Demo.getConnection()) {
      final Table students = new Table("Students", COL_NAMES, COL_TYPES, conn);
      students.setPrimaryKeyField(1)
          .setAutoIncCol("Id")
          .setForeignKey("UniversityGroup", "UniversityGroups", "Name")
          .setNotNullColumns()
          .setUniqueCol("LastName")
          .create();
      for (int i = 0; i < LAST_NAMES.length; i++) {
        students.insert(LAST_NAMES[i], FIRST_NAMES[i], GENDERS[i],
            Date.valueOf(BIRTH_DATES[i]), ADDRESSES[i], GROUPS[i]);
      }
      students.select()
          .where("LastName", "IN", "('Balor', 'Gonzalez')")
          .or("Gender", "=", "'Female'")
          .orderBy("Gender", true)
          .orderBy("Id", false)
          .limit(3)
          .execute(rs -> {
            try {
              SQLTableLogger.create(rs).printTable();
            } catch (SQLException e) {
              e.printStackTrace();
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
