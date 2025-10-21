package dao.impl;

import model.OrderItems;
import util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemsDaoImpl {
    public int insert(Connection conn, OrderItems it) throws SQLException {
        String sql = "INSERT INTO order_items(order_id, product_id, qty, price) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, it.getOrderId());
            ps.setInt(2, it.getProductId());
            ps.setInt(3, it.getQty());
            ps.setBigDecimal(4, it.getPrice());
            int affected = ps.executeUpdate();
            if (affected>0) try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) it.setId(keys.getInt(1)); }
            return affected;
        }
    }

    public List<OrderItems> findByOrderId(int orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        List<OrderItems> list = new ArrayList<>();
        try (Connection conn = DbConnection.getDb(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(mapRow(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private OrderItems mapRow(ResultSet rs) throws SQLException {
        OrderItems it = new OrderItems();
        it.setId(rs.getInt("id"));
        it.setOrderId(rs.getInt("order_id"));
        it.setProductId(rs.getInt("product_id"));
        it.setQty(rs.getInt("qty"));
        it.setPrice(rs.getBigDecimal("price"));
        return it;
    }
}
