package com.hoophacks.hoophacks3.model;

public class Status {

    String status;
    String name;
    int likes;

    public Status() {
    }

    public Status(int likes) {
        this.likes = likes;
    }

    public Status(String status, String name, int likes) {
        this.status = status;
        this.name = name;
        this.likes = likes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes() {
        this.likes = likes;
    }

}
