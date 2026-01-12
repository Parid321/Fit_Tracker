package ui;

import app.AppSession;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
  public MainFrame() {
    setTitle("FitTrack Desktop");
    setSize(980, 620);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JTabbedPane tabs = new JTabbedPane();

    tabs.addTab("Dashboard", new DashboardPanel());
    tabs.addTab("Activities", new ActivitiesPanel());
    tabs.addTab("Statistics", new StatsPanel());
    tabs.addTab("Notifications", new NotificationsPanel());
    tabs.addTab("Devices", new DevicesPanel());
    tabs.addTab("Profile", new ProfilePanel());

    JButton logout = new JButton("Logout");
    logout.addActionListener(e -> {
      AppSession.clear();
      new LoginFrame().setVisible(true);
      dispose();
    });

    JPanel top = new JPanel(new BorderLayout());
    top.add(new JLabel("  Logged in as: " + AppSession.getUser().email), BorderLayout.WEST);
    top.add(logout, BorderLayout.EAST);

    setLayout(new BorderLayout());
    add(top, BorderLayout.NORTH);
    add(tabs, BorderLayout.CENTER);
  }
}
