package org.example.service;

import org.example.dao.MemberDao;
import org.example.dto.Member;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class MemberService {

    private MemberDao memberDao;

    public MemberService() {
        this.memberDao = new MemberDao();
    }

    public boolean isLoginIdDup(Connection conn, String loginId) {
        return memberDao.isLoginIdDup(conn, loginId);
    }

    public int doJoin(Connection conn, String loginId, String loginPw, String name) {
        return memberDao.doJoin(conn,loginId, loginPw, name);
    }

    public Map<String, Object> dologin(Connection conn, String loginId, String loginPw){
        return memberDao.dologin(conn, loginId, loginPw);
    }

    public Map<String, Object> userInfo(Connection conn, String loginId){
        return memberDao.userInfo(conn, loginId);
    }

    public int doUpdateName(Connection conn, String loginId,String name) {
        return memberDao.doUpdateName(conn,loginId, name);
    }

    public int doUpdatePw(Connection conn, String loginId,String loginPw) {
        return memberDao.doUpdatePw(conn,loginId, loginPw);
    }

    public void doDelete(Connection conn, String loginId){
        memberDao.doDelete(conn, loginId);
    }

    public List<Member> getMembers(Connection conn) {
        return memberDao.getMembers(conn);
    }
}