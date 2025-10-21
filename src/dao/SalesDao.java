package dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface SalesDao {
    List<Map<String,Object>> getSalesSummary(Date from, Date to);
}
