package dao;

import model.Purchases;
import java.util.List;

public interface PurchasesDao {
    int insert(Purchases p);
    List<Purchases> findAll();
}
