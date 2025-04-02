package org.example.controller;

import org.example.service.MemberService;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.sql.Connection;
import java.util.Map;
import java.util.Scanner;

public class MemberController {

    private Connection conn;
    private Scanner sc;

    private MemberService memberService;

    public MemberController(Scanner sc, Connection conn) {
        this.sc = sc;
        this.conn = conn;
        this.memberService = new MemberService();
    }

    public void doJoin() {
        String loginId = null;
        String loginPw = null;
        String loginPwConfirm = null;
        String name = null;
        System.out.println("==회원가입==");
        while (true) {
            System.out.print("로그인 아이디 : ");
            loginId = sc.nextLine().trim();

            if (loginId.length() == 0 || loginId.contains(" ")) {
                System.out.println("아이디 똑바로 써");
                continue;
            }

            boolean isLoginIdDup = memberService.isLoginIdDup(conn, loginId);

            System.out.println(isLoginIdDup);

            if (isLoginIdDup) {
                System.out.println(loginId + "은(는) 이미 사용중");
                continue;
            }
            break;

        }
        while (true) {
            System.out.print("비밀번호 : ");
            loginPw = sc.nextLine().trim();

            if (loginPw.length() == 0 || loginPw.contains(" ")) {
                System.out.println("비번 똑바로 써");
                continue;
            }

            boolean loginCheckPw = true;

            while (true) {
                System.out.print("비번 확인 : ");
                loginPwConfirm = sc.nextLine().trim();

                if (loginPwConfirm.length() == 0 || loginPwConfirm.contains(" ")) {
                    System.out.println("비번 확인 똑바로 써");
                    continue;
                }

                if (loginPw.equals(loginPwConfirm) == false) {
                    System.out.println("일치하지 않아");
                    loginCheckPw = false;
                }
                break;
            }
            if (loginCheckPw) {
                break;
            }
        }
        while (true) {
            System.out.print("이름 : ");
            name = sc.nextLine();

            if (name.length() == 0 || name.contains(" ")) {
                System.out.println("이름 똑바로 써");
                continue;
            }
            break;
        }

        int id = memberService.doJoin(conn, loginId, loginPw, name);

        System.out.println(id + "번 회원이 가입됨");
    }
    public String doLogin(){
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
                System.out.println(loginId +"님 로그인 되었습니다.");
                break;
            }

            else if (memberMap.isEmpty()) {
                boolean isLoginIdDup = memberService.isLoginIdDup(conn, loginId);
                if (!isLoginIdDup) {
                    System.out.println("존재하지 않는 아이디입니다.");
                    continue;
                }
                System.out.println("비밀번호가 틀립니다.");
            }

        }
        return loginId;
    }
}
