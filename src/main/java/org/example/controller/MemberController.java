package org.example.controller;

import org.example.dto.Member;
import org.example.service.MemberService;

import java.sql.Connection;
import java.util.List;
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

            if (isLoginIdDup) {
                System.out.println(loginId + "은(는) 이미 사용중입니다.");
                continue;
            }
            break;

        }
        while (true) {
            System.out.print("비밀번호 : ");
            loginPw = sc.nextLine().trim();

            if (loginPw.length() == 0 || loginPw.contains(" ")) {
                System.out.println("비번을 입력해주세요.");
                continue;
            }

            boolean loginCheckPw = true;

            while (true) {
                System.out.print("비번 확인 : ");
                loginPwConfirm = sc.nextLine().trim();

                if (loginPwConfirm.length() == 0 || loginPwConfirm.contains(" ")) {
                    System.out.println("비번을 입력해주세요.");
                    continue;
                }

                if (loginPw.equals(loginPwConfirm) == false) {
                    System.out.println("일치하지 않습니다");
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
                System.out.println("이름을 바르게 입력해주세요.");
                continue;
            }
            break;
        }

        int id = memberService.doJoin(conn, loginId, loginPw, name);

        System.out.println(id + "번 회원이 가입되었습니다");
    }
    public String doLogin(){
        String loginId = null;
        String loginPw = null;
        int i = 1;
        while (i<4) {
            System.out.print("아이디 : ");
            loginId = sc.nextLine();
            System.out.print("비밀번호 : ");
            loginPw = sc.nextLine();

            Map<String, Object> memberMap = memberService.dologin(conn, loginId, loginPw);

            if(!memberMap.isEmpty()){
                System.out.println(loginId +"님 로그인 되었습니다.");
                return loginId;
            }

            else if (memberMap.isEmpty()) {
                boolean isLoginIdDup = memberService.isLoginIdDup(conn, loginId);
                if (!isLoginIdDup) {
                    System.out.println("존재하지 않는 아이디입니다.");
                    System.out.println(i + "번 잘못 입력하셨습니다.");
                    i++;
                    continue;
                }
                System.out.println("비밀번호가 틀립니다.");
                System.out.println(i + "번 잘못 입력하셨습니다.");
                i++;
            }

        }
        System.out.println("다시 로그인 바랍니다.");
        return null;
    }

    public void showInfo(String userId) {
        System.out.println("== 회원정보 ==");
        Map<String, Object> memberMap = memberService.userInfo(conn, userId);
        System.out.println("Member ID: " + memberMap.get("id"));
        System.out.println("이름: " + memberMap.get("name"));
        System.out.println("가입날짜: " + memberMap.get("regDate"));
        System.out.println("업데이트 날짜: " + memberMap.get("updateDate"));
        System.out.println("Login ID: " + memberMap.get("loginId"));
    }

    public void doUpdate(String userId) {
        System.out.println("== 회원정보 수정 ==");
        Map<String, Object> memberMap = memberService.userInfo(conn, userId);
        while(true){
            System.out.print("수정할 정보를 입력하세요: ");
            String cmd = sc.nextLine();

            if(cmd.length() == 0 || cmd.contains(" ")){
                System.out.println("다시 입력 바랍니다.");
                continue;
            }
            if(cmd.equals("name")){
                System.out.println("기존 이름: " + memberMap.get("name"));
                while(true){
                    System.out.print("바꿀 이름: ");
                    String name = sc.nextLine();
                    if(name.length() == 0 || name.contains(" ")){
                        System.out.println("올바른 이름이 아닙니다");
                        continue;
                    }
                    memberService.doUpdateName(conn,userId,name);
                    System.out.println("이름이 수정되었습니다.");
                    break;
                }
                return;
            } else if(cmd.equals("loginPw")){
                while(true){
                    System.out.print("바꿀 비밀번호: ");
                    String Pw1 = sc.nextLine();
                    System.out.print("바꿀 비밀번호 재입력: ");
                    String Pw2 = sc.nextLine();
                    if(!Pw1.equals(Pw2)){
                        System.out.println("비밀번호가 일치하지 않습니다.");
                        continue;
                    }
                    memberService.doUpdatePw(conn,userId,Pw1);
                    System.out.println("비밀번호가 수정되었습니다.");
                    break;
                }
                return;
            } else if(cmd.equals("nothing")){
                return;
            }
            System.out.println("수정할 정보가 없습니다.");
        }
    }

    public String doDelete(String userId) {
        while(true){
            System.out.print("탈퇴하시겠습니까: ");
            String cmd = sc.nextLine();
            if(cmd.equals("yes")){
                memberService.doDelete(conn, userId);
                userId = null;
                break;
            }
            else if(cmd.equals("no")){
                return userId;
            }
        }
        return userId;
    }

    public void showList() {
        System.out.println("==회원 목록==");

        List<Member> members = memberService.getMembers(conn);

        if (members.size() == 0) {
            System.out.println("가입된 회원이 없습니다");
            return;
        }

        System.out.println("  번호  /   이름");
        for (Member member : members) {
            System.out.printf("  %d     /     %s\n", member.getId(), member.getName());
        }
    }
}
