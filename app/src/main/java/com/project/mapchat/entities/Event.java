package com.project.mapchat.entities;

import java.util.ArrayList;
import java.util.Date;

public class Event {
    private Date date;
    private String desription;
    private ArrayList<String> list;
    private String locationName;
    private String cordinates;
    private String tag;

    public Event(Date date, String desription, ArrayList<String> list, String locationName, String cordinates, String tag) {
        this.date = date;
        this.desription = desription;
        this.list = list;
        this.locationName = locationName;
        this.cordinates = cordinates;
        this.tag = tag;
    }

    public Date getDate() {
        return date;
    }

    public String getDesription() {
        return desription;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getCordinates() {
        return cordinates;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "Event{" +
                "date=" + date +
                ", desription='" + desription + '\'' +
                ", list=" + list +
                ", locationName='" + locationName + '\'' +
                ", cordinates='" + cordinates + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
