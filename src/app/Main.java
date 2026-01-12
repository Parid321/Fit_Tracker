package app;

import db.Db;
import ui.LoginFrame;

import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        Db.init(); // create tables + seed MET
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "DB init failed: " + e.getMessage());
        return;
      }
      new LoginFrame().setVisible(true);
    });
  }
}
