package ui;

import app.AppSession;
import dao.ActivityDao;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DashboardPanel extends JPanel {
  private final JLabel kcalToday = new JLabel("0");
  private final JLabel actsToday = new JLabel("0");

  public DashboardPanel() {
    setLayout(new BorderLayout());

    JPanel box = new JPanel(new GridLayout(2,2,10,10));
    box.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));

    box.add(new JLabel("Calories today:"));
    box.add(kcalToday);
    box.add(new JLabel("Activities today:"));
    box.add(actsToday);

    add(box, BorderLayout.NORTH);

    JButton refresh = new JButton("Refresh");
    refresh.addActionListener(e -> load());
    add(refresh, BorderLayout.SOUTH);

    load();
  }

  private void load() {
    try {
      ActivityDao dao = new ActivityDao();
      String today = LocalDate.now().toString();
      long uid = AppSession.getUser().userID;
      int kcal = dao.sumCaloriesBetween(uid, today, today);
      int cnt = dao.countBetween(uid, today, today);
      kcalToday.setText(String.valueOf(kcal));
      actsToday.setText(String.valueOf(cnt));
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }
}
