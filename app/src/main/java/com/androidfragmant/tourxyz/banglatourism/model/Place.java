package com.androidfragmant.tourxyz.banglatourism.model;

import java.io.Serializable;

/**
 * Created by Ripon on 7/7/16.
 */
public class Place implements Serializable {

    private int id;
    private String name;
    private String description;
    private String howtogo;
    private String lattitude;
    private String longitude;
    private String hotel;
    private String others;
    private String picture;
    private String district;
    private String division;
    private int rating;


    public Place(int id, String name, String description, String howtogo, String lattitude, String longitude, String hotel, String others, String picture, String district, String division) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.howtogo = howtogo;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.hotel = hotel;
        this.others = others;
        this.picture = picture;
        this.district = district;
        this.division = division;
    }

    public Place(int id, String name, String description, String howtogo, String lattitude, String longitude, String hotel, String others, String picture, String district, String division, int rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.howtogo = howtogo;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.hotel = hotel;
        this.others = others;
        this.picture = picture;
        this.district = district;
        this.division = division;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHowtogo() {
        return howtogo;
    }

    public void setHowtogo(String howtogo) {
        this.howtogo = howtogo;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", howtogo='" + howtogo + '\'' +
                ", lattitude='" + lattitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", hotel='" + hotel + '\'' +
                ", others='" + others + '\'' +
                ", picture='" + picture + '\'' +
                ", district='" + district + '\'' +
                ", division='" + division + '\'' +
                '}';
    }
}
