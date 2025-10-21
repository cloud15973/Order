package controller.boss;

import dao.impl.SalesDaoImpl;
import util.ExcelExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.io.File;

public class SalesPanel extends JPanel {
    private JTable table;
    private SalesDaoImpl dao = new SalesDaoImpl();

    public SalesPanel() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Sales Summary"));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnQuery = new JButton("Query"); btnQuery.setToolTipText("Query sales summary");
        JButton btnExport = new JButton("Export XLSX"); btnExport.setToolTipText("Export sales summary to XLSX");
        top.add(btnQuery); top.add(btnExport);
        add(top, BorderLayout.NORTH);
        table = new JTable();
        table.setRowHeight(26);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnQuery.addActionListener(e -> query());
        btnExport.addActionListener(e -> {
            try {
                Date from = Date.valueOf("2000-01-01"); Date to = new Date(System.currentTimeMillis());
                java.util.List<Map<String,Object>> list = dao.getSalesSummary(from, to);
                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File(System.getProperty("user.home"), "sales_summary.xlsx"));
                int r = fc.showSaveDialog(this);
                if (r==JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    ExcelExporter.exportSalesSummary(list, f.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Exported sales to: " + f.getAbsolutePath());
                }
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage()); }
        });
    }

    private void query() {
        Date from = Date.valueOf("2000-01-01"); Date to = new Date(System.currentTimeMillis());
        java.util.List<Map<String,Object>> list = dao.getSalesSummary(from, to);
        String[] cols = new String[]{"ProductID","Name","TotalQty","TotalAmount"};
        DefaultTableModel m = new DefaultTableModel(cols,0){ public boolean isCellEditable(int r,int c){return false;} };
        for (Map<String,Object> r: list) {
            m.addRow(new Object[]{r.get("product_id"), r.get("name"), r.get("total_qty"), r.get("total_amount")});
        }
        table.setModel(m);
    }
}
