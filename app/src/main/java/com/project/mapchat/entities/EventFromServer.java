package com.project.mapchat.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.mapchat.entities.Location;

public class EventFromServer {

    @SerializedName("idL")
    @Expose
    private String idL;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("idU")
    @Expose
    private String idU;

    @SerializedName("groupName")
    @Expose
    private String groupName;

    @SerializedName("creationTime")
    @Expose
    private String creationTime;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("active")
    @Expose
    private String active;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("meetTime")
    @Expose
    private String meetTime;

    @SerializedName("location")
    @Expose
    private Location location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdL() {
        return idL;
    }

    public void setIdL(String idL) {
        this.idL = idL;
    }

    public String getIdU() {
        return idU;
    }

    public void setIdU(String idU) {
        this.idU = idU;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeetTime() {
        return meetTime;
    }

    public void setMeetTime(String meetTime) {
        this.meetTime = meetTime;
    }

    @Override
    public String toString() {
        return "EventFromServer{" +
                "idL='" + idL + '\'' +
                ", idU='" + idU + '\'' +
                ", groupName='" + groupName + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", description='" + description + '\'' +
                ", active='" + active + '\'' +
                ", location=" + location +
                ", type='" + type + '\'' +
                ", meetTime='" + meetTime + '\'' +
                '}';
    }
}
