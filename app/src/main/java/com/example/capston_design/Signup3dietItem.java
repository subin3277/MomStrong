package com.example.capston_design;

public class Signup3dietItem {
    String idx;
    String name;

    private boolean isSelected = false;

    public Signup3dietItem(){}

    public Signup3dietItem(String idx, String name){
        this.idx = idx;
        this.name = name;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(Boolean selected){
        isSelected = selected;
    }
    public boolean isSelected(){
        return isSelected;
    }
}
