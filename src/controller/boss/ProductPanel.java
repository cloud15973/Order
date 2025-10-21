package controller.boss;

import service.impl.ProductServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import model.Product;

public class ProductPanel extends JPanel {
    private JTable table;
    private ProductServiceImpl productService = new ProductServiceImpl();

    public ProductPanel() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Product Management"));
        JPanel top = new JPanel();
        JButton btnAdd = new JButton("Add"); btnAdd.setToolTipText("Add new product");
        JButton btnEdit = new JButton("Edit"); btnEdit.setToolTipText("Edit selected product");
        JButton btnDelete = new JButton("Delete"); btnDelete.setToolTipText("Delete selected product");
        top.add(btnAdd); top.add(btnEdit); top.add(btnDelete);
        add(top, BorderLayout.NORTH);

        table = new JTable();
        table.setRowHeight(26);
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadData();
    }

    private void loadData() {
        java.util.List<Product> list = productService.listAll();
        String[] cols = new String[]{"ID","SKU","Name","Price","Stock"};
        DefaultTableModel m = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
        for (Product p: list) {
            m.addRow(new Object[]{p.getId(), p.getSku(), p.getName(), p.getPrice(), p.getStock()});
        }
        table.setModel(m);
    }
}
