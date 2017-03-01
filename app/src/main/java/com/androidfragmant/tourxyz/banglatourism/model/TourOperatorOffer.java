package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * @author Ripon
 */
public class TourOperatorOffer {
    private int id;
    private String title;
    private String summary;
    private String details;
    private String imageName;
    private String link;

    public TourOperatorOffer(int id, String title, String summary, String details, String imageName, String link) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.details = details;
        this.imageName = imageName;
        this.link = link;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TourOperatorOffer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", details='" + details + '\'' +
                ", imageName='" + imageName + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
