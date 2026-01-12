package tests;

import db.Db;

import java.io.File;

public final class TestBootstrap {
  private TestBootstrap() {}

  public static void initDb() {
    // përdor DB të veçantë për test
    System.setProperty("fittrack.db.url", "jdbc:sqlite:fittrack_test.db");

    // reset DB file çdo herë (që testet të jenë deterministike)
    File f = new File("fittrack_test.db");
    if (f.exists()) {
      // në Windows ndonjëherë duhet pak durim nëse connection s’është mbyllur
      boolean deleted = f.delete();
      if (!deleted) {
        // nëse s’fshihet, të paktën vazhdojmë; por zakonisht fshihet pa problem
        System.out.println("[WARN] Could not delete fittrack_test.db (maybe still open).");
      }
    }

    // krijo tables + seed nga zero
    Db.init();
  }
}

