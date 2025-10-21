package service.impl;

import dao.impl.OrderItemsDaoImpl;
import dao.impl.OrdersDaoImpl;
import dao.impl.ProductDaoImpl;
import model.OrderItems;
import model.Orders;
import model.Product;
import util.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrdersServiceImpl {
    private OrdersDaoImpl ordersDao = new OrdersDaoImpl();
    private OrderItemsDaoImpl itemsDao = new OrderItemsDaoImpl();
    private ProductDaoImpl productDao = new ProductDaoImpl();

    // place order with transaction
    public boolean placeOrder(Orders order, List<OrderItems> items) {
        Connection conn = null;
        try {
            conn = DbConnection.getDb();
            conn.setAutoCommit(false);

            // insert order
            ordersDao.insert(conn, order);

            // for each item: lock product row, check stock, insert item, update stock
            for (OrderItems it : items) {
                Product p = productDao.findById(conn, it.getProductId());
                if (p == null) throw new SQLException("Product not found: " + it.getProductId());
                int remain = p.getStock() - it.getQty();
                if (remain < 0) throw new SQLException("Insufficient stock for product " + p.getId());
                // insert order item
                it.setOrderId(order.getId());
                itemsDao.insert(conn, it);
                // update stock
                productDao.updateStock(conn, p.getId(), remain);
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
