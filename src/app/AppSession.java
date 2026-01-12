package app;

import model.User;

public class AppSession {
  private static User currentUser;

  public static User getUser() { return currentUser; }
  public static void setUser(User u) { currentUser = u; }
  public static void clear() { currentUser = null; }
  public static boolean isLoggedIn() { return currentUser != null; }
}
