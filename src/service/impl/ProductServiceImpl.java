package service.impl;

import dao.ProductDao;
import dao.impl.ProductDaoImpl;
import model.Product;

import java.util.List;

public class ProductServiceImpl {
    private ProductDao dao = new ProductDaoImpl();

    public List<Product> listAll() { return dao.findAll(); }
    public Product findById(int id) { return dao.findById(id); }
    public int add(Product p) { return dao.insert(p); }
    public int update(Product p) { return dao.update(p); }
    public int delete(int id) { return dao.delete(id); }
}
