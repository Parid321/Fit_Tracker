package ui;

import app.AppSession;
import dao.StatisticsDao;
import model.Statistic;
import service.NotificationService;
import service.StatisticsService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StatsPanel extends JPanel {
  private final JComboBox<String> period = new JComboBox<>(new String[]{"Weekly", "Monthly"});
  private final DefaultTableModel model = new DefaultTableModel(
    new Object[]{"ID","Period","Total Activities","Total Calories","Avg/Day","Generated At"}, 0
  );
  private final JTable table = new JTable(model);

  private final StatisticsService statsService = new StatisticsService();
  private final StatisticsDao statsDao = new StatisticsDao();
  private final NotificationService notif = new NotificationService();

  public StatsPanel() {
    setLayout(new BorderLayout());

    JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton gen = new JButton("Generate");
    JButton refresh = new JButton("Refresh");
    top.add(new JLabel("Period:"));
    top.add(period);
    top.add(gen);
    top.add(refresh);

    gen.addActionListener(e -> generate());
    refresh.addActionListener(e -> load());

    add(top, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);

    load();
  }

  private void generate() {
    try {
      long uid = AppSession.getUser().userID;
      String p = (String) period.getSelectedItem();
      Statistic s = statsService.generate(uid, p);
      notif.info(uid, "Statistics generated: " + p + " (" + s.totalCalories + " kcal)");
      load();
      JOptionPane.showMessageDialog(this, "Generated: " + p);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }

  private void load() {
    try {
      model.setRowCount(0);
      List<Statistic> list = statsDao.list(AppSession.getUser().userID);
      for (Statistic s : list) {
        model.addRow(new Object[]{
          s.statsID, s.period, s.totalActivities, s.totalCalories, s.avgProgress, s.generatedAt
        });
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }
}

