package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * Created by Ripon on 8/2/16.
 */
public class Fare {

    private int id;
    private String fromPlace;
    private String toPlace;
    private String fare;
    private String startTime;
    private String companyName;
    private String estimatedTime;
    private String leavePlace;
    private String vehicle;

    public Fare(int id, String fromPlace, String toPlace, String fare, String startTime, String companyName, String estimatedTime, String leavePlace, String vehicle) {
        this.id = id;
        this.fromPlace = fromPlace;
        this.toPlace = toPlace;
        this.fare = fare;
        this.startTime = startTime;
        this.companyName = companyName;
        this.estimatedTime = estimatedTime;
        this.leavePlace = leavePlace;
        this.vehicle = vehicle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getLeavePlace() {
        return leavePlace;
    }

    public void setLeavePlace(String leavePlace) {
        this.leavePlace = leavePlace;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "Fare{" +
                "id=" + id +
                ", fromPlace='" + fromPlace + '\'' +
                ", toPlace='" + toPlace + '\'' +
                ", fare='" + fare + '\'' +
                ", startTime='" + startTime + '\'' +
                ", companyName='" + companyName + '\'' +
                ", estimatedTime='" + estimatedTime + '\'' +
                ", leavePlace='" + leavePlace + '\'' +
                ", vehicle='" + vehicle + '\'' +
                '}';
    }
}
