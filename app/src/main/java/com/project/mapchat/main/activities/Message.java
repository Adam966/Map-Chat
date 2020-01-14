package com.project.mapchat.main.activities;

public class Message {
    private String text;
    private ChatActivity.MemberData memberData;
    private boolean belongsToCurrentUser;

    public Message(String text, ChatActivity.MemberData data, boolean belongsToCurrentUser) {
        this.text = text;
        this.memberData = data;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }

    public ChatActivity.MemberData getMemberData() {
        return memberData;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}
