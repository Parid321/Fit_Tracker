package ui;

import app.AppSession;
import model.User;
import service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
  private final JTextField email = new JTextField();
  private final JPasswordField pass = new JPasswordField();
  private final AuthService auth = new AuthService();

  public LoginFrame() {
    setTitle("FitTrack - Login");
    setSize(420, 240);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel p = new JPanel(new GridBagLayout());
    GridBagConstraints g = new GridBagConstraints();
    g.insets = new Insets(6,6,6,6);
    g.fill = GridBagConstraints.HORIZONTAL;

    g.gridx=0; g.gridy=0; p.add(new JLabel("Email:"), g);
    g.gridx=1; g.gridy=0; p.add(email, g);

    g.gridx=0; g.gridy=1; p.add(new JLabel("Password:"), g);
    g.gridx=1; g.gridy=1; p.add(pass, g);

    JButton btnLogin = new JButton("Login");
    JButton btnRegister = new JButton("Register");

    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    actions.add(btnRegister);
    actions.add(btnLogin);

    g.gridx=0; g.gridy=2; g.gridwidth=2;
    p.add(actions, g);

    btnLogin.addActionListener(e -> doLogin());
    btnRegister.addActionListener(e -> {
      new RegisterFrame(this).setVisible(true);
      setVisible(false);
    });

    setContentPane(p);
  }

  private void doLogin() {
    try {
      String em = email.getText().trim();
      String pw = new String(pass.getPassword());

      if (em.isEmpty() || pw.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Plotëso email dhe password.");
        return;
      }

      User u = auth.login(em, pw);
      if (u == null) {
        JOptionPane.showMessageDialog(this, "Kredenciale të pasakta.");
        return;
      }

      AppSession.setUser(u);
      new MainFrame().setVisible(true);
      dispose();
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Gabim: " + ex.getMessage());
    }
  }
}
