package service;

import dao.ActivityDao;
import dao.StatisticsDao;
import model.Statistic;

import java.time.LocalDate;

public class StatisticsService {
  private final ActivityDao activityDao = new ActivityDao();
  private final StatisticsDao statisticsDao = new StatisticsDao();

  public Statistic generate(long userID, String period) throws Exception {
    int days = "Monthly".equals(period) ? 30 : 7;
    LocalDate to = LocalDate.now();
    LocalDate from = to.minusDays(days - 1);

    String fromISO = from.toString();
    String toISO = to.toString();

    int totalCalories = activityDao.sumCaloriesBetween(userID, fromISO, toISO);
    int totalActs = activityDao.countBetween(userID, fromISO, toISO);

    Statistic s = new Statistic();
    s.userID = userID;
    s.period = period;
    s.totalActivities = totalActs;
    s.totalCalories = totalCalories;
    s.avgProgress = (int)Math.round(totalCalories / (double)days);

    statisticsDao.insert(s);
    return s;
  }
}
