package service;

import dao.NotificationDao;

public class NotificationService {
  private final NotificationDao dao = new NotificationDao();

  public void info(long userID, String msg) {
    try { dao.insert(userID, msg); }
    catch (Exception ignored) {}
  }
}

