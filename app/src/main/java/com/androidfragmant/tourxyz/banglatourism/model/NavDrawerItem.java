package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * @author Ripon
 */
public class NavDrawerItem {
    private String icon;
    private String title;

    public NavDrawerItem() {

    }

    public NavDrawerItem(String icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
