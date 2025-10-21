package dao;

import model.Orders;
import java.sql.Connection;
import java.util.List;

public interface OrdersDao {
    int insert(java.sql.Connection conn, Orders o) throws Exception;
    Orders findById(java.sql.Connection conn, int id) throws Exception;
    List<Orders> findByMemberId(int memberId);
}
