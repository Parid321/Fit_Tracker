package tests;

import model.Activity;
import org.junit.jupiter.api.Test;
import util.CsvUtil;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvUtilTest {

  @Test
  void export_csv_writesHeaderAndEscapesCommas() throws Exception {
    Activity a = new Activity();
    a.activityID = 1;
    a.activityDate = "2026-01-10";
    a.type = "Running";
    a.distanceKm = 3.2;
    a.durationMin = 25;
    a.calories = 260;
    a.notes = "hello, comma";

    File f = File.createTempFile("activities", ".csv");
    f.deleteOnExit();

    CsvUtil.exportActivities(f, List.of(a));

    String content = Files.readString(f.toPath());
    assertTrue(content.startsWith("activityID,date,type,distance_km,duration_min,calories,notes"));
    assertTrue(content.contains("\"hello, comma\""));
  }
}
