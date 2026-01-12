package ui;

import app.AppSession;
import dao.DeviceDao;
import model.Device;
import service.NotificationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DevicesPanel extends JPanel {
  private final JTextField name = new JTextField();
  private final DefaultTableModel model = new DefaultTableModel(
    new Object[]{"ID","Device Name","Sync Status"}, 0
  );
  private final JTable table = new JTable(model);

  private final DeviceDao dao = new DeviceDao();
  private final NotificationService notif = new NotificationService();

  public DevicesPanel() {
    setLayout(new BorderLayout());

    JPanel top = new JPanel(new BorderLayout(8,8));
    top.setBorder(BorderFactory.createTitledBorder("Add Device"));

    JPanel form = new JPanel(new GridLayout(1,2,8,8));
    form.add(new JLabel("Device name:"));
    form.add(name);

    JButton add = new JButton("Add");
    add.addActionListener(e -> addDevice());

    top.add(form, BorderLayout.CENTER);
    top.add(add, BorderLayout.EAST);

    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton refresh = new JButton("Refresh");
    JButton toggle = new JButton("Toggle Sync");
    JButton del = new JButton("Delete");
    actions.add(refresh); actions.add(toggle); actions.add(del);

    refresh.addActionListener(e -> load());
    toggle.addActionListener(e -> toggle());
    del.addActionListener(e -> delete());

    add(top, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);
    add(actions, BorderLayout.SOUTH);

    load();
  }

  private void addDevice(){
    try{
      String dn = name.getText().trim();
      if(dn.isEmpty()){
        JOptionPane.showMessageDialog(this, "Vendos emrin e pajisjes.");
        return;
      }
      Device d = new Device();
      d.userID = AppSession.getUser().userID;
      d.deviceName = dn;
      d.syncStatus = "Not Synced";
      dao.insert(d);
      notif.info(d.userID, "Device added: " + dn);
      name.setText("");
      load();
    }catch(Exception e){
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }

  private void toggle(){
    try{
      int row = table.getSelectedRow();
      if(row < 0){ JOptionPane.showMessageDialog(this, "Zgjidh një pajisje."); return; }
      long id = Long.parseLong(model.getValueAt(row,0).toString());
      dao.toggle(id, AppSession.getUser().userID);
      notif.info(AppSession.getUser().userID, "Device sync toggled: ID " + id);
      load();
    }catch(Exception e){
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }

  private void delete(){
    try{
      int row = table.getSelectedRow();
      if(row < 0){ JOptionPane.showMessageDialog(this, "Zgjidh një pajisje."); return; }
      long id = Long.parseLong(model.getValueAt(row,0).toString());
      dao.delete(id, AppSession.getUser().userID);
      notif.info(AppSession.getUser().userID, "Device deleted: ID " + id);
      load();
    }catch(Exception e){
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }

  private void load(){
    try{
      model.setRowCount(0);
      List<Device> list = dao.list(AppSession.getUser().userID);
      for(Device d: list){
        model.addRow(new Object[]{d.deviceID, d.deviceName, d.syncStatus});
      }
    }catch(Exception e){
      JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
  }
}

