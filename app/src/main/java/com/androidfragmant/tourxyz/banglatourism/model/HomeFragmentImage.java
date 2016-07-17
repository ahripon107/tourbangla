package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * Created by Ripon on 7/13/16.
 */
public class HomeFragmentImage {

    private int id;
    private String category;
    private String imagename;
    private String text;

    public HomeFragmentImage(int id, String category, String imagename, String text) {
        this.id = id;
        this.category = category;
        this.imagename = imagename;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "HomeFragmentImage{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", imagename='" + imagename + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
