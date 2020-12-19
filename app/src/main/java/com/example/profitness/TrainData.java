package com.example.profitness;

import java.util.List;

public class TrainData  {
    String date;
    List<String> hours;
    // List<String> hours;
    TrainData(){

    }

    public TrainData(String date, List<String> hours) {
        this.date = date;
        this.hours = hours;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getHours() {
        return hours;
    }

    public void setHours(List<String> hours) {
        this.hours = hours;
    }
}
