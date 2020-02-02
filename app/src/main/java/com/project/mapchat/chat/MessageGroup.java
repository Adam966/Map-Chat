package com.project.mapchat.chat;

public class MessageGroup {

    private int idEG;
    private String messageText;
    private String  cTime;
    private boolean belongToUser;
    private int idU;

    public MessageGroup(int idEG, String messageText, String cTime, boolean belongToUser, int idU) {
        this.idEG = idEG;
        this.messageText = messageText;
        this.cTime = cTime;
        this.belongToUser = belongToUser;
        this.idU = idU;
    }

    public int getIdU() {
        return idU;
    }

    public void setIdU(int idU) {
        this.idU = idU;
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

    @Override
    public String toString() {
        return "MessageGroup{" +
                "idEG=" + idEG +
                ", messageText='" + messageText + '\'' +
                ", cTime='" + cTime + '\'' +
                ", belongToUser=" + belongToUser +
                ", idU=" + idU +
                '}';
    }
}
