package com.example.profitness;

public class modelMenu {

    String breakFast, lunch, dinner, day;

    public modelMenu(){

    }

    public modelMenu(String breakFast, String lunch, String dinner, String day){
        this.breakFast = breakFast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.day = day;
    }

    public String getBreakFast(){ return breakFast; }

    public String getLunch(){
        return lunch;
    }

    public String getDinner(){
        return dinner;
    }

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
