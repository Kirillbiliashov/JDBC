package src;

import controllers.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AlterTableTest {

  private final Table studentsTable;
  private final int INITIAL_COLS_COUNT = 7;

  public AlterTableTest() throws Exception {
    studentsTable = new StudentsTableController().instantiateTable().getTable();
  }

  @Test
  void addColumnTest() {
    try {
      studentsTable.columns().add("Hobby", "varchar(200)");
      studentsTable.select().execute(rs -> {
        try {
          final int newColsCount = rs.getMetaData().getColumnCount();
          assertEquals(INITIAL_COLS_COUNT + 1, newColsCount);
        } catch (Exception e) {
          e.printStackTrace();
        }
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
        try {
          final int newColsCount = rs.getMetaData().getColumnCount();
          assertEquals(INITIAL_COLS_COUNT - 1, newColsCount);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void modifyColumnTypeTest() {
    final int COL_IDX = 6;
    final String NEW_TYPE = "TINYTEXT";
    try {
      studentsTable.columns().modify("Address", NEW_TYPE);
      studentsTable.select().execute(rs -> {
        try {
          assertEquals(NEW_TYPE, rs.getMetaData().getColumnTypeName(COL_IDX));
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @AfterEach
  void recreateTable() {
    try {
      studentsTable.drop();
      new StudentsTableController().instantiateTable().populateTable();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
