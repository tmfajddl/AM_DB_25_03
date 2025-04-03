package org.example.dao;

import org.example.dto.Member;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemberDao {


    public boolean isLoginIdDup(Connection conn, String loginId) {
        SecSql sql = new SecSql();

        sql.append("SELECT COUNT(*) > 0");
        sql.append("FROM `member`");
        sql.append("WHERE loginId = ?;", loginId);

        return DBUtil.selectRowBooleanValue(conn, sql);
    }

    public int doJoin(Connection conn, String loginId, String loginPw, String name) {

        SecSql sql = new SecSql();

        sql.append("INSERT INTO `member`");
        sql.append("SET regDate = NOW(),");
        sql.append("updateDate = NOW(),");
        sql.append("loginId = ?,", loginId);
        sql.append("loginPw = ?,", loginPw);
        sql.append("`name` = ?;", name);

        return DBUtil.insert(conn, sql);
    }

    public Map<String, Object> dologin(Connection conn, String loginId, String loginPw) {
        SecSql sql = new SecSql();
        sql.append("SELECT *");
        sql.append("FROM `member`");
        sql.append("WHERE loginId = ? AND", loginId);
        sql.append("loginPw = ?;", loginPw);

        return DBUtil.selectRow(conn, sql);
    }

    public Map<String, Object> userInfo(Connection conn, String loginId) {
        SecSql sql = new SecSql();
        sql.append("SELECT *");
        sql.append("FROM `member`");
        sql.append("WHERE loginId = ?;", loginId);

        return DBUtil.selectRow(conn, sql);
    }

    public int doUpdateName(Connection conn, String userId,String name) {
        SecSql sql = new SecSql();
        sql.append("UPDATE `member`");
        sql.append("SET updateDate = NOW()");
        sql.append(",`name` = ?", name);
        sql.append("WHERE loginId = ?;", userId);

        return DBUtil.update(conn, sql);
    }

    public int doUpdatePw(Connection conn, String userId, String loginPw) {
        SecSql sql = new SecSql();
        sql.append("UPDATE `member`");
        sql.append("SET updateDate = NOW()");
        sql.append(",loginPw = ?", loginPw);
        sql.append("WHERE loginId = ?;", userId);

        return DBUtil.update(conn, sql);
    }

    public void doDelete(Connection conn, String loginId) {
        SecSql sql = new SecSql();
        sql.append("DELETE FROM `member`");
        sql.append("WHERE loginId = ?", loginId);

        DBUtil.delete(conn, sql);
    }

    public List<Member> getMembers(Connection conn) {
        SecSql sql = new SecSql();
        sql.append("SELECT *");
        sql.append("FROM `member`");
        sql.append("ORDER BY id DESC");

        List<Map<String, Object>> memberListMap = DBUtil.selectRows(conn, sql);

        List<Member> members = new ArrayList<>();

        for (Map<String, Object> memberMap : memberListMap) {
            members.add(new Member(memberMap));
        }
        return members;
    }
}