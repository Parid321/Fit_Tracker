package util;

import model.Activity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvUtil {

  public static void exportActivities(File file, List<Activity> list) throws IOException {
    try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
      // header
      w.write("activityID,date,type,distance_km,duration_min,calories,notes\n");

      for (Activity a : list) {
        w.write(a.activityID + ",");
        w.write(escape(a.activityDate) + ",");
        w.write(escape(a.type) + ",");
        w.write(a.distanceKm + ",");
        w.write(a.durationMin + ",");
        w.write(a.calories + ",");
        w.write(escape(a.notes));
        w.write("\n");
      }
    }
  }

  private static String escape(String s) {
    if (s == null) return "";
    String x = s.replace("\"", "\"\"");
    // nëse ka presje/newline, e fusim në quotes
    if (x.contains(",") || x.contains("\n") || x.contains("\r")) return "\"" + x + "\"";
    return x;
  }
}
