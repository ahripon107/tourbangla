package com.androidfragmant.tourxyz.banglatourism.train;

/**
 * Created by Ripon on 8/18/16.
 */
public class IdName {

    private String id;
    private String name;

    public IdName(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public IdName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "IdName [id=" + this.id + ", name=" + this.name + "]";
    }
}
