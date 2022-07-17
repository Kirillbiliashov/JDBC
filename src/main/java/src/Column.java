package src;

import java.util.ArrayList;
import java.util.List;

public final class Column {

  private final String name;
  private final String LINE_SEPARATOR = ", \n";
  private String type;
  private boolean isNotNull;
  private boolean isUnique;
  private boolean isAutoIncrement;
  private boolean isPrimaryKey;
  private String defaultValue;
  private String referenceCol;
  private List<String> checkStrList;
  private boolean isCascadeDelete;

  public Column(final String name, final String type) {
    this.name = name;
    this.type = type;
    this.checkStrList = new ArrayList<>();
  }

  public String toString() {
    return this.name + " " + this.type + this.getNotNullStr() +
        this.getAutoIncStr() + this.getDefaultValueStr() +
        this.getUniqueStr() + this.getPrimaryKeyStr() +
        this.getForeignKeyStr() + this.getColumnChecks();
  }

  public void setDataType(final String dataType) {
    this.type = dataType;
  }

  void setNotNull() {
    this.isNotNull = true;
  }

  void setUnique() {
    this.isUnique = true;
  }

  void setAutoIncrement() {
    this.isAutoIncrement = true;
  }

  void setDefaultValue(final String defaultValue) {
    this.defaultValue = defaultValue;
  }

  void setPrimaryKey() {
    this.isPrimaryKey = true;
  }

  void setReferenceCol(final String tableName, final String foreignField,
                       final boolean isCascadeDelete) {
    this.referenceCol = tableName + "(" + foreignField + ")";
    this.isCascadeDelete = isCascadeDelete;
  }

  void setCheck(final String operator, final String value) {
    final String checkStr = operator + value;
    if (!this.checkStrList.contains(checkStr)) {
      this.checkStrList.add(checkStr);
    }
  }

  public boolean isAutoIncrement() {
    return this.isAutoIncrement;
  }

  public String getName() {
    return this.name;
  }

  private String getNotNullStr() {
    return this.isNotNull ? " NOT NULL" : "";
  }

  private String getAutoIncStr() {
    return this.isAutoIncrement ? " AUTO_INCREMENT" : "";
  }

  private String getDefaultValueStr() {
    return defaultValue == null ? "" : " DEFAULT " + defaultValue;
  }

  private String getPrimaryKeyStr() {
    return this.isPrimaryKey ? LINE_SEPARATOR + "PRIMARY KEY (" + this.name +
        ")" : "";
  }

  private String getUniqueStr() {
    return isUnique ? LINE_SEPARATOR + "UNIQUE(" + this.name + ")" : "";
  }

  private String getForeignKeyStr() {
    final String cascadeDeleteStr = this.isCascadeDelete ? " ON DELETE CASCADE" : "";
    return this.referenceCol == null ? "" : LINE_SEPARATOR + "FOREIGN KEY " +
        this.name + " REFERENCES " + this.referenceCol + cascadeDeleteStr;
  }

  private String getColumnChecks() {
    return String.join(", ", this.checkStrList.stream()
        .map(str -> "CHECK (" + this.name + str + ")").toList());
  }

}
