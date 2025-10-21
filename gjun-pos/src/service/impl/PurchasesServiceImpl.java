package service.impl;

import dao.impl.PurchasesDaoImpl;
import model.Purchases;

import java.util.List;

public class PurchasesServiceImpl {
    private PurchasesDaoImpl dao = new PurchasesDaoImpl();
    public int addPurchase(Purchases p) { return dao.insert(p); }
    public List<Purchases> listAll() { return dao.findAll(); }
}
