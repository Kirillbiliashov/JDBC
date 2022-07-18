package src;

import controllers.StudentsTableController;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModifyRecordsTest {

  private final static StudentsTableController controller = new StudentsTableController();
  private static final int EXP_RECORDS_COUNT = 8;
  private static Table studentsTable;

  @BeforeAll
  static void instantiateTable() {
    try {
      studentsTable = controller.instantiateTable().getTable();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void deleteSingleStudentTest() {
    final int EXP_ROWS_MODIFIED = 1;
    try {
      studentsTable.delete()
          .where("LastName", "=", "'Gonzalez'")
          .execute(count -> assertEquals(EXP_ROWS_MODIFIED, count));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void deleteMultipleStudentsTest() {
    final int EXP_ROWS_MODIFIED = 2;
    try {
      studentsTable.delete()
          .where("Gender", "=", "'Female'")
          .execute(count -> assertEquals(EXP_ROWS_MODIFIED, count));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void updateSingleStudentTest() {
    final int EXP_ROWS_MODIFIED = 1;
    try {
      studentsTable.update().set("LastName", "'Brannagan'")
          .where("FirstName", "=", "'Finn'")
          .execute(count -> assertEquals(EXP_ROWS_MODIFIED, count));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void updateMultipleStudentsTest() {
    final int EXP_ROWS_MODIFIED = 2;
    try {
      studentsTable.update().set("Address", "'Black Square street'")
          .where("Gender", "=", "'Female'")
          .execute(count -> assertEquals(EXP_ROWS_MODIFIED, count));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void insertRecordTest() {
    try {
      studentsTable.insert("Herringhton", "Billy", "Male", "2000-05-05",
          "dungeon street", "IM13");
      studentsTable.select().execute(rs -> {
        try {
          rs.last();
          assertEquals(EXP_RECORDS_COUNT + 1, rs.getRow());
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @AfterEach
  void repopulateTable() {
    try {
      studentsTable.delete().execute(null);
      controller.populateTable();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
