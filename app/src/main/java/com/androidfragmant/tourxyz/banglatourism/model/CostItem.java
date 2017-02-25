package com.androidfragmant.tourxyz.banglatourism.model;

/**
 * @author Ripon
 */
public class CostItem implements Comparable<CostItem> {
    private int costId;
    private int tourId;
    private int costAmount;
    private String costPurpose;

    public CostItem(int costId, int tourId, int costAmount, String costPurpose) {
        this.costId = costId;
        this.tourId = tourId;
        this.costAmount = costAmount;
        this.costPurpose = costPurpose;
    }

    public int getCostId() {
        return costId;
    }

    public void setCostId(int costId) {
        this.costId = costId;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public int getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(int costAmount) {
        this.costAmount = costAmount;
    }

    public String getCostPurpose() {
        return costPurpose;
    }

    public void setCostPurpose(String costPurpose) {
        this.costPurpose = costPurpose;
    }

    @Override
    public String toString() {
        return "CostItem{" +
                "costId=" + costId +
                ", tourId=" + tourId +
                ", costAmount=" + costAmount +
                ", costPurpose='" + costPurpose + '\'' +
                '}';
    }

    @Override
    public int compareTo(CostItem another) {
        if(costId>another.costId)
            return -1;
        else
            return 1;
    }
}
