package service.impl;

import dao.MemberDao;
import dao.impl.MemberDaoImpl;
import model.Member;
import service.MemberService;

import java.util.List;

public class MemberServiceImpl implements MemberService {
    private MemberDao dao = new MemberDaoImpl();

    @Override
    public Member login(String account, String password) {
        Member m = dao.findByAccount(account);
        if (m != null && m.getPassword().equals(password)) return m;
        return null;
    }

    @Override
    public List<Member> listAll() { return dao.findAll(); }

    @Override
    public int register(Member m) { return dao.insert(m); }

    @Override
    public int update(Member m) { return dao.update(m); }

    @Override
    public int delete(int id) { return dao.delete(id); }
}
