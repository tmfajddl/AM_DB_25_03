package org.example;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("==프로그램 시작==");

        Scanner sc = new Scanner(System.in);


        int lastArticleId = 0;
        List<Article> articles = new ArrayList<>();

        while (true) {
            System.out.print("명령어 > ");
            String cmd = sc.nextLine().trim();

            if (cmd.equals("exit")) {
                break;
            }
            if (cmd.equals("article write")) {
                System.out.println("==글쓰기==");
                int id = lastArticleId + 1;
                System.out.print("제목 : ");
                String title = sc.nextLine().trim();
                System.out.print("내용 : ");
                String body = sc.nextLine().trim();

                Article article = new Article(id, title, body);
                articles.add(article);

                lastArticleId++;
                System.out.println(article);
                System.out.println(id + "번 글이 작성되었습니다");

                /// ///////////////////////////////////////////
                Connection conn = null;
                PreparedStatement pstmt = null;
                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://127.0.0.1:3306/AM_DB_25_03?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
                    conn = DriverManager.getConnection(url, "root", "");
                    System.out.println("연결 성공!");


                    String sql = "insert into article";
                    sql += " set regDate = now(),";
                    sql += "updateDate = now(),";
                    sql += "title = '" + title + "',";
                    sql += "`body` = '" + body + "';";

                    System.out.println(sql);

                    pstmt = conn.prepareStatement(sql);

                    int affectedRows = pstmt.executeUpdate();

                    System.out.println("affected rows: " + affectedRows);

                } catch (ClassNotFoundException e) {
                    System.out.println("드라이버 로딩 실패" + e);
                } catch (SQLException e) {
                    System.out.println("에러 : " + e);
                } finally {
                    try {
                        if (conn != null && !conn.isClosed()) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (pstmt != null && !pstmt.isClosed()) {
                            pstmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            } else if (cmd.equals("article list")) {
                System.out.println("==목록==");
                if (articles.size() == 0) {
                    System.out.println("게시글 없음");
                    continue;
                }
                System.out.println("   번호    /    제목    ");
                for (Article article : articles) {
                    System.out.printf("   %d     /   %s    \n", article.getId(), article.getTitle());
                }
                String sql = "select * from article";


                /// ////////////////////////

                Connection conn = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                List<Article> list = new ArrayList<Article>();

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://127.0.0.1:3306/AM_DB_25_03?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
                    conn = DriverManager.getConnection(url, "root", "");
                    System.out.println("연결 성공!");

                    sql = "select * from article order by id desc";

                    System.out.println(sql);

                    pstmt = conn.prepareStatement(sql);
                    rs = pstmt.executeQuery(sql);

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String regDate = rs.getString("regDate");
                        String updateDate = rs.getString("updateDate");
                        String title = rs.getString("title");
                        String body = rs.getString("body");
                        Article article = new Article(id, regDate, updateDate, title, body);
                        list.add(article);
                    }
                    for (int i = 0; i < list.size(); i++) {
                        System.out.println("번호: " + list.get(i).getId());
                        System.out.println("작성날짜: " + list.get(i).getRegDate());
                        System.out.println("수정날짜: " + list.get(i).getUpdateDate());
                        System.out.println("제목 :" + list.get(i).getTitle());
                        System.out.println("내용: " + list.get(i).getBody());
                    }


                } catch (ClassNotFoundException e) {
                    System.out.println("드라이버 로딩 실패" + e);
                } catch (SQLException e) {
                    System.out.println("에러 : " + e);
                } finally {
                    try {
                        if (rs != null && !rs.isClosed()) {
                            rs.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (pstmt != null && !pstmt.isClosed()) {
                            pstmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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

        System.out.println("==프로그램 종료==");
        sc.close();
    }
}