package com.example.capston_design;

public class DietItem {
    String name;
    String nutirition;

    public DietItem(String name,String nutirition){
        this.name=name;
        this.nutirition=nutirition;
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
}
