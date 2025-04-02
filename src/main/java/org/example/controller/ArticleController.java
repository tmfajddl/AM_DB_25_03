package org.example.controller;

import org.example.Article;
import org.example.service.ArticleService;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.sql.Connection;
import java.util.ArrayList;
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
        this.articleService = new ArticleService();
    }

    public void doWrite(String loginName){
        System.out.println("==글쓰기==");
        System.out.print("제목 : ");
        String title = sc.nextLine();
        System.out.print("내용 : ");
        String body = sc.nextLine();

        SecSql sql = new SecSql();

        int id = articleService.doAdd(conn, title, body, loginName);

        System.out.println(id + "번 글이 생성됨");
    }
    public void showList(){
        System.out.println("==목록==");

        List<Article> articles = new ArrayList<>();

        SecSql sql = new SecSql();
        sql.append("SELECT *");
        sql.append("FROM article");
        sql.append("ORDER BY id DESC");

        List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

        for (Map<String, Object> articleMap : articleListMap) {
            articles.add(new Article(articleMap));
        }

        if (articles.size() == 0) {
            System.out.println("게시글이 없습니다");
            return;
        }

        System.out.println("  번호  /   제목  /   작성자");
        for (Article article : articles) {
            System.out.printf("  %d     /   %s   /    %s\n", article.getId(), article.getTitle(), article.getWriter());
        }
    }

    public void doDetail(String cmd){
        int id = 0;

        try {
            id = Integer.parseInt(cmd.split(" ")[2]);
        } catch (Exception e) {
            System.out.println("번호는 정수로 입력해");
            return;
        }

        SecSql sql = new SecSql();
        sql.append("SELECT *");
        sql.append("FROM article");
        sql.append("WHERE id = ?;", id);

        Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

        if (articleMap.isEmpty()) {
            System.out.println(id + "번 글은 없음");
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
     public void doDelete(String cmd, String loginName){
         int id = 0;

         try {
             id = Integer.parseInt(cmd.split(" ")[2]);
         } catch (Exception e) {
             System.out.println("번호는 정수로 입력해");
             return;
         }


         Map<String, Object> articleMap = articleService.isIdDup(conn,id);

         if (articleMap.isEmpty()) {
             System.out.println(id + "번 글은 없음");
             return;
         }

         if(!loginName.equals(articleMap.get("writer"))){
             System.out.println("삭제 권한이 없습니다");
             return;
         }

         System.out.println("==삭제==");
         SecSql sql = new SecSql();
         sql.append("DELETE FROM article");
         sql.append("WHERE id = ?;", id);

         DBUtil.delete(conn, sql);

         System.out.println(id + "번 글이 삭제되었습니다.");
     }

     public void doModify(String cmd, String loginName){
         int id = 0;

         try {
             id = Integer.parseInt(cmd.split(" ")[2]);
         } catch (Exception e) {
             System.out.println("번호는 정수로 입력해");
             return;
         }
         Map<String, Object> articleMap =  articleService.isIdDup(conn,id);

         if (articleMap.isEmpty()) {
             System.out.println(id + "번 글은 없음");
             return;
         }

         if(!loginName.equals(articleMap.get("writer"))){
             System.out.println("수정 권한이 없습니다");
             return;
         }

         System.out.println("==수정==");
         System.out.print("새 제목 : ");
         String title = sc.nextLine().trim();
         System.out.print("새 내용 : ");
         String body = sc.nextLine().trim();

         SecSql sql = new SecSql();
         sql.append("UPDATE article");
         sql.append("SET updateDate = NOW()");
         if (title.length() > 0) {
             sql.append(", title = ?", title);
         }
         if (body.length() > 0) {
             sql.append(",`body` = ?", body);
         }
         sql.append("WHERE id = ?;", id);

         DBUtil.update(conn, sql);

         System.out.println(id + "번 글이 수정되었습니다.");
     }
}
