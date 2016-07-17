package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * Created by Ripon on 7/7/16.
 */
public class BlogPost {

    private int id;
    private String name;
    private String title;
    private String details;
    private String tags;
    private String image;

    public BlogPost(int id, String name, String title, String details, String tags, String image) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.details = details;
        this.tags = tags;
        this.image = image;
    }

    public BlogPost(String name, String title, String details, String tags, String image) {
        this.name = name;
        this.title = title;
        this.details = details;
        this.tags = tags;
        this.image = image;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "BlogPost{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", tags='" + tags + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
