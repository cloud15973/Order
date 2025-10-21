package controller.sale;

import model.OrderItems;
import model.Orders;
import service.impl.OrdersServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CartPanel extends JPanel {
    private java.util.List<OrderItems> cart = new ArrayList<>();
    private OrdersServiceImpl ordersService = new OrdersServiceImpl();

    public CartPanel() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        JButton btnPlace = new JButton("Place Order");
        top.add(btnPlace);
        add(top, BorderLayout.NORTH);
        btnPlace.addActionListener(e -> placeOrder());
    }

    private void placeOrder() {
        Orders o = new Orders();
        o.setMemberId(1); // demo
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (OrderItems it: cart) total = total.add(it.getPrice().multiply(new java.math.BigDecimal(it.getQty())));
        o.setTotal(total);
        o.setStatus("NEW");
        boolean ok = ordersService.placeOrder(o, cart);
        JOptionPane.showMessageDialog(this, ok?"Order placed":"Order failed");
    }
}
