package org.example.controller;

import org.example.Article;
import org.example.service.ArticleService;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ArticleController {

    private Connection conn;
    private Scanner sc;

    private ArticleService articleService;

    public ArticleController(Scanner sc, Connection conn) {
        this.sc = sc;
        this.conn = conn;
        this.articleService = new ArticleService(conn);
    }


    public void doWrite(String userId) {
        System.out.println("==글쓰기==");
        System.out.print("제목 : ");
        String title = sc.nextLine();
        System.out.print("내용 : ");
        String body = sc.nextLine();

        int id = articleService.doWrite(title, body, userId);

        System.out.println(id + "번 글이 생성됨");
    }

    public void showList() {
        System.out.println("==목록==");

        List<Article> articles = articleService.getArticles();

        if (articles.size() == 0) {
            System.out.println("게시글이 없습니다");
            return;
        }

        System.out.println("  번호  /   제목   /   작성자");
        for (Article article : articles) {
            System.out.printf("  %d     /     %s     /     %s\n", article.getId(), article.getTitle(), article.getWriter());
        }
    }

    public void doModify(String cmd, String userId) {

        int id = 0;

        try {
            id = Integer.parseInt(cmd.split(" ")[2]);
        } catch (Exception e) {
            System.out.println("번호는 정수로 입력해");
            return;
        }

        Map<String, Object> articleMap = articleService.getArticleById(id);

        if (articleMap.isEmpty()) {
            System.out.println(id + "번 글은 없어");
            return;
        }

        Article article = new Article(articleMap);
        if(!userId.equals(article.getWriter())) {
            System.out.println("수정권한이 없습니다.");
            return;
        }

        System.out.println("==수정==");
        System.out.print("새 제목 : ");
        String title = sc.nextLine().trim();
        System.out.print("새 내용 : ");
        String body = sc.nextLine().trim();

        articleService.doUpdate(id, title, body);

        System.out.println(id + "번 글이 수정되었습니다.");

    }

    public void showDetail(String cmd) {

        int id = 0;

        try {
            id = Integer.parseInt(cmd.split(" ")[2]);
        } catch (Exception e) {
            System.out.println("번호는 정수로 입력해");
            return;
        }

        System.out.println("==상세보기==");

        Map<String, Object> articleMap = articleService.getArticleById(id);

        if (articleMap.isEmpty()) {
            System.out.println(id + "번 글은 없어");
            return;
        }

        Article article = new Article(articleMap);

        System.out.println("번호 : " + article.getId());
        System.out.println("작성날짜 : " + article.getRegDate());
        System.out.println("수정날짜 : " + article.getUpdateDate());
        System.out.println("제목 : " + article.getTitle());
        System.out.println("내용 : " + article.getBody());
        System.out.println("글쓴이 : " + article.getWriter());
    }

    public void doDelete(String cmd,  String userId) {

        int id = 0;

        try {
            id = Integer.parseInt(cmd.split(" ")[2]);
        } catch (Exception e) {
            System.out.println("번호는 정수로 입력해");
            return;
        }

        Map<String, Object> articleMap = articleService.getArticleById(id);

        if (articleMap.isEmpty()) {
            System.out.println(id + "번 글은 없어");
            return;
        }

        Article article = new Article(articleMap);
        if(!userId.equals(article.getWriter())) {
            System.out.println("삭제권한이 없습니다.");
            return;
        }

        System.out.println("==삭제==");

        articleService.doDelete(id);

        System.out.println(id + "번 글이 삭제되었습니다.");
    }
}
