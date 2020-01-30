package com.project.mapchat.chat;

public class MessageGroup {

    private int idEG;
    private String messageText;
    private String  cTime;
    private String recieverName;
    private boolean belongToUser;

    public MessageGroup(int idEG, String messageText, String cTime, boolean belongToUser) {
        this.idEG = idEG;
        this.messageText = messageText;
        this.cTime = cTime;
        this.belongToUser = belongToUser;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    public boolean isBelongToUser() {
        return belongToUser;
    }

    public void setBelongToUser(boolean belongToUser) {
        this.belongToUser = belongToUser;
    }

    public MessageGroup(){}

    public int getIdEG() {
        return idEG;
    }

    public void setIdEG(int idEG) {
        this.idEG = idEG;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }
}
