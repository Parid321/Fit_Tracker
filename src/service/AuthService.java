package service;

import dao.UserDao;
import model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class AuthService {
  private final UserDao userDao = new UserDao();

  public User login(String email, String password) throws Exception {
    User u = userDao.findByEmail(email);
    if (u == null) return null;
    String hash = sha256(password);
    return hash.equals(u.passwordHash) ? u : null;
  }

  public User register(String name, String email, Integer age, Double weight, String goal, String password) throws Exception {
    if (userDao.findByEmail(email) != null) return null;

    User u = new User();
    u.name = name;
    u.email = email.toLowerCase();
    u.age = age;
    u.weight = weight;
    u.goal = goal;
    u.passwordHash = sha256(password);

    long id = userDao.insert(u);
    u.userID = id;
    return u;
  }

  private String sha256(String s) throws Exception {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] b = md.digest(s.getBytes(StandardCharsets.UTF_8));
    StringBuilder sb = new StringBuilder();
    for (byte x : b) sb.append(String.format("%02x", x));
    return sb.toString();
  }
}

