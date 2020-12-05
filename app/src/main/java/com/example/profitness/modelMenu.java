package com.example.profitness;

import com.google.firebase.firestore.Exclude;

public class modelMenu {

    String breakFast, lunch, dinner, day;

    public modelMenu(){

    }

    public modelMenu(String day, String breakFast, String lunch, String dinner){
        this.day = day;
        this.breakFast = breakFast;
        this.lunch = lunch;
        this.dinner = dinner;

    }

    public String getBreakFast(){ return breakFast; }

    public String getLunch(){
        return lunch;
    }

    public String getDinner(){
        return dinner;
    }

    @Exclude
    public String getDay(){return day;}

    public void setBreakFast(String breakFast){
        this.breakFast = breakFast;
    }

    public void setLunch(String lunch){
        this.lunch = lunch;
    }

    public void setDinner(String dinner){
        this.dinner = dinner;
    }

    public void setDay(String day){this.day = day;}

}
