
package com.project.mapchat.entities;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("meetTime")
    @Expose
    private String meetTime;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("location")
    @Expose
    private Location location;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeetTime() {
        return meetTime;
    }

    public void setMeetTime(String meetTime) {
        this.meetTime = meetTime;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Event{" +
                "groupName='" + groupName + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", meetTime='" + meetTime + '\'' +
                ", tags=" + tags +
                ", location=" + location +
                '}';
    }
}
