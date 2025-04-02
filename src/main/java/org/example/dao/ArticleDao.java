package org.example.dao;

import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.sql.Connection;
import java.util.Map;

public class ArticleDao {
    public Map<String, Object> isIdDup(Connection conn, int id) {
        SecSql sql = new SecSql();

        sql.append("SELECT *");
        sql.append("FROM article");
        sql.append("WHERE id = ?;", id);

        return DBUtil.selectRow(conn, sql);
    }

    public int doAdd(Connection conn, String title, String body, String loginName) {

        SecSql sql = new SecSql();

        sql.append("INSERT INTO article");
        sql.append("SET regDate = NOW(),");
        sql.append("updateDate = NOW(),");
        sql.append("title = ?,", title);
        sql.append("`body` = ?,", body);
        sql.append("writer = ?;", loginName);

        return DBUtil.insert(conn, sql);
    }
}
