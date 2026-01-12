package dao;

import db.Db;
import model.Device;

import java.sql.*;
import java.util.*;

public class DeviceDao {

  public void insert(Device d) throws SQLException {
    String sql = "INSERT INTO Devices(userID, device_name, sync_status) VALUES(?,?,?)";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, d.userID);
      ps.setString(2, d.deviceName);
      ps.setString(3, d.syncStatus == null ? "Not Synced" : d.syncStatus);
      ps.executeUpdate();
    }
  }

  public List<Device> list(long userID) throws SQLException {
    String sql = "SELECT * FROM Devices WHERE userID=? ORDER BY deviceID DESC";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, userID);
      try (ResultSet rs = ps.executeQuery()) {
        List<Device> out = new ArrayList<>();
        while (rs.next()) out.add(map(rs));
        return out;
      }
    }
  }

  public void toggle(long deviceID, long userID) throws SQLException {
    String sql = "UPDATE Devices SET sync_status = CASE WHEN sync_status='Synced' THEN 'Not Synced' ELSE 'Synced' END WHERE deviceID=? AND userID=?";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, deviceID);
      ps.setLong(2, userID);
      ps.executeUpdate();
    }
  }

  public void delete(long deviceID, long userID) throws SQLException {
    String sql = "DELETE FROM Devices WHERE deviceID=? AND userID=?";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, deviceID);
      ps.setLong(2, userID);
      ps.executeUpdate();
    }
  }

  private Device map(ResultSet rs) throws SQLException {
    Device d = new Device();
    d.deviceID = rs.getLong("deviceID");
    d.userID = rs.getLong("userID");
    d.deviceName = rs.getString("device_name");
    d.syncStatus = rs.getString("sync_status");
    return d;
  }
}
