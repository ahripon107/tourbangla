package com.sfuronlabs.ripon.tourbangla;

import com.parse.ParseObject;

/**
 * Created by Ripon on 6/14/15.
 */
public class Place {
    private int id;
    private String name;
    private String description;
    private String howtogo;
    private String lattitude;
    private String longitude;
    private String hotel;
    private String others;
    private String picture;
    private String address;
    private String type;
    private String district;
    private ParseObject parseObject;
    private int rating;
    private String objectId;

    public Place() {
    }

    public Place(int id, String name, String description, String howtogo, String lattitude, String longitude, String hotel,String others, String picture,String address,String type,String district) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.howtogo = howtogo;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.hotel = hotel;
        this.others = others;
        this.picture = picture;
        this.address = address;
        this.type = type;
        this.district = district;
    }

    public Place(int id,String name,String picture,String type,String district)
    {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.type = type;
        this.district = district;
    }

    public Place(int id, String name, String description, String howtogo, String lattitude, String longitude, String hotel, String others, String picture, String address, String type, String district, ParseObject parseObject) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.howtogo = howtogo;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.hotel = hotel;
        this.others = others;
        this.picture = picture;
        this.address = address;
        this.type = type;
        this.district = district;
        this.parseObject = parseObject;
    }

    public Place(int id, String name, String description, String howtogo, String lattitude, String longitude, String hotel, String others, String picture, String address, String type, String district, String objectId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.howtogo = howtogo;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.hotel = hotel;
        this.others = others;
        this.picture = picture;
        this.address = address;
        this.type = type;
        this.district = district;
        this.objectId = objectId;
    }

    public Place(int id, String name, String description, String howtogo, String lattitude, String longitude, String hotel, String others, String picture, String address, String type, String district, String objectId,int rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.howtogo = howtogo;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.hotel = hotel;
        this.others = others;
        this.picture = picture;
        this.address = address;
        this.type = type;
        this.district = district;
        this.objectId = objectId;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHowtogo() {
        return howtogo;
    }

    public String getLattitude() {
        return lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getHotel() {
        return hotel;
    }

    public String getOthers() {
        return others;
    }

    public String getPicture() {
        return picture;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getDistrict() {
        return district;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }



    public void setDescription(String description) {
        this.description = description;
    }

    public void setHowtogo(String howtogo) {
        this.howtogo = howtogo;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public ParseObject getParseObject() {
        return parseObject;
    }

    public void setParseObject(ParseObject parseObject) {
        this.parseObject = parseObject;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
