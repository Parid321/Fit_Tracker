package db;

import java.sql.*;

public final class Db {

  // Default: file DB. Në teste do e vendosim: -Dfittrack.db.url=jdbc:sqlite::memory:
  private static final String DEFAULT_URL = "jdbc:sqlite:fittrack.db";

  private Db(){}

  public static String url() {
    String u = System.getProperty("fittrack.db.url");
    return (u == null || u.isBlank()) ? DEFAULT_URL : u;
  }

  public static Connection get() throws SQLException {
    return DriverManager.getConnection(url());
  }

  public static void init() {
    try (Connection c = get(); Statement st = c.createStatement()) {
      for (String s : Schema.SQL) st.execute(s);
      for (String s : Schema.SEED) st.execute(s);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
