package com.hoophacks.hoophacks3;

/**
 * Created by lauradouglas on 01/04/2018.
 */

public class Exercise {
    private String name;
//    private String tip;
//    private String skill;
    private int image;

    public Exercise(int image, String name) {
        this.image = image;
        this.name = name;
//        this.tip = tip;
//        this.skill = skill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getTip() {
//        return tip;
//    }
//
//    public void setTip(String tip) {
//        this.tip = tip;
//    }
//
//    public String getSkill() {
//        return skill;
//    }
//
//    public void setSkill(String skill) {
//        this.skill = skill;
//    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
