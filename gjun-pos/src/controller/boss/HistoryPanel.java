package controller.boss;

import dao.impl.OrdersDaoImpl;
import model.Orders;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoryPanel extends JPanel {
    private JTable table;
    private OrdersDaoImpl dao = new OrdersDaoImpl();

    public HistoryPanel() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        top.add(btnRefresh);
        add(top, BorderLayout.NORTH);
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        btnRefresh.addActionListener(e -> loadData());
        loadData();
    }

    private void loadData() {
        java.util.List<Orders> list = dao.findByMemberId(0); // placeholder: implement all in dao if needed
        String[] cols = new String[]{"ID","MemberID","Total","Status","CreatedAt"};
        DefaultTableModel m = new DefaultTableModel(cols,0){ public boolean isCellEditable(int r,int c){return false;} };
        for (Orders o: list) {
            m.addRow(new Object[]{o.getId(), o.getMemberId(), o.getTotal(), o.getStatus(), o.getCreatedAt()});
        }
        table.setModel(m);
    }
}
