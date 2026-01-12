package db;

public final class Schema {
  private Schema(){}

  public static final String[] SQL = {
    "PRAGMA foreign_keys = ON;",

    "CREATE TABLE IF NOT EXISTS Users (" +
      "userID INTEGER PRIMARY KEY AUTOINCREMENT," +
      "name TEXT NOT NULL," +
      "email TEXT NOT NULL UNIQUE," +
      "age INTEGER," +
      "weight REAL," +
      "goal TEXT," +
      "password_hash TEXT NOT NULL" +
    ");",

    "CREATE TABLE IF NOT EXISTS MetValues (" +
      "type TEXT PRIMARY KEY," +
      "met REAL NOT NULL" +
    ");",

    "CREATE TABLE IF NOT EXISTS Activities (" +
      "activityID INTEGER PRIMARY KEY AUTOINCREMENT," +
      "userID INTEGER NOT NULL," +
      "type TEXT NOT NULL," +
      "distance_km REAL DEFAULT 0," +
      "duration_min INTEGER NOT NULL," +
      "calories INTEGER NOT NULL," +
      "activity_date TEXT NOT NULL," + // YYYY-MM-DD
      "notes TEXT," +
      "FOREIGN KEY(userID) REFERENCES Users(userID) ON DELETE CASCADE" +
    ");",

    "CREATE TABLE IF NOT EXISTS Statistics (" +
      "statsID INTEGER PRIMARY KEY AUTOINCREMENT," +
      "userID INTEGER NOT NULL," +
      "period TEXT NOT NULL," + // Weekly/Monthly
      "total_activities INTEGER NOT NULL," +
      "total_calories INTEGER NOT NULL," +
      "avg_progress INTEGER NOT NULL," +
      "generated_at TEXT DEFAULT (datetime('now'))," +
      "FOREIGN KEY(userID) REFERENCES Users(userID) ON DELETE CASCADE" +
    ");",

    "CREATE TABLE IF NOT EXISTS Notifications (" +
      "notifID INTEGER PRIMARY KEY AUTOINCREMENT," +
      "userID INTEGER NOT NULL," +
      "content TEXT NOT NULL," +
      "created_at TEXT DEFAULT (datetime('now'))," +
      "status TEXT NOT NULL DEFAULT 'Unread'," +
      "FOREIGN KEY(userID) REFERENCES Users(userID) ON DELETE CASCADE" +
    ");",

    "CREATE TABLE IF NOT EXISTS Devices (" +
      "deviceID INTEGER PRIMARY KEY AUTOINCREMENT," +
      "userID INTEGER NOT NULL," +
      "device_name TEXT NOT NULL," +
      "sync_status TEXT NOT NULL DEFAULT 'Not Synced'," +
      "FOREIGN KEY(userID) REFERENCES Users(userID) ON DELETE CASCADE" +
    ");"
  };

  public static final String[] SEED = {
    "INSERT OR IGNORE INTO MetValues(type, met) VALUES ('Walking',3.5);",
    "INSERT OR IGNORE INTO MetValues(type, met) VALUES ('Running',8.0);",
    "INSERT OR IGNORE INTO MetValues(type, met) VALUES ('Cycling',6.8);",
    "INSERT OR IGNORE INTO MetValues(type, met) VALUES ('Gym',5.0);",
    "INSERT OR IGNORE INTO MetValues(type, met) VALUES ('Yoga',2.5);",
    "INSERT OR IGNORE INTO MetValues(type, met) VALUES ('Football',7.0);",
    "INSERT OR IGNORE INTO MetValues(type, met) VALUES ('Swimming',6.0);"
  };
}

