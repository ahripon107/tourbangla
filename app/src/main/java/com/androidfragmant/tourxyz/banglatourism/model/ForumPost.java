package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * Created by Ripon on 7/16/16.
 */
public class ForumPost {

    private int id;
    private String name;
    private String question;

    public ForumPost(int id, String name, String question) {
        this.id = id;
        this.name = name;
        this.question = question;
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

    @Override
    public String toString() {
        return "ForumPost{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", question='" + question + '\'' +
                '}';
    }
}
