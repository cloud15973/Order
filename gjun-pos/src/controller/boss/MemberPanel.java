package controller.boss;

import model.Member;
import service.impl.MemberServiceImpl;
import util.ExcelExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class MemberPanel extends JPanel {
    private JTable table;
    private MemberServiceImpl service = new MemberServiceImpl();

    public MemberPanel() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Member Management"));
        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Add"); btnAdd.setToolTipText("Add new member");
        JButton btnEdit = new JButton("Edit"); btnEdit.setToolTipText("Edit selected member");
        JButton btnDelete = new JButton("Delete"); btnDelete.setToolTipText("Delete selected member");
        JButton btnExport = new JButton("Export XLSX"); btnExport.setToolTipText("Export members to XLSX");
        Dimension bsize = new Dimension(110,28);
        btnAdd.setPreferredSize(bsize); btnEdit.setPreferredSize(bsize); btnDelete.setPreferredSize(bsize); btnExport.setPreferredSize(new Dimension(140,28));
        top.add(btnAdd); top.add(btnEdit); top.add(btnDelete); top.add(btnExport);
        add(top, BorderLayout.NORTH);

        table = new JTable();
        table.setRowHeight(26);
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadData();

        btnAdd.addActionListener(e -> openEditor(null));
        btnEdit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r==-1) return;
            int id = (int)table.getValueAt(r,0);
            Member m = service.listAll().stream().filter(x->x.getId()==id).findFirst().orElse(null);
            openEditor(m);
        });
        btnDelete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r==-1) return;
            int id = (int)table.getValueAt(r,0);
            int ok = JOptionPane.showConfirmDialog(this, "Delete member?","Confirm", JOptionPane.YES_NO_OPTION);
            if (ok==JOptionPane.YES_OPTION) { service.delete(id); loadData(); }
        });
        btnExport.addActionListener(e -> {
            try {
                List<Member> list = service.listAll();
                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File(System.getProperty("user.home"), "members.xlsx"));
                int r = fc.showSaveDialog(this);
                if (r==JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    ExcelExporter.exportMembers(list, f.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Exported members to: " + f.getAbsolutePath());
                }
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage()); }
        });
    }

    private void loadData() {
        java.util.List<Member> list = service.listAll();
        String[] cols = new String[]{"ID","Account","Name","Email","Phone","Role"};
        DefaultTableModel m = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
        for (Member p: list) {
            m.addRow(new Object[]{p.getId(), p.getAccount(), p.getName(), p.getEmail(), p.getPhone(), p.getRole()});
        }
        table.setModel(m);
    }

    private void openEditor(Member m) {
        JTextField account = new JTextField(); JTextField name = new JTextField(); JTextField email = new JTextField();
        JTextField phone = new JTextField(); JTextField role = new JTextField(); JPasswordField pwd = new JPasswordField();
        if (m!=null) { account.setText(m.getAccount()); name.setText(m.getName()); email.setText(m.getEmail()); phone.setText(m.getPhone()); role.setText(m.getRole()); }
        JPanel p = new JPanel(new GridLayout(0,2,5,5));
        p.setPreferredSize(new Dimension(360,220));
        p.add(new JLabel("Account")); p.add(account);
        p.add(new JLabel("Password (leave blank to keep)")); p.add(pwd);
        p.add(new JLabel("Name")); p.add(name);
        p.add(new JLabel("Email")); p.add(email);
        p.add(new JLabel("Phone")); p.add(phone);
        p.add(new JLabel("Role")); p.add(role);
        int ok = JOptionPane.showConfirmDialog(this, p, m==null?"Add":"Edit", JOptionPane.OK_CANCEL_OPTION);
        if (ok==JOptionPane.OK_OPTION) {
            if (m==null) m = new Member();
            m.setAccount(account.getText().trim());
            if (pwd.getPassword().length>0) m.setPassword(new String(pwd.getPassword()));
            m.setName(name.getText().trim());
            m.setEmail(email.getText().trim());
            m.setPhone(phone.getText().trim());
            m.setRole(role.getText().trim());
            if (m.getId()==0) service.register(m); else service.update(m);
            loadData();
        }
    }
}
