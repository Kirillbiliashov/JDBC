package src;

import controllers.*;
import org.junit.jupiter.api.*;
import queryBuilders.SelectQueryBuilder;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static src.ExpectedStatementResults.*;

public class SelectQueryTest {

  @Test
  void testWhereSelect() {
    try {
      new StudentsTableController().instantiateTable().getTable().select()
          .where("Gender", "=", "'Male'")
          .execute(rs -> this.resultSetConsumer(rs, WHERE));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testWhereOrSelect() {
    try {
      new StudentsTableController().instantiateTable().getTable().select()
          .where("Gender", "=", "'Male'")
          .or("LastName", "=", "'Wexler'")
          .execute(rs -> this.resultSetConsumer(rs, WHERE_OR));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testWhereNotSelect() {
    try {
      new StudentsTableController().instantiateTable().getTable().select()
          .whereNot("UniversityGroup", "=", "'IP92'")
          .execute(rs -> this.resultSetConsumer(rs, WHERE_NOT));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testOrderBySelect() {
    try {
      new StudentsTableController().instantiateTable().getTable().select()
          .orderBy("LastName", true)
          .execute(rs -> this.resultSetConsumer(rs, ORDER_BY));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testWhereAndSelect() {
    try {
      new StudentsTableController().instantiateTable().getTable().select()
          .where("Gender", "=", "'Male'")
          .and("LastName", "<>", "'Berg'")
          .execute(rs -> this.resultSetConsumer(rs, WHERE_AND));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testGroupBySelect() {
    try {
      new StudentsTableController().instantiateTable().getTable()
          .select("UniversityGroup", "COUNT(UniversityGroup)")
          .as("COUNT(UniversityGroup)", "Members")
          .groupBy("UniversityGroup")
          .execute(rs -> this.resultSetConsumer(rs, GROUP_BY));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testLimitSelect() {
    try {
      new StudentsTableController().instantiateTable().getTable()
          .select()
          .limit(4)
          .execute(rs -> this.resultSetConsumer(rs, LIMIT));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testJoinSelect() {
    try {
      new StudentsTableController().instantiateTable().getTable()
          .select("LastName", "FirstName", "Name", "Course")
          .join("UniversityGroups", "UniversityGroup", "Name")
          .execute(rs -> this.resultSetConsumer(rs, JOIN));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testRightJoinSelect() {
    try {
      new StudentsTableController().instantiateTable().getTable()
          .select("LastName", "FirstName", "Name", "Course")
          .rightJoin("UniversityGroups", "UniversityGroup", "Name")
          .execute(rs -> this.resultSetConsumer(rs, RIGHT_JOIN));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testLeftJoinSelect() {
    try {
      new GroupsTableController().instantiateTable().getTable()
          .select("Name", "Course", "LastName", "FirstName")
          .leftJoin("Students", "Name", "UniversityGroup")
          .execute(rs -> this.resultSetConsumer(rs, LEFT_JOIN));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testCrossJoinSelect() {
    try {
      new StudentsTableController().instantiateTable().getTable()
          .select()
          .crossJoin("UniversityGroups")
          .execute(rs -> this.resultSetConsumer(rs, CROSS_JOIN));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testWhereExistsSelect() {
    try {
      new StudentsTableController().instantiateTable().getTable()
          .select("LastName", "FirstName", "UniversityGroup")
          .whereExists(new SelectQueryBuilder("UniversityGroups",
              new String[]{"Course"}).where("Name", "=", "UniversityGroup")
              .and("Course", ">", "1"))
          .execute(rs -> this.resultSetConsumer(rs, WHERE_EXISTS));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testWhereAnySelect() {
    try {
      new StudentsTableController().instantiateTable().getTable()
          .select("LastName", "FirstName", "UniversityGroup")
          .whereAny("UniversityGroup", "=",
              new SelectQueryBuilder("UniversityGroups", new String[]{"Name"})
                  .where("Course", "=", "1"))
          .execute(rs -> this.resultSetConsumer(rs, WHERE_ANY));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testWhereAllSelect() {
    try {
      new StudentsTableController().instantiateTable().getTable()
          .select("LastName", "FirstName", "UniversityGroup")
          .whereAll("UniversityGroup", "=",
              new SelectQueryBuilder("UniversityGroups", new String[]{"Name"})
                  .where("Course", "=", "1"))
          .execute(rs -> this.resultSetConsumer(rs, WHERE_ALL));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void resultSetConsumer(ResultSet rs, final ExpectedStatementResults expRes) {
    try {
      SQLTableLogger.create(rs).printTable();
      rs.last();
      assertEquals(expRes.getExpRecordsCount(), rs.getRow());
      final String[] expColNames = expRes.getExpColNames();
      if (expColNames == null) return;
      final ResultSetMetaData metadata = rs.getMetaData();
      for (int i = 0; i < expColNames.length; i++) {
        assertEquals(expColNames[i], metadata.getColumnLabel(i + 1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
