package dao.impl;

import model.Orders;
import util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDaoImpl {

    public int insert(Connection conn, Orders o) throws SQLException {
        String sql = "INSERT INTO orders(member_id, total, status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getMemberId());
            ps.setBigDecimal(2, o.getTotal());
            ps.setString(3, o.getStatus());
            int affected = ps.executeUpdate();
            if (affected>0) try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) o.setId(keys.getInt(1)); }
            return affected;
        }
    }

    public Orders findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return mapRow(rs); }
        }
        return null;
    }

    public List<Orders> findByMemberId(int memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ? ORDER BY created_at DESC";
        List<Orders> list = new ArrayList<>();
        try (Connection conn = DbConnection.getDb(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(mapRow(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Orders mapRow(ResultSet rs) throws SQLException {
        Orders o = new Orders();
        o.setId(rs.getInt("id"));
        o.setMemberId(rs.getInt("member_id"));
        o.setTotal(rs.getBigDecimal("total"));
        o.setStatus(rs.getString("status"));
        o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return o;
    }
}
