
package com.project.mapchat.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("eventName")
    @Expose
    private String eventName;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("type")
    @Expose
    private Boolean type;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("ownerID")
    @Expose
    private Double ownerID;
    @SerializedName("id")
    @Expose
    private Integer id;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Event() {
    }

    /**
     * 
     * @param date
     * @param eventName
     * @param description
     * @param location
     * @param tag
     * @param id
     * @param type
     * @param ownerID
     */
    public Event(String eventName, Location location, String description, String date, Boolean type, String tag, Double ownerID, Integer id) {
        super();
        this.eventName = eventName;
        this.location = location;
        this.description = description;
        this.date = date;
        this.type = type;
        this.tag = tag;
        this.ownerID = ownerID;
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Double ownerID) {
        this.ownerID = ownerID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
