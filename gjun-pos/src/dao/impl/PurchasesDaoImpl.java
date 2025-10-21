package dao.impl;

import model.Purchases;
import util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchasesDaoImpl {
    public int insert(Purchases p) {
        String sql = "INSERT INTO purchases(product_id, qty, cost) VALUES(?,?,?)";
        try (Connection conn = DbConnection.getDb(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getProductId());
            ps.setInt(2, p.getQty());
            ps.setBigDecimal(3, p.getCost());
            int affected = ps.executeUpdate();
            if (affected>0) try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) p.setId(keys.getInt(1)); }
            return affected;
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public List<Purchases> findAll() {
        String sql = "SELECT * FROM purchases ORDER BY purchased_at DESC";
        List<Purchases> list = new ArrayList<>();
        try (Connection conn = DbConnection.getDb(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Purchases p = new Purchases();
                p.setId(rs.getInt("id"));
                p.setProductId(rs.getInt("product_id"));
                p.setQty(rs.getInt("qty"));
                p.setCost(rs.getBigDecimal("cost"));
                p.setPurchasedAt(rs.getTimestamp("purchased_at").toLocalDateTime());
                list.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
