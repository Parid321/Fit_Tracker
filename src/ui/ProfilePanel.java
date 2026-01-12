package ui;

import app.AppSession;
import dao.UserDao;
import model.User;
import service.NotificationService;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
  private final JTextField name = new JTextField();
  private final JTextField age = new JTextField();
  private final JTextField weight = new JTextField();
  private final JTextField goal = new JTextField();

  private final UserDao userDao = new UserDao();
  private final NotificationService notif = new NotificationService();

  public ProfilePanel() {
    setLayout(new GridBagLayout());
    GridBagConstraints g = new GridBagConstraints();
    g.insets = new Insets(6,6,6,6);
    g.fill = GridBagConstraints.HORIZONTAL;

    int r=0;
    addRow(g, r++, "Name:", name);
    addRow(g, r++, "Age:", age);
    addRow(g, r++, "Weight kg:", weight);
    addRow(g, r++, "Goal:", goal);

    JButton save = new JButton("Save Profile");
    g.gridx=0; g.gridy=r; g.gridwidth=2;
    add(save, g);

    save.addActionListener(e -> save());

    load();
  }

  private void addRow(GridBagConstraints g, int row, String label, JComponent field){
    g.gridx=0; g.gridy=row; g.gridwidth=1;
    add(new JLabel(label), g);
    g.gridx=1; g.gridy=row;
    add(field, g);
  }

  private void load(){
    User u = AppSession.getUser();
    name.setText(u.name);
    age.setText(u.age == null ? "" : String.valueOf(u.age));
    weight.setText(u.weight == null ? "" : String.valueOf(u.weight));
    goal.setText(u.goal == null ? "" : u.goal);
  }

  private void save(){
    try{
      User u = AppSession.getUser();
      u.name = name.getText().trim();
      u.age = age.getText().trim().isEmpty() ? null : Integer.parseInt(age.getText().trim());
      u.weight = weight.getText().trim().isEmpty() ? null : Double.parseDouble(weight.getText().trim());
      u.goal = goal.getText().trim().isEmpty() ? null : goal.getText().trim();

      if(u.name.isEmpty()){
        JOptionPane.showMessageDialog(this, "Name nuk mund të jetë bosh.");
        return;
      }

      userDao.updateProfile(u);
      notif.info(u.userID, "Profile updated.");
      JOptionPane.showMessageDialog(this, "Saved.");
    }catch(NumberFormatException ex){
      JOptionPane.showMessageDialog(this, "Age/Weight duhet të jenë numra.");
    }catch(Exception e){
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }
}

