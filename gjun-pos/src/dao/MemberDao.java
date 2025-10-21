package dao;

import model.Member;
import java.util.List;

public interface MemberDao {
    Member findById(int id);
    Member findByAccount(String account);
    List<Member> findAll();
    int insert(Member m);
    int update(Member m);
    int delete(int id);
}
