package org.example;


import com.sun.jdi.Value;

import javax.print.attribute.SupportedValuesAttribute;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("==프로그램 시작==");
        Scanner sc = new Scanner(System.in);
        int lastArticleId = 0;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstm = null;
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
                String sql = " INSERT INTO articles VALUES(?,?,?)";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1,  lastArticleId);
                pstm.setString(2,  title);
                pstm.setString(3,  body);
                pstm.executeUpdate();
                lastArticleId++;

            } else if (cmd.equals("article list")) {
                System.out.println("==목록==");
                String sql = "select * from articles";
                rs = conn.prepareStatement(sql).executeQuery();
            }
        }

        System.out.println("==프로그램 종료==");
        sc.close();
    }
}