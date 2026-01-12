package ui;

import app.AppSession;
import dao.ActivityDao;
import dao.MetDao;
import dao.UserDao;
import model.Activity;
import model.User;
import service.CaloriesService;
import service.NotificationService;
import util.CsvUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ActivitiesPanel extends JPanel {
  private final JComboBox<String> type = new JComboBox<>(new String[]{
    "Walking","Running","Cycling","Gym","Yoga","Football","Swimming"
  });
  private final JTextField date = new JTextField(LocalDate.now().toString());
  private final JTextField distance = new JTextField("0");
  private final JTextField duration = new JTextField("30");
  private final JTextField notes = new JTextField("");

  // FILTER
  private final JTextField fromDate = new JTextField("");
  private final JTextField toDate = new JTextField("");

  private final DefaultTableModel model = new DefaultTableModel(
    new Object[]{"ID","Date","Type","Distance(km)","Duration(min)","Calories","Notes"}, 0
  ) {
    @Override public boolean isCellEditable(int row, int col) { return false; }
  };

  private final JTable table = new JTable(model);

  private final ActivityDao activityDao = new ActivityDao();
  private final MetDao metDao = new MetDao();
  private final UserDao userDao = new UserDao();
  private final CaloriesService caloriesService = new CaloriesService();
  private final NotificationService notif = new NotificationService();

  public ActivitiesPanel() {
    setLayout(new BorderLayout());

    // ADD FORM (top)
    JPanel form = new JPanel(new GridLayout(2, 6, 8, 8));
    form.setBorder(BorderFactory.createTitledBorder("Add Activity"));

    form.add(new JLabel("Date (YYYY-MM-DD)"));
    form.add(new JLabel("Type"));
    form.add(new JLabel("Distance km"));
    form.add(new JLabel("Duration min"));
    form.add(new JLabel("Notes"));
    form.add(new JLabel(" "));

    form.add(date);
    form.add(type);
    form.add(distance);
    form.add(duration);
    form.add(notes);

    JButton add = new JButton("Add");
    form.add(add);
    add.addActionListener(e -> addActivity());

    // FILTER BAR (under form)
    JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
    filterBar.setBorder(BorderFactory.createTitledBorder("Search / Filter"));

    filterBar.add(new JLabel("From:"));
    fromDate.setPreferredSize(new Dimension(110, 24));
    filterBar.add(fromDate);

    filterBar.add(new JLabel("To:"));
    toDate.setPreferredSize(new Dimension(110, 24));
    filterBar.add(toDate);

    JButton applyFilter = new JButton("Apply");
    JButton clearFilter = new JButton("Clear");
    JButton exportCsv = new JButton("Export CSV");

    filterBar.add(applyFilter);
    filterBar.add(clearFilter);
    filterBar.add(exportCsv);

    applyFilter.addActionListener(e -> loadFiltered());
    clearFilter.addActionListener(e -> {
      fromDate.setText("");
      toDate.setText("");
      load();
    });
    exportCsv.addActionListener(e -> exportCsv());

    // ACTIONS bottom
    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton refresh = new JButton("Refresh");
    JButton delete = new JButton("Delete Selected");

    actions.add(refresh);
    actions.add(delete);

    refresh.addActionListener(e -> loadFilteredOrAll());
    delete.addActionListener(e -> deleteSelected());

    // Layout
    JPanel top = new JPanel(new BorderLayout());
    top.add(form, BorderLayout.NORTH);
    top.add(filterBar, BorderLayout.SOUTH);

    add(top, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);
    add(actions, BorderLayout.SOUTH);

    load();
  }

  private void addActivity() {
    try {
      long uid = AppSession.getUser().userID;

      String d = date.getText().trim();
      String t = (String) type.getSelectedItem();
      double dist = Double.parseDouble(distance.getText().trim());
      int dur = Integer.parseInt(duration.getText().trim());
      String n = notes.getText().trim();

      if (d.isEmpty() || t == null || dur <= 0) {
        JOptionPane.showMessageDialog(this, "Date/Type/Duration janë të detyrueshme.");
        return;
      }

      // simple date validation
      LocalDate.parse(d);

      User u = userDao.findById(uid);
      double met = metDao.getMetForType(t);
      double w = (u.weight == null) ? 70.0 : u.weight;
      int kcal = caloriesService.calc(met, dur, w);

      Activity a = new Activity();
      a.userID = uid;
      a.type = t;
      a.distanceKm = dist;
      a.durationMin = dur;
      a.calories = kcal;
      a.activityDate = d;
      a.notes = n.isEmpty() ? null : n;

      long id = activityDao.insert(a);
      notif.info(uid, "Activity added: " + t + " (" + dur + " min, " + kcal + " kcal)");

      // reset
      notes.setText("");
      duration.setText("30");
      distance.setText("0");
      date.setText(LocalDate.now().toString());

      loadFilteredOrAll();
      JOptionPane.showMessageDialog(this, "Saved! activityID=" + id);
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this, "Distance/Duration duhet të jenë numra.");
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage() + "\nDate format: YYYY-MM-DD");
    }
  }

  private void deleteSelected() {
    try {
      int row = table.getSelectedRow();
      if (row < 0) {
        JOptionPane.showMessageDialog(this, "Zgjidh një rresht.");
        return;
      }
      long id = Long.parseLong(model.getValueAt(row, 0).toString());
      long uid = AppSession.getUser().userID;
      activityDao.delete(id, uid);
      notif.info(uid, "Activity deleted: ID " + id);
      loadFilteredOrAll();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
  }

  private void loadFilteredOrAll() {
    if ((fromDate.getText() != null && !fromDate.getText().trim().isEmpty()) ||
        (toDate.getText() != null && !toDate.getText().trim().isEmpty())) {
      loadFiltered();
    } else {
      load();
    }
  }

  private void load() {
    try {
      model.setRowCount(0);
      List<Activity> list = activityDao.listForUser(AppSession.getUser().userID);
      fillTable(list);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }

  private void loadFiltered() {
    try {
      String f = fromDate.getText().trim();
      String t = toDate.getText().trim();

      if (!f.isEmpty()) LocalDate.parse(f);
      if (!t.isEmpty()) LocalDate.parse(t);

      model.setRowCount(0);
      List<Activity> list = activityDao.listForUserFiltered(AppSession.getUser().userID, f, t);
      fillTable(list);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage() + "\nDate format: YYYY-MM-DD");
    }
  }

  private void fillTable(List<Activity> list) {
    for (Activity a : list) {
      model.addRow(new Object[]{
        a.activityID, a.activityDate, a.type,
        a.distanceKm, a.durationMin, a.calories,
        a.notes == null ? "" : a.notes
      });
    }
  }

  private void exportCsv() {
    try {
      String f = fromDate.getText().trim();
      String t = toDate.getText().trim();
      if (!f.isEmpty()) LocalDate.parse(f);
      if (!t.isEmpty()) LocalDate.parse(t);

      List<Activity> list;
      if (!f.isEmpty() || !t.isEmpty()) {
        list = activityDao.listForUserFiltered(AppSession.getUser().userID, f, t);
      } else {
        list = activityDao.listForUser(AppSession.getUser().userID);
      }

      JFileChooser fc = new JFileChooser();
      fc.setDialogTitle("Save CSV");
      fc.setSelectedFile(new File("activities.csv"));
      int res = fc.showSaveDialog(this);
      if (res != JFileChooser.APPROVE_OPTION) return;

      File file = fc.getSelectedFile();
      CsvUtil.exportActivities(file, list);

      JOptionPane.showMessageDialog(this, "CSV exported:\n" + file.getAbsolutePath());
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Export error: " + e.getMessage());
    }
  }
}
