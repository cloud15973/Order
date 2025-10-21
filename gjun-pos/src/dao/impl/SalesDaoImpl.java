package dao.impl;

import util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class SalesDaoImpl {
    // Example: get total sales per product between dates
    public List<Map<String, Object>> getSalesSummary(Date from, Date to) {
        String sql = "SELECT p.id AS product_id, p.name, SUM(oi.qty) AS total_qty, SUM(oi.qty*oi.price) AS total_amount " +
                     "FROM order_items oi JOIN product p ON oi.product_id = p.id JOIN orders o ON oi.order_id = o.id " +
                     "WHERE o.created_at BETWEEN ? AND ? GROUP BY p.id, p.name";
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DbConnection.getDb(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(from.getTime()));
            ps.setTimestamp(2, new Timestamp(to.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,Object> row = new HashMap<>();
                    row.put("product_id", rs.getInt("product_id"));
                    row.put("name", rs.getString("name"));
                    row.put("total_qty", rs.getInt("total_qty"));
                    row.put("total_amount", rs.getBigDecimal("total_amount"));
                    list.add(row);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
