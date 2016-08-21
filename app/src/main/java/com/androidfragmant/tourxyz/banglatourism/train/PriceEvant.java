package com.androidfragmant.tourxyz.banglatourism.train;

/**
 * Created by Ripon on 8/18/16.
 */
public class PriceEvant {

    private String ac_berth;
    private String ac_seat;
    private String comiutar;
    private String distance;
    private String first_berth;
    private String first_chair;
    private String from;
    private String id;
    private String kumutur;
    private String name;
    private String second_mail;
    private String second_sadaron;
    private String shuvon;
    private String shuvon_chair;
    private String snigdha;
    private String sulov;
    private String to;

    public PriceEvant(String distance, String second_sadaron, String second_mail, String comiutar, String sulov, String shuvon, String shuvon_chair, String first_chair, String first_berth, String snigdha, String ac_seat, String ac_berth, String from, String to) {
        this.distance = distance;
        this.second_sadaron = second_sadaron;
        this.second_mail = second_mail;
        this.comiutar = comiutar;
        this.sulov = sulov;
        this.shuvon = shuvon;
        this.shuvon_chair = shuvon_chair;
        this.first_chair = first_chair;
        this.first_berth = first_berth;
        this.snigdha = snigdha;
        this.ac_seat = ac_seat;
        this.ac_berth = ac_berth;
        this.from = from;
        this.to = to;
    }

    public PriceEvant(String id, String distance, String name, String from, String to, String shuvon, String shuvon_chair, String snigdha, String first_chair, String first_berth, String ac_seat, String ac_berth, String kumutur) {
        this.id = id;
        this.distance = distance;
        this.name = name;
        this.from = from;
        this.to = to;
        this.shuvon = shuvon;
        this.shuvon_chair = shuvon_chair;
        this.snigdha = snigdha;
        this.first_chair = first_chair;
        this.first_berth = first_berth;
        this.ac_seat = ac_seat;
        this.ac_berth = ac_berth;
        this.kumutur = kumutur;
    }

    public PriceEvant(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getKumutur() {
        return this.kumutur;
    }

    public void setKumutur(String kumutur) {
        this.kumutur = kumutur;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getSecond_sadaron() {
        return this.second_sadaron;
    }

    public void setSecond_sadaron(String second_sadaron) {
        this.second_sadaron = second_sadaron;
    }

    public String getSecond_mail() {
        return this.second_mail;
    }

    public void setSecond_mail(String second_mail) {
        this.second_mail = second_mail;
    }

    public String getComiutar() {
        return this.comiutar;
    }

    public void setComiutar(String comiutar) {
        this.comiutar = comiutar;
    }

    public String getSulov() {
        return this.sulov;
    }

    public void setSulov(String sulov) {
        this.sulov = sulov;
    }

    public String getShuvon() {
        return this.shuvon;
    }

    public void setShuvon(String shuvon) {
        this.shuvon = shuvon;
    }

    public String getShuvon_chair() {
        return this.shuvon_chair;
    }

    public void setShuvon_chair(String shuvon_chair) {
        this.shuvon_chair = shuvon_chair;
    }

    public String getFirst_chair() {
        return this.first_chair;
    }

    public void setFirst_chair(String first_chair) {
        this.first_chair = first_chair;
    }

    public String getFirst_berth() {
        return this.first_berth;
    }

    public void setFirst_berth(String first_berth) {
        this.first_berth = first_berth;
    }

    public String getSnigdha() {
        return this.snigdha;
    }

    public void setSnigdha(String snigdha) {
        this.snigdha = snigdha;
    }

    public String getAc_seat() {
        return this.ac_seat;
    }

    public void setAc_seat(String ac_seat) {
        this.ac_seat = ac_seat;
    }

    public String getAc_berth() {
        return this.ac_berth;
    }

    public void setAc_berth(String ac_berth) {
        this.ac_berth = ac_berth;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String toString() {
        return "PriceEvant [second_sadaron=" + this.second_sadaron + ", second_mail=" + this.second_mail + ", comiutar=" + this.comiutar + ", sulov=" + this.sulov + ", shuvon=" + this.shuvon + ", shuvon_chair=" + this.shuvon_chair + ", first_chair=" + this.first_chair + ", first_berth=" + this.first_berth + ", snigdha=" + this.snigdha + ", ac_seat=" + this.ac_seat + ", ac_berth=" + this.ac_berth + ", from=" + this.from + ", to=" + this.to + "]";
    }
}
