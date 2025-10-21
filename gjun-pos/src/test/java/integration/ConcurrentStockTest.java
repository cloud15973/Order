import org.junit.jupiter.api.Test;
import service.impl.OrdersServiceImpl;
import service.impl.ProductServiceImpl;
import model.Product;
import model.Orders;
import model.OrderItems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentStockTest {
    @Test
    public void testConcurrentOrdersReduceStockSafely() throws InterruptedException, ExecutionException {
        ProductServiceImpl ps = new ProductServiceImpl();
        Product p = new Product(); p.setSku("CTEST"+System.currentTimeMillis()); p.setName("CTEST"); p.setDescription("desc"); p.setPrice(new BigDecimal("1.00")); p.setStock(5);
        ps.add(p);

        Callable<Boolean> task = () -> {
            OrdersServiceImpl os = new OrdersServiceImpl();
            Orders o = new Orders(); o.setMemberId(1); o.setTotal(new BigDecimal("1.00")); o.setStatus("NEW");
            OrderItems it = new OrderItems(); it.setProductId(p.getId()); it.setQty(1); it.setPrice(new BigDecimal("1.00"));
            List<OrderItems> items = new ArrayList<>(); items.add(it);
            return os.placeOrder(o, items);
        };

        ExecutorService ex = Executors.newFixedThreadPool(5);
        List<Future<Boolean>> results = ex.invokeAll(java.util.Collections.nCopies(5, task));
        ex.shutdown();
        int success = 0;
        for (Future<Boolean> f: results) if (f.get()) success++;
        // at most 5 successes, but stock started 5, so expect 5 successes
        assertEquals(5, success);
        Product loaded = ps.findById(p.getId());
        assertEquals(0, loaded.getStock());
        // cleanup
        ps.delete(p.getId());
    }
}
