package tests;

import dao.UserDao;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

  @BeforeEach
  void init() {
    TestBootstrap.initDb();
  }

  @Test
  void register_then_login_success() throws Exception {
    AuthService auth = new AuthService();

    User u = auth.register("Test", "t@test.com", 20, 70.0, "Goal", "secret123");
    assertNotNull(u);
    assertTrue(u.userID > 0);

    User logged = auth.login("t@test.com", "secret123");
    assertNotNull(logged);
    assertEquals("t@test.com", logged.email);
  }

  @Test
  void login_wrongPassword_fails() throws Exception {
    AuthService auth = new AuthService();
    auth.register("A", "a@test.com", null, null, null, "pass1");

    User logged = auth.login("a@test.com", "wrong");
    assertNull(logged);
  }

  @Test
  void register_duplicateEmail_returnsNull() throws Exception {
    AuthService auth = new AuthService();
    assertNotNull(auth.register("A", "dup@test.com", null, null, null, "p1"));
    assertNull(auth.register("B", "dup@test.com", null, null, null, "p2"));
  }

  @Test
  void storedPassword_isHashed_notPlainText() throws Exception {
    AuthService auth = new AuthService();
    auth.register("H", "hash@test.com", null, null, null, "plainpass");

    UserDao dao = new UserDao();
    User u = dao.findByEmail("hash@test.com");

    assertNotNull(u);
    assertNotEquals("plainpass", u.passwordHash);
    assertEquals(64, u.passwordHash.length()); // SHA-256 hex
  }
}
