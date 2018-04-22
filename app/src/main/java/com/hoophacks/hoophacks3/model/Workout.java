package com.hoophacks.hoophacks3.model;

import java.util.ArrayList;

public class Workout {
    ArrayList<String> exercises;

    public Workout(){

    }

    public Workout(ArrayList<String> exercises){
        this.exercises = exercises;
    }

    public ArrayList<String> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<String> exercises) {
        this.exercises = exercises;
    }
}
