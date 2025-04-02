package org.example;

import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    int action = 0;
    String loginName = null;
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
            if(action == 0){
                System.out.println("로그인 후 이용바랍니다.");
                return 0;
            }
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
            sql.append("`body` = ?,", body);
            sql.append("writer = ?;", loginName);

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

            System.out.println("  번호  /   제목  /   작성자");
            for (Article article : articles) {
                System.out.printf("  %d     /   %s   /    %s\n", article.getId(), article.getTitle(), article.getWriter());
            }
        } else if (cmd.startsWith("article modify")) {
            if(action == 0){
                System.out.println("로그인 후 이용바랍니다.");
                return 0;
            }

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
            sql.append("WHERE id = ?;", id);

            Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

            if (articleMap.isEmpty()) {
                System.out.println(id + "번 글은 없음");
                return 0;
            }

            if(!loginName.equals(articleMap.get("writer"))){
                System.out.println("수정 권한이 없습니다");
                return 0;
            }

            System.out.println("==수정==");
            System.out.print("새 제목 : ");
            String title = sc.nextLine().trim();
            System.out.print("새 내용 : ");
            String body = sc.nextLine().trim();

            sql = new SecSql();
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
        } else if (cmd.startsWith("article detail")) {

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
            sql.append("WHERE id = ?;", id);

            Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

            if (articleMap.isEmpty()) {
                System.out.println(id + "번 글은 없음");
                return 0;
            }

            Article article = new Article(articleMap);

            System.out.println("번호 : " + article.getId());
            System.out.println("작성날짜 : " + article.getRegDate());
            System.out.println("수정날짜 : " + article.getUpdateDate());
            System.out.println("제목 : " + article.getTitle());
            System.out.println("내용 : " + article.getBody());
        } else if (cmd.startsWith("article delete")) {

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
            sql.append("WHERE id = ?;", id);

            Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

            if (articleMap.isEmpty()) {
                System.out.println(id + "번 글은 없음");
                return 0;
            }

            if(!loginName.equals(articleMap.get("writer"))){
                System.out.println("삭제 권한이 없습니다");
                return 0;
            }

            System.out.println("==삭제==");
            sql = new SecSql();
            sql.append("DELETE FROM article");
            sql.append("WHERE id = ?;", id);

            DBUtil.delete(conn, sql);

            System.out.println(id + "번 글이 삭제되었습니다.");
        } else if (cmd.equals("member join")) {
            if(action != 0){
                System.out.println("이미 회원가입 되어있습니다.");
                return 0;
            }
            System.out.println("====회원가입====");
            String loginId = null;
            while (true) {
                System.out.print("아이디 : ");
                loginId = sc.nextLine();
                if (loginId.trim().isEmpty() || loginId.contains(" ")) {
                    System.out.println("아이디가 바르게 입력되지 않았습니다");
                    continue;
                }
                SecSql sql = new SecSql();
                sql.append("SELECT *");
                sql.append("FROM `member`");
                sql.append("WHERE loginId = ?;", loginId);
                Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);
                if (articleMap.isEmpty()) {
                    break;
                }
                System.out.println("아이디가 중복되었습니다.");

            }

            String loginPw = null;
            while (true) {
                System.out.print("비밀번호 : ");
                loginPw = sc.nextLine();
                System.out.print("비밀번호 확인 : ");
                String loginPw2 = sc.nextLine();
                if (loginPw.equals(loginPw2)) {
                    break;
                }
                System.out.println("비밀번호가 일치하지 않습니다.");
            }
            String name = null;
            while (true) {
                System.out.print("이름 : ");
                name = sc.nextLine();
                if(!name.isEmpty()){
                    break;
                }
                System.out.println("이름을 입력하세요.");
            }


            SecSql sql = new SecSql();
            sql.append("INSERT INTO `member`");
            sql.append("SET regDate = NOW(),");
            sql.append("updateDate = NOW(),");
            sql.append("loginId = ?,", loginId);
            sql.append("loginPw = ?,", loginPw);
            sql.append("`name` = ?;", name);

            int id = DBUtil.insert(conn, sql);

            System.out.println(id + "번 회원이 추가되었습니다.");
        } else if (cmd.equals("member list")) {
                System.out.println("==회원 목록==");

                List<Member> members = new ArrayList<>();

                SecSql sql = new SecSql();
                sql.append("SELECT *");
                sql.append("FROM `member`");
                sql.append("ORDER BY id DESC");

                List<Map<String, Object>> memberListMap = DBUtil.selectRows(conn, sql);

                for (Map<String, Object> memberMap : memberListMap) {
                    members.add(new Member(memberMap));
                }

                if (members.size() == 0) {
                    System.out.println("회원이 없습니다");
                    return 0;
                }

                System.out.println("  번호  /   이름  ");
                for (Member member : members) {
                    System.out.printf("  %d     /   %s   \n", member.getId(), member.getName());
                }
        } else if (cmd.equals("member logout")) {
            if(action == 0) {
                System.out.println("로그인 되어 있지않습니다.");
                return 0;
            }
            System.out.println(loginName + "님 로그아웃 되었습니다.");
            loginName = null;
            action--;

        } else if (cmd.equals("member login")) {
            if(action != 0) {
                System.out.println("이미 로그인 되었습니다.");
                return 0;
            }
            String loginId = null;
            String loginPw = null;
            while (true) {
                System.out.print("아이디 : ");
                loginId = sc.nextLine();
                System.out.print("비밀번호 : ");
                loginPw = sc.nextLine();
                SecSql sql = new SecSql();
                sql.append("SELECT *");
                sql.append("FROM `member`");
                sql.append("WHERE loginId = ? AND", loginId);
                sql.append("loginPw = ?;", loginPw);

                Map<String, Object> memberMap = DBUtil.selectRow(conn, sql);

                if(!memberMap.isEmpty()){
                    loginName = loginId;
                    System.out.println(loginName +"님 로그인 되었습니다.");
                    action++;
                    break;
                }

                else if (memberMap.isEmpty()) {
                    sql = new SecSql();
                    sql.append("SELECT *");
                    sql.append("FROM `member`");
                    sql.append("WHERE loginId = ?;", loginId);

                    memberMap = DBUtil.selectRow(conn, sql);
                    if (memberMap.isEmpty()) {
                        System.out.println("존재하지 않는 아이디입니다.");
                        continue;
                    }
                    System.out.println("비밀번호가 틀립니다.");
                }

            }
        }
        return 0;
    }
}