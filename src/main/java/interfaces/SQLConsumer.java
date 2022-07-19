package interfaces;

import java.sql.SQLException;

public interface SQLConsumer<T> {
  public void accept(T t) throws SQLException;
}