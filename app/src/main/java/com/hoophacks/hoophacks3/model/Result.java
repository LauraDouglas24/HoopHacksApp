package com.hoophacks.hoophacks3.model;

import com.google.firebase.database.ServerValue;

import java.sql.Time;
import java.util.Map;

public class Result {

    int result;
    String exerciseName;

    public Result(){

    }

    public Result(int result, String exerciseName) {
        this.result = result;
        this.exerciseName = exerciseName;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
}
