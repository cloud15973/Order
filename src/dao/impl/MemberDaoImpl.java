package dao.impl;

import dao.MemberDao;
import model.Member;
import util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDaoImpl implements MemberDao {

    @Override
    public Member findById(int id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try (Connection conn = DbConnection.getDb(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToMember(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Member findByAccount(String account) {
        String sql = "SELECT * FROM member WHERE account = ?";
        try (Connection conn = DbConnection.getDb(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToMember(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM member ORDER BY id";
        List<Member> list = new ArrayList<>();
        try (Connection conn = DbConnection.getDb(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToMember(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public int insert(Member m) {
        String sql = "INSERT INTO member(account, password, name, email, phone, role) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DbConnection.getDb(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getAccount());
            ps.setString(2, m.getPassword());
            ps.setString(3, m.getName());
            ps.setString(4, m.getEmail());
            ps.setString(5, m.getPhone());
            ps.setString(6, m.getRole());
            int affected = ps.executeUpdate();
            if (affected == 0) return 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) m.setId(keys.getInt(1));
            }
            return affected;
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int update(Member m) {
        String sql = "UPDATE member SET password=?, name=?, email=?, phone=?, role=? WHERE id=?";
        try (Connection conn = DbConnection.getDb(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getPassword());
            ps.setString(2, m.getName());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getPhone());
            ps.setString(5, m.getRole());
            ps.setInt(6, m.getId());
            return ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM member WHERE id = ?";
        try (Connection conn = DbConnection.getDb(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    private Member mapRowToMember(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setId(rs.getInt("id"));
        m.setAccount(rs.getString("account"));
        m.setPassword(rs.getString("password"));
        m.setName(rs.getString("name"));
        m.setEmail(rs.getString("email"));
        m.setPhone(rs.getString("phone"));
        m.setRole(rs.getString("role"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) m.setCreatedAt(ts.toLocalDateTime());
        return m;
    }
}
