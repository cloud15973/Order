package controller.boss;

import model.Purchases;
import model.Product;
import service.impl.PurchasesServiceImpl;
import service.impl.ProductServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class StockPanel extends JPanel {
    private JTable table;
    private PurchasesServiceImpl purchasesService = new PurchasesServiceImpl();
    private ProductServiceImpl productService = new ProductServiceImpl();

    public StockPanel() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        JButton btnAdd = new JButton("Add Purchase");
        JButton btnRefresh = new JButton("Refresh");
        top.add(btnAdd); top.add(btnRefresh);
        add(top, BorderLayout.NORTH);
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadData();
        btnAdd.addActionListener(e -> addPurchase());
        btnRefresh.addActionListener(e -> loadData());
    }

    private void loadData() {
        java.util.List<Purchases> list = purchasesService.listAll();
        String[] cols = new String[]{"ID","ProductID","Qty","Cost","PurchasedAt"};
        DefaultTableModel m = new DefaultTableModel(cols,0){ public boolean isCellEditable(int r,int c){return false;} };
        for (Purchases p: list) {
            m.addRow(new Object[]{p.getId(), p.getProductId(), p.getQty(), p.getCost(), p.getPurchasedAt()});
        }
        table.setModel(m);
    }

    private void addPurchase() {
        JTextField pid = new JTextField(); JTextField qty = new JTextField(); JTextField cost = new JTextField();
        JPanel p = new JPanel(new GridLayout(0,2));
        p.add(new JLabel("Product ID")); p.add(pid);
        p.add(new JLabel("Qty")); p.add(qty);
        p.add(new JLabel("Cost")); p.add(cost);
        int ok = JOptionPane.showConfirmDialog(this, p, "Add Purchase", JOptionPane.OK_CANCEL_OPTION);
        if (ok==JOptionPane.OK_OPTION) {
            try {
                int productId = Integer.parseInt(pid.getText().trim());
                int q = Integer.parseInt(qty.getText().trim());
                BigDecimal c = new BigDecimal(cost.getText().trim());
                Purchases pu = new Purchases(); pu.setProductId(productId); pu.setQty(q); pu.setCost(c);
                purchasesService.addPurchase(pu);
                // update product stock
                Product prod = productService.findById(productId);
                if (prod!=null) {
                    prod.setStock(prod.getStock() + q);
                    productService.update(prod);
                }
                loadData();
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        }
    }
}
