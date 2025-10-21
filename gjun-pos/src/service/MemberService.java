package service;

import model.Member;
import java.util.List;

public interface MemberService {
    Member login(String account, String password);
    List<Member> listAll();
    int register(Member m);
    int update(Member m);
    int delete(int id);
}
