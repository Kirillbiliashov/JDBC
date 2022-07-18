import controllers.*;
import org.junit.jupiter.api.*;
import src.SQLTableLogger;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class RecordsCountTest {

  private int recordsCount = 0;
  private final static int EXPECTED_STUDENTS_COUNT = 8;
  private final static int EXPECTED_GROUPS_COUNT = 15;
  private final static int EXPECTED_DISCIPLINES_COUNT = 9;
  private final static int EXPECTED_GROUP_DISCIPLINE_COUNT = 22;

  private void countRecords(final TableController controller) throws Exception {
    controller.instantiateTable().populateTable().getTable().select()
        .execute(rs -> {
          try {
            SQLTableLogger.create(rs).printTable();
            rs.last();
            this.recordsCount = rs.getRow();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        });
  }

  private void testRecordsCount(final int expCount,
                                final TableController controller) {
    try {
      this.countRecords(controller);
      assertEquals(expCount, this.recordsCount);
      this.recordsCount = 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testTableRecordsCount() {
    this.testRecordsCount(EXPECTED_GROUPS_COUNT, new GroupsTableController());
    this.testRecordsCount(EXPECTED_DISCIPLINES_COUNT,
        new DisciplinesTableController());
    this.testRecordsCount(EXPECTED_STUDENTS_COUNT, new StudentsTableController());
    this.testRecordsCount(EXPECTED_GROUP_DISCIPLINE_COUNT,
        new GroupDisciplineTableController());
  }

}
