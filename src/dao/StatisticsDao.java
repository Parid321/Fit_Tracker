package dao;

import db.Db;
import model.Statistic;

import java.sql.*;
import java.util.*;

public class StatisticsDao {

  public void insert(Statistic s) throws SQLException {
    String sql = "INSERT INTO Statistics(userID, period, total_activities, total_calories, avg_progress) VALUES(?,?,?,?,?)";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, s.userID);
      ps.setString(2, s.period);
      ps.setInt(3, s.totalActivities);
      ps.setInt(4, s.totalCalories);
      ps.setInt(5, s.avgProgress);
      ps.executeUpdate();
    }
  }

  public List<Statistic> list(long userID) throws SQLException {
    String sql = "SELECT * FROM Statistics WHERE userID=? ORDER BY generated_at DESC, statsID DESC";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, userID);
      try (ResultSet rs = ps.executeQuery()) {
        List<Statistic> out = new ArrayList<>();
        while (rs.next()) out.add(map(rs));
        return out;
      }
    }
  }

  private Statistic map(ResultSet rs) throws SQLException {
    Statistic s = new Statistic();
    s.statsID = rs.getLong("statsID");
    s.userID = rs.getLong("userID");
    s.period = rs.getString("period");
    s.totalActivities = rs.getInt("total_activities");
    s.totalCalories = rs.getInt("total_calories");
    s.avgProgress = rs.getInt("avg_progress");
    s.generatedAt = rs.getString("generated_at");
    return s;
  }
}
