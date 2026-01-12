package ui;

import app.AppSession;
import model.User;
import service.AuthService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
  private final JTextField name = new JTextField();
  private final JTextField email = new JTextField();
  private final JTextField age = new JTextField();
  private final JTextField weight = new JTextField();
  private final JTextField goal = new JTextField();
  private final JPasswordField pass = new JPasswordField();

  private final AuthService auth = new AuthService();
  private final JFrame backTo;

  public RegisterFrame(JFrame backTo) {
    this.backTo = backTo;

    setTitle("FitTrack - Register");
    setSize(520, 360);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel p = new JPanel(new GridBagLayout());
    GridBagConstraints g = new GridBagConstraints();
    g.insets = new Insets(6,6,6,6);
    g.fill = GridBagConstraints.HORIZONTAL;

    int r=0;
    addRow(p,g,r++,"Name:",name);
    addRow(p,g,r++,"Email:",email);
    addRow(p,g,r++,"Age (optional):",age);
    addRow(p,g,r++,"Weight kg (optional):",weight);
    addRow(p,g,r++,"Goal (optional):",goal);
    addRow(p,g,r++,"Password:",pass);

    JButton btnBack = new JButton("Back");
    JButton btnCreate = new JButton("Create Account");

    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    actions.add(btnBack);
    actions.add(btnCreate);

    g.gridx=0; g.gridy=r; g.gridwidth=2;
    p.add(actions, g);

    btnBack.addActionListener(e -> {
      backTo.setVisible(true);
      dispose();
    });

    btnCreate.addActionListener(e -> doRegister());

    setContentPane(p);
  }

  private void addRow(JPanel p, GridBagConstraints g, int row, String label, JComponent field){
    g.gridx=0; g.gridy=row; g.gridwidth=1;
    p.add(new JLabel(label), g);
    g.gridx=1; g.gridy=row;
    p.add(field, g);
  }

  private void doRegister(){
    try {
      String nm = name.getText().trim();
      String em = email.getText().trim();
      String pw = new String(pass.getPassword());
      if (nm.isEmpty() || em.isEmpty() || pw.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Name, Email, Password janë të detyrueshme.");
        return;
      }

      Integer a = age.getText().trim().isEmpty() ? null : Integer.parseInt(age.getText().trim());
      Double w = weight.getText().trim().isEmpty() ? null : Double.parseDouble(weight.getText().trim());
      String gl = goal.getText().trim().isEmpty() ? null : goal.getText().trim();

      User u = auth.register(nm, em, a, w, gl, pw);
      if (u == null) {
        JOptionPane.showMessageDialog(this, "Ky email ekziston. Provo login.");
        return;
      }

      AppSession.setUser(u);
      new MainFrame().setVisible(true);
      dispose();
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this, "Age/Weight duhet të jenë numra.");
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Gabim: " + ex.getMessage());
    }
  }
}
