package org.example.service;

import org.example.dao.MemberDao;

import java.sql.Connection;
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
}