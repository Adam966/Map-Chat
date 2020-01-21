
package com.project.mapchat.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Place implements Serializable {

    private String country;
    private String houseNumber;
    private String postcode;
    private String road;
    private String town;
    private Double lat;
    private Double lng;
    private String formatted;

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Place{" +
                "country='" + country + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", postcode='" + postcode + '\'' +
                ", road='" + road + '\'' +
                ", town='" + town + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
