package com.example.profitness.objects;

import java.util.ArrayList;
import java.util.List;

public class TrainData  {
    String date;
    ArrayList<String> hours;

    TrainData(){

    }

    public TrainData(String date, ArrayList<String> hours) {
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

    public void setHours(ArrayList<String> hours) {
        this.hours = hours;
    }
}
