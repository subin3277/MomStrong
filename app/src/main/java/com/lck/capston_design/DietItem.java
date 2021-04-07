package com.lck.capston_design;

public class DietItem {
    String name;
    String nutirition;
    int idx;

    public DietItem(String name,String nutirition,int idx){
        this.name=name;
        this.nutirition=nutirition;
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNutirition() {
        return nutirition;
    }

    public void setNutirition(String nutirition) {
        this.nutirition = nutirition;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
