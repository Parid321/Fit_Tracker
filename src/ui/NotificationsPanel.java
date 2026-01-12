package ui;

import app.AppSession;
import dao.NotificationDao;
import model.AppNotification;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NotificationsPanel extends JPanel {
  private final DefaultTableModel model = new DefaultTableModel(
    new Object[]{"ID","Created","Status","Content"}, 0
  );
  private final JTable table = new JTable(model);
  private final NotificationDao dao = new NotificationDao();

  public NotificationsPanel() {
    setLayout(new BorderLayout());

    JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton refresh = new JButton("Refresh");
    JButton read = new JButton("Mark Read");
    JButton del = new JButton("Delete");

    refresh.addActionListener(e -> load());
    read.addActionListener(e -> markRead());
    del.addActionListener(e -> delete());

    top.add(refresh); top.add(read); top.add(del);

    add(top, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);

    load();
  }

  private void markRead() {
    try {
      int row = table.getSelectedRow();
      if (row < 0) { JOptionPane.showMessageDialog(this, "Zgjidh një njoftim."); return; }
      long id = Long.parseLong(model.getValueAt(row, 0).toString());
      dao.markRead(id, AppSession.getUser().userID);
      load();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }

  private void delete() {
    try {
      int row = table.getSelectedRow();
      if (row < 0) { JOptionPane.showMessageDialog(this, "Zgjidh një njoftim."); return; }
      long id = Long.parseLong(model.getValueAt(row, 0).toString());
      dao.delete(id, AppSession.getUser().userID);
      load();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }

  private void load() {
    try {
      model.setRowCount(0);
      List<AppNotification> list = dao.list(AppSession.getUser().userID);
      for (AppNotification n : list) {
        model.addRow(new Object[]{ n.notifID, n.createdAt, n.status, n.content });
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }
}

