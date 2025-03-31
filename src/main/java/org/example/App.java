package org.example;

import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {

    public void run() {
        System.out.println("==프로그램 시작==");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("명령어 > ");
            String cmd = sc.nextLine().trim();

            Connection conn = null;

            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            String url = "jdbc:mariadb://127.0.0.1:3306/AM_DB_25_03?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";

            try {
                conn = DriverManager.getConnection(url, "root", "");

                int actionResult = doAction(conn, sc, cmd);

                if (actionResult == -1) {
                    System.out.println("==프로그램 종료==");
                    sc.close();
                    break;
                }

            } catch (SQLException e) {
                System.out.println("에러 1 : " + e);
            } finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int doAction(Connection conn, Scanner sc, String cmd) {

        if (cmd.equals("exit")) {
            return -1;
        }

        if (cmd.equals("article write")) {
            System.out.println("==글쓰기==");
            System.out.print("제목 : ");
            String title = sc.nextLine();
            System.out.print("내용 : ");
            String body = sc.nextLine();

            SecSql sql = new SecSql();

            sql.append("INSERT INTO article");
            sql.append("SET regDate = NOW(),");
            sql.append("updateDate = NOW(),");
            sql.append("title = ?,", title);
            sql.append("`body` = ?;", body);

            int id = DBUtil.insert(conn, sql);

            System.out.println(id + "번 글이 생성됨");


        } else if (cmd.equals("article list")) {
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
                return 0;
            }

            System.out.println("  번호  /   제목  ");
            for (Article article : articles) {
                System.out.printf("  %d     /   %s   \n", article.getId(), article.getTitle());
            }
        } else if (cmd.startsWith("article modify")) {

            int id = 0;

            try {
                id = Integer.parseInt(cmd.split(" ")[2]);
            } catch (Exception e) {
                System.out.println("번호는 정수로 입력해");
                return 0;
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
                sql.append(",title = ?", title);
            }
            if (body.length() > 0) {
                sql.append(",`body` = ?", body);
            }
            sql.append("WHERE id = ?;",id);

            int rows = DBUtil.update(conn, sql);
            if (rows == 0) {
                System.out.println("해당 게시물이 없습니다.");
                return 0;
            }

            System.out.println(id + "번 글이 수정되었습니다.");
        }
        else if (cmd.startsWith("article detail")) {
            System.out.println("==상세보기==");
            int id = 0;

            try {
                id = Integer.parseInt(cmd.split(" ")[2]);
            } catch (Exception e) {
                System.out.println("번호는 정수로 입력해");
                return 0;
            }

            SecSql sql = new SecSql();
            sql.append("SELECT *");
            sql.append("FROM article");
            sql.append("WHERE id = ?;",id);
            List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);
            if(articleListMap.size() == 0) {
                System.out.println("해당 게시물이 없습니다.");
                return 0;
            }
            for (Map<String, Object> articleMap : articleListMap) {
                if(articleMap.get("id").equals(id)) {
                    System.out.println("id : " + articleMap.get("id"));
                    System.out.println("regDate : " + articleMap.get("regDate"));
                    System.out.println("updateDate : " + articleMap.get("updateDate"));
                    System.out.println("title : " + articleMap.get("title"));
                    System.out.println("body : " + articleMap.get("body"));
                    break;
                }
            }

        }
        else if (cmd.startsWith("article delete")) {
            int id = 0;
            try {
                id = Integer.parseInt(cmd.split(" ")[2]);
            } catch (Exception e) {
                System.out.println("번호는 정수로 입력해");
                return 0;
            }

            SecSql sql = new SecSql();
            sql.append("DELETE FROM article");
            sql.append("WHERE id = ?;",id);
            int rows = DBUtil.delete(conn, sql);

            if (rows == 0) {
                System.out.println("해당 게시물이 없습니다.");
                return 0;
            }

            System.out.println(id + "번 글이 삭제되었습니다.");
        }
        return 0;
    }
}