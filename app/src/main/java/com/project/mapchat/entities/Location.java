
package com.project.mapchat.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("postCode")
    @Expose
    private String postCode;
    @SerializedName("town")
    @Expose
    private String town;
    @SerializedName("state")
    @Expose
    private String state;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Location() {
    }

    /**
     * 
     * @param address
     * @param town
     * @param coordinates
     * @param postCode
     * @param state
     */
    public Location(Coordinates coordinates, String address, String postCode, String town, String state) {
        super();
        this.coordinates = coordinates;
        this.address = address;
        this.postCode = postCode;
        this.town = town;
        this.state = state;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
