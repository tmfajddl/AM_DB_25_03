package org.example.service;

import org.example.dto.Article;
import org.example.dao.ArticleDao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class ArticleService {

    private ArticleDao articleDao;

    public ArticleService(Connection conn) {
        this.articleDao = new ArticleDao(conn);
    }

    public int doWrite(String title, String body, String writer) {
        return articleDao.doWrite(title, body, writer);

    }

    public List<Article> getArticles() {
        return articleDao.getArticles();
    }

    public Map<String, Object> getArticleById(int id) {
        return articleDao.getArticleById(id);
    }

    public void doUpdate(int id, String title, String body) {
        articleDao.doUpdate(id, title, body);
    }

    public void doDelete(int id) {
        articleDao.doDelete(id);
    }

    public List<Article> doSearchTitle(String word) {
        return articleDao.doSearchTitle(word);
    }

    public List<Article> doSearchBody(String word) {
        return articleDao.doSearchBoby(word);
    }

    public List<Article> doSearchWriter(String word) {
        return articleDao.doSearchWriter(word);
    }

    public List<Article> getPageArticles(int start, int limit){
        return articleDao.getPageArticles(start, limit);
    }
}