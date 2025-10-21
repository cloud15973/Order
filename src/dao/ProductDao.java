package dao;

import model.Product;
import java.util.List;

public interface ProductDao {
    Product findById(int id);
    List<Product> findAll();
    int insert(Product p);
    int update(Product p);
    int delete(int id);
}
