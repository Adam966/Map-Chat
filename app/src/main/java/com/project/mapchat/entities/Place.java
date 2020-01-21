
package com.project.mapchat.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {

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

    public Place() {
    }

    public static final Parcelable.Creator CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel parcel) {
            return new Place(parcel);
        }

        @Override
        public Place[] newArray(int i) {
            return new Place[i];
        }
    };

    public Place(Parcel parcel) {
        this.country = parcel.readString();
        this.houseNumber = parcel.readString();
        this.postcode = parcel.readString();
        this.town = parcel.readString();
        this.road = parcel.readString();
        this.lat = parcel.readDouble();
        this.lng = parcel.readDouble();
        this.formatted = parcel.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.country);
        parcel.writeString(this.houseNumber);
        parcel.writeString(this.postcode);
        parcel.writeString(this.road);
        parcel.writeString(this.town);
        parcel.writeDouble(this.lat);
        parcel.writeDouble(this.lng);
        parcel.writeString(this.formatted);
    }
}
