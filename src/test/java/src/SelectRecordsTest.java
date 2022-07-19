package src;

import controllers.*;
import org.junit.jupiter.api.*;
import queryBuilders.SelectQueryBuilder;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static src.ExpectedStatementResults.*;

public class SelectRecordsTest {

  private final Table studentsTable;
  private final Table groupsTable;

  public SelectRecordsTest() {
    studentsTable = new StudentsTableController().instantiateTable().getTable();
    groupsTable = new GroupsTableController().instantiateTable().getTable();
  }

  @Test
  void testWhereSelect() {
    try {
      studentsTable.select().where("Gender", "=", "'Male'")
          .execute(rs -> this.resultSetConsumer(rs, WHERE));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testWhereOrSelect() {
    try {
      studentsTable.select().where("Gender", "=", "'Male'")
          .or("LastName", "=", "'Wexler'")
          .execute(rs -> this.resultSetConsumer(rs, WHERE_OR));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testWhereNotSelect() {
    try {
      studentsTable.select().whereNot("UniversityGroup", "=", "'IP92'")
          .execute(rs -> this.resultSetConsumer(rs, WHERE_NOT));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testOrderBySelect() {
    try {
      studentsTable.select().orderBy("LastName", true)
          .execute(rs -> this.resultSetConsumer(rs, ORDER_BY));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testWhereAndSelect() {
    try {
      studentsTable.select().where("Gender", "=", "'Male'")
          .and("LastName", "<>", "'Berg'")
          .execute(rs -> this.resultSetConsumer(rs, WHERE_AND));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testGroupBySelect() {
    try {
      studentsTable.select("UniversityGroup", "COUNT(UniversityGroup)")
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
      studentsTable.select().limit(4)
          .execute(rs -> this.resultSetConsumer(rs, LIMIT));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testJoinSelect() {
    try {
      studentsTable.select("LastName", "FirstName", "Name", "Course")
          .join("UniversityGroups", "UniversityGroup", "Name")
          .execute(rs -> this.resultSetConsumer(rs, JOIN));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testRightJoinSelect() {
    try {
      studentsTable.select("LastName", "FirstName", "Name", "Course")
          .rightJoin("UniversityGroups", "UniversityGroup", "Name")
          .execute(rs -> this.resultSetConsumer(rs, RIGHT_JOIN));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testLeftJoinSelect() {
    try {
      groupsTable.select("Name", "Course", "LastName", "FirstName")
          .leftJoin("Students", "Name", "UniversityGroup")
          .execute(rs -> this.resultSetConsumer(rs, LEFT_JOIN));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testCrossJoinSelect() {
    try {
      studentsTable.select().crossJoin("UniversityGroups")
          .execute(rs -> this.resultSetConsumer(rs, CROSS_JOIN));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testWhereExistsSelect() {
    try {
      studentsTable.select("LastName", "FirstName", "UniversityGroup")
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
      studentsTable.select("LastName", "FirstName", "UniversityGroup")
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
      studentsTable.select("LastName", "FirstName", "UniversityGroup")
          .whereAll("UniversityGroup", "=",
              new SelectQueryBuilder("UniversityGroups", new String[]{"Name"})
                  .where("Course", "=", "1"))
          .execute(rs -> this.resultSetConsumer(rs, WHERE_ALL));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSelfJoin() {
    try {
      studentsTable.select("A.LastName", "B.LastName", "A.UniversityGroup")
          .as("A.LastName", "LastName1").as("B.LastName", "LastName2")
          .as("A.UniversityGroup", "UniversityGroup").selfJoin()
          .where("A.Id", "<>", "B.Id")
          .and("A.UniversityGroup", "=", "B.UniversityGroup")
          .execute(rs -> this.resultSetConsumer(rs, SELF_JOIN));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSelectInto() {
    try {
      final Table males = new Table("Males",
          new String[]{"Id", "LastName", "FirstName", "HomeAddress"},
          new String[]{"int", "varchar(20)", "varchar(20)", "varchar(50)"})
          .column("Id").autoIncrement().primaryKey().setNotNullColumns();
      males.create();
      final SelectQueryBuilder queryBuilder = studentsTable
          .select("LastName", "FirstName", "Address")
          .where("Gender", "=", "'Male'");
      studentsTable.insertInto("Males", queryBuilder, "LastName", "FirstName",
          "HomeAddress");
      males.select().execute(rs -> this.resultSetConsumer(rs, SELECT_INSERT));
      males.drop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testHaving() {
    try {
      studentsTable.select("COUNT(UniversityGroup)", "UniversityGroup")
          .as("COUNT(UniversityGroup)", "Count")
          .groupBy("UniversityGroup").having("Count", ">", "1")
          .execute(rs -> this.resultSetConsumer(rs, HAVING));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void resultSetConsumer(ResultSet rs,
                                 final ExpectedStatementResults expRes)
      throws SQLException {
    SQLTableLogger.create(rs).printTable();
    rs.last();
    assertEquals(expRes.getExpRecordsCount(), rs.getRow());
    final String[] expColNames = expRes.getExpColNames();
    if (expColNames == null) return;
    final ResultSetMetaData metadata = rs.getMetaData();
    for (int i = 0; i < expColNames.length; i++) {
      assertEquals(expColNames[i], metadata.getColumnLabel(i + 1));
    }
  }

}
