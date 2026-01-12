package dao;

import db.Db;
import model.User;

import java.sql.*;

public class UserDao {

  public User findByEmail(String email) throws SQLException {
    String sql = "SELECT * FROM Users WHERE email=?";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, email.toLowerCase());
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? map(rs) : null;
      }
    }
  }

  public User findById(long id) throws SQLException {
    String sql = "SELECT * FROM Users WHERE userID=?";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? map(rs) : null;
      }
    }
  }

  public long insert(User u) throws SQLException {
    String sql = "INSERT INTO Users(name,email,age,weight,goal,password_hash) VALUES(?,?,?,?,?,?)";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, u.name);
      ps.setString(2, u.email.toLowerCase());
      if (u.age == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, u.age);
      if (u.weight == null) ps.setNull(4, Types.REAL); else ps.setDouble(4, u.weight);
      ps.setString(5, u.goal);
      ps.setString(6, u.passwordHash);
      ps.executeUpdate();
      try (ResultSet keys = ps.getGeneratedKeys()) {
        return keys.next() ? keys.getLong(1) : -1;
      }
    }
  }

  public void updateProfile(User u) throws SQLException {
    String sql = "UPDATE Users SET name=?, age=?, weight=?, goal=? WHERE userID=?";
    try (Connection c = Db.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, u.name);
      if (u.age == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, u.age);
      if (u.weight == null) ps.setNull(3, Types.REAL); else ps.setDouble(3, u.weight);
      ps.setString(4, u.goal);
      ps.setLong(5, u.userID);
      ps.executeUpdate();
    }
  }

  private User map(ResultSet rs) throws SQLException {
    User u = new User();
    u.userID = rs.getLong("userID");
    u.name = rs.getString("name");
    u.email = rs.getString("email");
    int age = rs.getInt("age");
    u.age = rs.wasNull() ? null : age;
    double w = rs.getDouble("weight");
    u.weight = rs.wasNull() ? null : w;
    u.goal = rs.getString("goal");
    u.passwordHash = rs.getString("password_hash");
    return u;
  }
}

