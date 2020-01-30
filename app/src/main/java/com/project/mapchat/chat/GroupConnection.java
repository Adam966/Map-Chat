package com.project.mapchat.chat;

public class GroupConnection {

    public String idG;
    public boolean connected;

    public GroupConnection()
    {}

    public String getIdG() {
        return idG;
    }

    public void setIdG(String idG) {
        this.idG = idG;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
