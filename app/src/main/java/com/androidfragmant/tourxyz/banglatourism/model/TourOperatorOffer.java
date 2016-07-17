package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * Created by Ripon on 7/6/16.
 */
public class TourOperatorOffer {

    private String title;
    private String summary;
    private String details;
    private String imageName;

    public TourOperatorOffer(String title, String summary, String details, String imageName) {
        this.title = title;
        this.summary = summary;
        this.details = details;
        this.imageName = imageName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "TourOperatorOffer{" +
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", details='" + details + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
