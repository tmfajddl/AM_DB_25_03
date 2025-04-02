package org.example.service;

import org.example.dao.ArticleDao;

import java.sql.Connection;
import java.util.Map;

public class ArticleService {
    private ArticleDao articleDao;

    public ArticleService() {
        this.articleDao = new ArticleDao();
    }

    public Map<String, Object> isIdDup(Connection conn, int id) {
        return articleDao.isIdDup(conn, id);
    }

    public int doAdd(Connection conn, String loginId, String loginPw, String name) {
        return articleDao.doAdd(conn,loginId, loginPw, name);
    }
}
