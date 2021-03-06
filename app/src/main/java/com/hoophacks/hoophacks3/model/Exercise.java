package com.hoophacks.hoophacks3.model;

import android.net.Uri;

public class Exercise {
    String image;
    String skillArea;
    String skillLevel;
    String tip;
    String video;
    int time;

    public Exercise(){

    }

    public Exercise(String image, String skillArea, String skillLevel, String tip, String video, int time){
        this.image = image;
        this.skillArea = skillArea;
        this.skillLevel = skillLevel;
        this.tip = tip;
        this.video = video;
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSkillArea() {
        return skillArea;
    }

    public void setSkillArea(String skillArea) {
        this.skillArea = skillArea;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
