package dao;

import db.Db;
import model.Activity;

import java.sql.*;
import java.util.*;

public class ActivityDao {

  public long insert(Activity a) throws SQLException {
    String sql = "INSERT INTO Activities(userID,type,distance_km,duration_min,calories,activity_date,notes) VALUES(?,?,?,?,?,?,?)";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setLong(1, a.userID);
      ps.setString(2, a.type);
      ps.setDouble(3, a.distanceKm);
      ps.setInt(4, a.durationMin);
      ps.setInt(5, a.calories);
      ps.setString(6, a.activityDate);
      ps.setString(7, a.notes);
      ps.executeUpdate();
      try (ResultSet keys = ps.getGeneratedKeys()) {
        return keys.next() ? keys.getLong(1) : -1;
      }
    }
  }

  public void delete(long activityID, long userID) throws SQLException {
    String sql = "DELETE FROM Activities WHERE activityID=? AND userID=?";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, activityID);
      ps.setLong(2, userID);
      ps.executeUpdate();
    }
  }

  public List<Activity> listForUser(long userID) throws SQLException {
    String sql = "SELECT * FROM Activities WHERE userID=? ORDER BY activity_date DESC, activityID DESC";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, userID);
      try (ResultSet rs = ps.executeQuery()) {
        List<Activity> out = new ArrayList<>();
        while (rs.next()) out.add(map(rs));
        return out;
      }
    }
  }

  public int sumCaloriesBetween(long userID, String fromISO, String toISO) throws SQLException {
    String sql = "SELECT COALESCE(SUM(calories),0) FROM Activities WHERE userID=? AND activity_date BETWEEN ? AND ?";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, userID);
      ps.setString(2, fromISO);
      ps.setString(3, toISO);
      try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
    }
  }

  public int countBetween(long userID, String fromISO, String toISO) throws SQLException {
    String sql = "SELECT COUNT(*) FROM Activities WHERE userID=? AND activity_date BETWEEN ? AND ?";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, userID);
      ps.setString(2, fromISO);
      ps.setString(3, toISO);
      try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
    }
  }

  private Activity map(ResultSet rs) throws SQLException {
    Activity a = new Activity();
    a.activityID = rs.getLong("activityID");
    a.userID = rs.getLong("userID");
    a.type = rs.getString("type");
    a.distanceKm = rs.getDouble("distance_km");
    a.durationMin = rs.getInt("duration_min");
    a.calories = rs.getInt("calories");
    a.activityDate = rs.getString("activity_date");
    a.notes = rs.getString("notes");
    return a;
  }

  public List<Activity> listForUserFiltered(long userID, String fromISO, String toISO) throws SQLException {
  String base = "SELECT * FROM Activities WHERE userID=? ";
  boolean hasFrom = fromISO != null && !fromISO.isBlank();
  boolean hasTo = toISO != null && !toISO.isBlank();

  if (hasFrom && hasTo) base += "AND activity_date BETWEEN ? AND ? ";
  else if (hasFrom) base += "AND activity_date >= ? ";
  else if (hasTo) base += "AND activity_date <= ? ";

  base += "ORDER BY activity_date DESC, activityID DESC";

  try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(base)) {
    int idx = 1;
    ps.setLong(idx++, userID);

    if (hasFrom && hasTo) {
      ps.setString(idx++, fromISO);
      ps.setString(idx++, toISO);
    } else if (hasFrom) {
      ps.setString(idx++, fromISO);
    } else if (hasTo) {
      ps.setString(idx++, toISO);
    }

    try (ResultSet rs = ps.executeQuery()) {
      List<Activity> out = new ArrayList<>();
      while (rs.next()) out.add(map(rs));
      return out;
    }
  }
}

}
