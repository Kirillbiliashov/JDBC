import java.sql.*;
import java.util.function.Consumer;

public class SelectBuilder {
  private final Connection conn;
  private String selectStr;

  public SelectBuilder(final Connection conn, final String selectStr) {
    this.conn = conn;
    this.selectStr = selectStr;
  }

  public SelectBuilder where(final String colName, final String operator,
                             final String value) {
    selectStr += " WHERE " + this.buildExpression(colName, operator, value);
    return this;
  }

  public SelectBuilder whereNot(final String colName, final String operator,
                                final String value) {
    selectStr += " WHERE NOT " + this.buildExpression(colName, operator, value);
    return this;
  }

  public SelectBuilder and(final String colName, final String operator,
                           final String value) {
    selectStr += " AND " + this.buildExpression(colName, operator, value);
    return this;
  }

  public SelectBuilder andNot(final String colName, final String operator,
                              final String value) {
    selectStr += " AND " + this.buildExpression(colName, operator, value);
    return this;
  }

  public SelectBuilder or(final String colName, final String operator,
                          final String value) {
    selectStr += " OR " + this.buildExpression(colName, operator, value);
    return this;
  }

  private String buildExpression(final String colName, final String operator,
                                 final String value) {
    return colName + " " + operator + " " + value;
  }

  public void execute(final Consumer<ResultSet> fn) {
    try (final Statement stmt = this.conn.createStatement()) {
      final ResultSet rs = stmt.executeQuery(this.selectStr);
      fn.accept(rs);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
