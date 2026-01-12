package service;

public class CaloriesService {
  public int calc(double met, int minutes, double weightKg) {
    double w = (weightKg > 0) ? weightKg : 70.0;
    double hours = Math.max(0, minutes) / 60.0;
    return (int)Math.round(met * w * hours);
  }
}
