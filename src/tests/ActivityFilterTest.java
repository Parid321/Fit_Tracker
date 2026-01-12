package tests;

import dao.ActivityDao;
import dao.UserDao;
import model.Activity;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityFilterTest {

  @BeforeEach
  void init() {
    TestBootstrap.initDb();
  }

  @Test
  void filter_between_dates_returnsOnlyInRange() throws Exception {
    // create user
    UserDao userDao = new UserDao();
    User u = new User();
    u.name = "U";
    u.email = "u@test.com";
    u.passwordHash = "x".repeat(64);
    long uid = userDao.insert(u);

    // insert activities
    ActivityDao aDao = new ActivityDao();
    insertAct(aDao, uid, "Running", "2026-01-01", 300);
    insertAct(aDao, uid, "Walking", "2026-01-05", 80);
    insertAct(aDao, uid, "Gym", "2026-01-10", 220);

    // filter
    List<Activity> list = aDao.listForUserFiltered(uid, "2026-01-02", "2026-01-09");
    assertEquals(1, list.size());
    assertEquals("Walking", list.get(0).type);
    assertEquals("2026-01-05", list.get(0).activityDate);
  }

  @Test
  void filter_onlyFromDate() throws Exception {
    UserDao userDao = new UserDao();
    User u = new User();
    u.name = "U2";
    u.email = "u2@test.com";
    u.passwordHash = "x".repeat(64);
    long uid = userDao.insert(u);

    ActivityDao aDao = new ActivityDao();
    insertAct(aDao, uid, "Walking", "2026-01-05", 80);
    insertAct(aDao, uid, "Gym", "2026-01-10", 220);

    List<Activity> list = aDao.listForUserFiltered(uid, "2026-01-06", "");
    assertEquals(1, list.size());
    assertEquals("Gym", list.get(0).type);
  }

  @Test
  void filter_onlyToDate() throws Exception {
    UserDao userDao = new UserDao();
    User u = new User();
    u.name = "U3";
    u.email = "u3@test.com";
    u.passwordHash = "x".repeat(64);
    long uid = userDao.insert(u);

    ActivityDao aDao = new ActivityDao();
    insertAct(aDao, uid, "Running", "2026-01-01", 300);
    insertAct(aDao, uid, "Walking", "2026-01-05", 80);
    insertAct(aDao, uid, "Gym", "2026-01-10", 220);

    List<Activity> list = aDao.listForUserFiltered(uid, "", "2026-01-05");
    assertEquals(2, list.size());
    // ordered DESC, so first should be 2026-01-05
    assertEquals("2026-01-05", list.get(0).activityDate);
  }

  private void insertAct(ActivityDao dao, long uid, String type, String date, int cal) throws Exception {
    Activity a = new Activity();
    a.userID = uid;
    a.type = type;
    a.distanceKm = 0;
    a.durationMin = 10;
    a.calories = cal;
    a.activityDate = date;
    a.notes = null;
    dao.insert(a);
  }
}
