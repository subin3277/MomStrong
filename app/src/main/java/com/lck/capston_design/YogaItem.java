package com.lck.capston_design;

public class YogaItem {
    String pose;
    String path;

    public YogaItem(){}

    public YogaItem(String pose,String path){
        this.pose=pose;
        this.path = path;
    }

    public String getPose() {
        return pose;
    }

    public void setPose(String pose) {
        this.pose = pose;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
