package src;

import controllers.*;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AlterTableTest {

  private final static StudentsTableController controller = new StudentsTableController();
  private static final int INITIAL_COLS_COUNT = 7;
  private static Table studentsTable;

  static {
    try {
      studentsTable = controller.instantiateTable().populateTable().getTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  void addColumnTest() {
    try {
      studentsTable.columns().add("Hobby", "varchar(200)");
      studentsTable.select().execute(rs -> {
        final int newColsCount = rs.getMetaData().getColumnCount();
        assertEquals(INITIAL_COLS_COUNT + 1, newColsCount);
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void modifyColumnTest() {
    final int COL_IDX = 6;
    final String NEW_TYPE = "TINYTEXT";
    try {
      studentsTable.columns().modify("Address", NEW_TYPE);
      studentsTable.select().execute(rs -> {
        assertEquals(NEW_TYPE, rs.getMetaData().getColumnTypeName(COL_IDX));
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void dropColumnTest() {
    try {
      studentsTable.columns().drop("Address");
      studentsTable.select().execute(rs -> {
        final int newColsCount = rs.getMetaData().getColumnCount();
        assertEquals(INITIAL_COLS_COUNT - 1, newColsCount);
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void createIndexTest() {
    final String IDX_NAME = "Last_Name_Idx";
    try {
      studentsTable.columns().createIndex(IDX_NAME, true, "LastName");
      final String showIndexStr = "SHOW INDEX FROM Students";
      final ResultSet rs = StatementExecutor.executeQueryStatement(showIndexStr);
      boolean indexFound = false;
      while (rs.next()) {
        if (IDX_NAME.equals(rs.getString("Key_Name"))) {
          indexFound = true;
          break;
        }
      }
      assertTrue(indexFound);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @AfterEach
  void resetColumnModifications() {
    try {
      studentsTable.drop();
      studentsTable = controller.instantiateTable().populateTable().getTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
