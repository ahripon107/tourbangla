package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * Created by Ripon on 8/16/16.
 */
public class DistrictName {

    private String banglaName;
    private String englishName;

    public DistrictName(String banglaName, String englishName) {
        this.banglaName = banglaName;
        this.englishName = englishName;
    }

    public String getBanglaName() {
        return banglaName;
    }

    public void setBanglaName(String banglaName) {
        this.banglaName = banglaName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    @Override
    public String toString() {
        return "DistrictName{" +
                "banglaName='" + banglaName + '\'' +
                ", englishName='" + englishName + '\'' +
                '}';
    }
}
