package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * Created by amin on 8/17/16.
 */
public class DivisionName {

    private String nameEnglish;
    private String nameBangla;

    public DivisionName(String nameEnglish, String nameBangla) {
        this.nameEnglish = nameEnglish;
        this.nameBangla = nameBangla;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public String getNameBangla() {
        return nameBangla;
    }

    public void setNameBangla(String nameBangla) {
        this.nameBangla = nameBangla;
    }

    @Override
    public String toString() {
        return "DivisionName{" +
                "nameEnglish='" + nameEnglish + '\'' +
                ", nameBangla='" + nameBangla + '\'' +
                '}';
    }
}
