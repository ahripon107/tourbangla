package com.androidfragmant.tourxyz.banglatourism.train;

/**
 * Created by Ripon on 8/18/16.
 */
public class Train {

    private String area_id;
    private String delay_hour;
    private String delay_min;
    private String id;
    private String note;
    private String presentStation;
    private String time;
    private String train_code;
    private String train_end;
    private String train_from;
    private String train_id;
    private String train_name;
    private String train_off;
    private String train_start;
    private String train_to;

    public Train(String id, String train_name, String train_from, String train_to, String presentStation, String delay_hour, String delay_min, String time, String note, String train_id) {
        this.id = id;
        this.train_name = train_name;
        this.train_from = train_from;
        this.train_to = train_to;
        this.presentStation = presentStation;
        this.delay_hour = delay_hour;
        this.delay_min = delay_min;
        this.time = time;
        this.note = note;
        this.train_id = train_id;
    }

    public Train(String id, String train_name) {
        this.id = id;
        this.train_name = train_name;
    }

    public Train(String train_from) {
        this.train_from = train_from;
    }

    public Train(String train_name, String train_from, String train_to, String train_code) {
        this.train_name = train_name;
        this.train_from = train_from;
        this.train_to = train_to;
        this.train_code = train_code;
    }

    public Train(String train_name, String train_from, String train_to, String train_start, String train_end, String train_off) {
        this.train_name = train_name;
        this.train_from = train_from;
        this.train_to = train_to;
        this.train_start = train_start;
        this.train_end = train_end;
        this.train_off = train_off;
    }

    public Train(String train_name, String train_from, String train_to) {
        this.train_name = train_name;
        this.train_from = train_from;
        this.train_to = train_to;
    }

    public Train(String train_name, String train_from, String train_to, String train_start, String train_end, String train_off, String area_id) {
        this.train_name = train_name;
        this.train_from = train_from;
        this.train_to = train_to;
        this.train_start = train_start;
        this.train_end = train_end;
        this.train_off = train_off;
        this.area_id = area_id;
    }

    public Train(String id, String train_name, String presentStation, String time, String note) {
        this.id = id;
        this.train_name = train_name;
        this.presentStation = presentStation;
        this.time = time;
        this.note = note;
    }

    public String getArea_id() {
        return this.area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getTrain_name() {
        return this.train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getTrain_to() {
        return this.train_to;
    }

    public void setTrain_to(String train_to) {
        this.train_to = train_to;
    }

    public String getTrain_from() {
        return this.train_from;
    }

    public void setTrain_from(String train_from) {
        this.train_from = train_from;
    }

    public String getTrain_code() {
        return this.train_code;
    }

    public void setTrain_code(String train_code) {
        this.train_code = train_code;
    }

    public String getTrain_id() {
        return this.train_id;
    }

    public void setTrain_id(String train_id) {
        this.train_id = train_id;
    }

    public String getTrain_off() {
        return this.train_off;
    }

    public void setTrain_off(String train_off) {
        this.train_off = train_off;
    }

    public String getTrain_start() {
        return this.train_start;
    }

    public void setTrain_start(String train_start) {
        this.train_start = train_start;
    }

    public String getTrain_end() {
        return this.train_end;
    }

    public void setTrain_end(String train_end) {
        this.train_end = train_end;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPresentStation() {
        return this.presentStation;
    }

    public void setPresentStation(String presentStation) {
        this.presentStation = presentStation;
    }

    public String getDelay_hour() {
        return this.delay_hour;
    }

    public void setDelay_hour(String delay_hour) {
        this.delay_hour = delay_hour;
    }

    public String getDelay_min() {
        return this.delay_min;
    }

    public void setDelay_min(String delay_min) {
        this.delay_min = delay_min;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String toString() {
        return "Train [train_name=" + this.train_name + ", train_to=" + this.train_to + ", train_from=" + this.train_from + ", train_code=" + this.train_code + ", train_id=" + this.train_id + ", train_off=" + this.train_off + ", train_start=" + this.train_start + ", train_end=" + this.train_end + ", id=" + this.id + "]";
    }
}
