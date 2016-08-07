package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * Created by Ripon on 8/6/16.
 */
public class YoutubeVideo {
    private int id;
    private int laceId;
    private String title;
    private String url;

    public YoutubeVideo(int id, int laceId, String title, String url) {
        this.id = id;
        this.laceId = laceId;
        this.title = title;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLaceId() {
        return laceId;
    }

    public void setLaceId(int laceId) {
        this.laceId = laceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "YoutubeVideo{" +
                "id=" + id +
                ", laceId=" + laceId +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
