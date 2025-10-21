import org.junit.jupiter.api.Test;
import service.impl.ProductServiceImpl;
import model.Product;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {
    @Test
    public void addAndListProduct() {
        ProductServiceImpl s = new ProductServiceImpl();
        Product p = new Product();
        p.setSku("TESTSKU"+System.currentTimeMillis());
        p.setName("Test Product");
        p.setDescription("Desc");
        p.setPrice(new BigDecimal("9.99"));
        p.setStock(10);
        int r = s.add(p);
        assertTrue(r>0, "insert should return >0");
        Product loaded = s.findById(p.getId());
        assertNotNull(loaded);
        assertEquals(p.getName(), loaded.getName());
        // cleanup
        s.delete(p.getId());
    }
}
