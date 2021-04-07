package com.lck.capston_design;

public class WeightItem {
    String date;
    String weight;
    String ect;

    public WeightItem(){}

    public WeightItem(String date,String weight,String ect){
        this.date=date;
        this.weight=weight;
        this.ect=ect;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEct() {
        return ect;
    }

    public void setEct(String ect) {
        this.ect = ect;
    }
}
