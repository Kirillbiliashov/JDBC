package src;

public class Column {

  private final String name;
  private  String type;
  private final String LINE_SEPARATOR = ", \n";
  private boolean isNotNull;
  private boolean isUnique;
  private boolean isAutoIncrement;
  private boolean isPrimaryKey;
  private String defaultValue;
  private String referenceCol;
  private String checkStr;
  private boolean isCascadeDelete;

  public Column(final String name, final String type) {
    this.name = name;
    this.type = type;
  }

  public String toString() {
    return this.name + " " + this.type + this.getNotNullStr() +
        this.getAutoIncStr() + this.getDefaultValueStr() +
        this.getUniqueStr() + this.getPrimaryKeyStr() +
        this.getForeignKeyStr() + this.getCheckStr();
  }

  protected void setDataType(final String dataType) {
    this.type = dataType;
  }

  protected void setNotNull() {
    this.isNotNull = true;
  }

  protected void setUnique() {
    this.isUnique = true;

  }

  protected void setAutoIncrement() {
    this.isAutoIncrement = true;

  }

  protected void setDefaultValue(final String defaultValue) {
    this.defaultValue = defaultValue;
  }

  protected void setPrimaryKey() {
    this.isPrimaryKey = true;

  }

  protected void setReferenceCol(final String tableName,
                                 final String foreignField,
                                 final boolean isCascadeDelete) {
    this.referenceCol = tableName + "(" + foreignField + ")";
    this.isCascadeDelete = isCascadeDelete;
  }

  protected void setCheck(final String operator, final String value) {
    this.checkStr = operator + value;
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

  private String getCheckStr() {
    return this.checkStr == null ? "" : LINE_SEPARATOR + "CHECK (" +
        this.name + this.checkStr + ")";
  }

}
