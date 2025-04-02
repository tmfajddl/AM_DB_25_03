package org.example;

public class Member {
    private int id;
    private String regDate;
    private String updateDate;
    private String liginId;
    private String liginPw;
    private String Name;
    public Member(int id, String regDate, String updateDate, String liginId, String liginPw, String Name) {
        this.id = id;
        this.regDate = regDate;
        this.updateDate = updateDate;
        this.liginId = liginId;
        this.liginPw = liginPw;
        this.Name = Name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getRegDate() {
        return regDate;
    }
    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
    public String getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    public String getLiginId() {
        return liginId;
    }
    public void setLiginId(String liginId) {
        this.liginId = liginId;
    }
    public String getLiginPw() {
        return liginPw;
    }
    public void setLiginPw(String liginPw) {
        this.liginPw = liginPw;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
}
