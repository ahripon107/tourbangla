package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * @author Ripon
 */
public class HomeFragmentElement {

    private String title;
    private String buttonText;

    public HomeFragmentElement(String title, String buttonText) {
        this.title = title;
        this.buttonText = buttonText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
                ", buttonText='" + buttonText + '\'' +
                '}';
    }
}
