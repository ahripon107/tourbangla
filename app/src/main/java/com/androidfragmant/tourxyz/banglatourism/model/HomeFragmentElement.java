package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * Created by amin on 6/27/16.
 */
public class HomeFragmentElement {

    private String title;
    private String image;
    private String buttonText;

    public HomeFragmentElement(String title, String image, String buttonText) {
        this.title = title;
        this.image = image;
        this.buttonText = buttonText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    @Override
    public String toString() {
        return "HomeFragmentElement{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", buttonText='" + buttonText + '\'' +
                '}';
    }
}
