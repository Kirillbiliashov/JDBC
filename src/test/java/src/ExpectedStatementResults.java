package src;

public enum ExpectedStatementResults {

  WHERE(6, null),
  WHERE_OR(7, null),
  WHERE_NOT(5, null),
  ORDER_BY(8, null),
  WHERE_AND(5, null),
  GROUP_BY(6, new String[]{"UniversityGroup", "Members"}),
  LIMIT(4, null),
  JOIN(8, new String[]{"LastName", "FirstName", "Name", "Course"}),
  RIGHT_JOIN(17, new String[]{"LastName", "FirstName", "Name", "Course"}),
  LEFT_JOIN(17, new String[]{"Name", "Course", "LastName", "FirstName"}),
  CROSS_JOIN(120, null),
  WHERE_EXISTS(5, new String[]{"LastName", "FirstName", "UniversityGroup"}),
  WHERE_ALL(0, new String[]{"LastName", "FirstName", "UniversityGroup"}),
  WHERE_ANY(3, new String[]{"LastName", "FirstName", "UniversityGroup"});

  private int expRecordsCount;
  private String[] expColNames;

  ExpectedStatementResults(final int expRecordsCount,
                           final String[] expColNames) {
    this.expRecordsCount = expRecordsCount;
    this.expColNames = expColNames;
  }

  public int getExpRecordsCount() {
    return this.expRecordsCount;
  }

  public String[] getExpColNames(){
    return this.expColNames;
  }

}
