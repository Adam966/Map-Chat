package com.project.mapchat.entities;

public class Friend {
    private int id;
    private String nickName;

    public Friend(int id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
