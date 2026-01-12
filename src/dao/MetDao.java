package dao;

import db.Db;

import java.sql.*;

public class MetDao {
  public double getMetForType(String type) throws SQLException {
    String sql = "SELECT met FROM MetValues WHERE type=?";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, type);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getDouble(1) : 4.0;
      }
    }
  }
}

