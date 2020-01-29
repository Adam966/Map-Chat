package com.project.mapchat.entities;

public class UserEvent {
    private int idU;
    private int idEg;

    public UserEvent(int idU, int event) {
        this.idU = idU;
        this.idEg = event;
    }

    public UserEvent() {
    }

    public int getIdU() {
        return idU;
    }

    public void setIdU(int idU) {
        this.idU = idU;
    }

    public int getEvent() {
        return idEg;
    }

    public void setEvent(int event) {
        this.idEg = event;
    }
}
