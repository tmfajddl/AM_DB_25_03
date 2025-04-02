package org.example;

import org.example.controller.ArticleController;
import org.example.controller.MemberController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

        MemberController memberController = new MemberController(sc, conn);
        ArticleController articleController = new ArticleController(sc, conn);

        if (cmd.equals("article write")) {
            if(action == 0){
                System.out.println("로그인 후 이용바랍니다.");
                return 0;
            }
            articleController.doWrite(loginName);


        } else if (cmd.equals("article list")) {
            articleController.showList();
        } else if (cmd.startsWith("article modify")) {
            if(action == 0){
                System.out.println("로그인 후 이용바랍니다.");
                return 0;
            }
            articleController.doModify(cmd, loginName);
        } else if (cmd.startsWith("article detail")) {
            articleController.doDetail(cmd);
        } else if (cmd.startsWith("article delete")) {
            if(action == 0){
                System.out.println("로그인 후 이용바랍니다.");
                return 0;
            }
            articleController.doDelete(cmd,loginName);
        } else if (cmd.equals("member join")) {
            if(action != 0){
                System.out.println("이미 회원가입 되어있습니다.");
                return 0;
            }
            memberController.doJoin();
        } else if (cmd.equals("member list")) {
            memberController.showList();
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
            loginName = memberController.doLogin();
            action++;
        }
        return 0;
    }
}