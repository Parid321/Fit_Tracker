package tests;

import org.junit.jupiter.api.Test;
import service.CaloriesService;

import static org.junit.jupiter.api.Assertions.*;

public class CaloriesServiceTest {

  @Test
  void calories_formula_metWeightMinutes() {
    CaloriesService cs = new CaloriesService();
    // MET=8, weight=80kg, minutes=60 => 8*80*1 = 640
    assertEquals(640, cs.calc(8.0, 60, 80.0));
  }

  @Test
  void calories_defaultWeightWhenInvalid() {
    CaloriesService cs = new CaloriesService();
    // default 70kg: 3.5*70*1h = 245
    assertEquals(245, cs.calc(3.5, 60, 0));
  }

  @Test
  void calories_zeroMinutes_isZero() {
    CaloriesService cs = new CaloriesService();
    assertEquals(0, cs.calc(5.0, 0, 70.0));
  }
}
