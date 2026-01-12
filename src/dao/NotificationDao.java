package dao;

import db.Db;
import model.AppNotification;

import java.sql.*;
import java.util.*;

public class NotificationDao {

  public void insert(long userID, String content) throws SQLException {
    String sql = "INSERT INTO Notifications(userID, content, status) VALUES(?,?,'Unread')";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, userID);
      ps.setString(2, content);
      ps.executeUpdate();
    }
  }

  public List<AppNotification> list(long userID) throws SQLException {
    String sql = "SELECT * FROM Notifications WHERE userID=? ORDER BY created_at DESC, notifID DESC";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, userID);
      try (ResultSet rs = ps.executeQuery()) {
        List<AppNotification> out = new ArrayList<>();
        while (rs.next()) out.add(map(rs));
        return out;
      }
    }
  }

  public void markRead(long notifID, long userID) throws SQLException {
    String sql = "UPDATE Notifications SET status='Read' WHERE notifID=? AND userID=?";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, notifID);
      ps.setLong(2, userID);
      ps.executeUpdate();
    }
  }

  public void delete(long notifID, long userID) throws SQLException {
    String sql = "DELETE FROM Notifications WHERE notifID=? AND userID=?";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, notifID);
      ps.setLong(2, userID);
      ps.executeUpdate();
    }
  }

  private AppNotification map(ResultSet rs) throws SQLException {
    AppNotification n = new AppNotification();
    n.notifID = rs.getLong("notifID");
    n.userID = rs.getLong("userID");
    n.content = rs.getString("content");
    n.createdAt = rs.getString("created_at");
    n.status = rs.getString("status");
    return n;
  }
}

