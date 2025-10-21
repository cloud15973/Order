import org.junit.jupiter.api.Test;
import service.impl.OrdersServiceImpl;
import service.impl.ProductServiceImpl;
import model.Product;
import model.Orders;
import model.OrderItems;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class OrdersTransactionTest {
    @Test
    public void testPlaceOrderSuccessful() {
        ProductServiceImpl ps = new ProductServiceImpl();
        Product p = new Product(); p.setSku("OTEST"+System.currentTimeMillis()); p.setName("OTEST"); p.setDescription("desc"); p.setPrice(new BigDecimal("1.00")); p.setStock(5);
        ps.add(p);
        OrdersServiceImpl os = new OrdersServiceImpl();
        Orders o = new Orders(); o.setMemberId(1); o.setTotal(new BigDecimal("2.00")); o.setStatus("NEW");
        OrderItems it = new OrderItems(); it.setProductId(p.getId()); it.setQty(2); it.setPrice(new BigDecimal("1.00"));
        List<OrderItems> items = new ArrayList<>(); items.add(it);
        boolean ok = os.placeOrder(o, items);
        assertTrue(ok);
        Product loaded = ps.findById(p.getId());
        assertEquals(3, loaded.getStock());
        // cleanup
        ps.delete(p.getId());
    }

    @Test
    public void testPlaceOrderInsufficientStock() {
        ProductServiceImpl ps = new ProductServiceImpl();
        Product p = new Product(); p.setSku("OTEST2"+System.currentTimeMillis()); p.setName("OTEST2"); p.setDescription("desc"); p.setPrice(new BigDecimal("1.00")); p.setStock(1);
        ps.add(p);
        OrdersServiceImpl os = new OrdersServiceImpl();
        Orders o = new Orders(); o.setMemberId(1); o.setTotal(new BigDecimal("2.00")); o.setStatus("NEW");
        OrderItems it = new OrderItems(); it.setProductId(p.getId()); it.setQty(2); it.setPrice(new BigDecimal("1.00"));
        List<OrderItems> items = new ArrayList<>(); items.add(it);
        boolean ok = os.placeOrder(o, items);
        assertFalse(ok);
        Product loaded = ps.findById(p.getId());
        assertEquals(1, loaded.getStock());
        // cleanup
        ps.delete(p.getId());
    }
}
