package dao;

import model.OrderItems;
import java.sql.Connection;
import java.util.List;

public interface OrderItemsDao {
    int insert(java.sql.Connection conn, OrderItems it) throws Exception;
    List<OrderItems> findByOrderId(int orderId);
}
