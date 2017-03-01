package com.androidfragmant.tourxyz.banglatourism.model;

import java.io.Serializable;

/**
 * @author Ripon
 */
public class CostPlace implements Serializable,Comparable<CostPlace> {
    private int id;
    private int cost;
    private String costPlace;

    public CostPlace(int id, int cost, String costPlace) {
        this.id = id;
        this.cost = cost;
        this.costPlace = costPlace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getCostPlace() {
        return costPlace;
    }

    public void setCostPlace(String costPlace) {
        this.costPlace = costPlace;
    }

    @Override
    public String toString() {
        return "CostPlace{" +
                "id=" + id +
                ", cost=" + cost +
                ", costPlace='" + costPlace + '\'' +
                '}';
    }

    @Override
    public int compareTo(CostPlace another) {
        if(id>another.id)
            return -1;
        else
            return 1;
    }
}
