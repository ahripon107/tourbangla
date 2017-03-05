package com.androidfragmant.tourxyz.banglatourism.model;

import java.io.Serializable;

/**
 * @author Ripon
 */
public class ForumPost implements Serializable {
    private int id;
    private String name;
    private String question;
    private String profileimage;
    private String timestamp;

    public ForumPost(int id, String name, String question, String profileimage, String timestamp) {
        this.id = id;
        this.name = name;
        this.question = question;
        this.profileimage = profileimage;
        this.timestamp = timestamp;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    @Override
    public String toString() {
        return "ForumPost{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", question='" + question + '\'' +
                ", profileimage='" + profileimage + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
