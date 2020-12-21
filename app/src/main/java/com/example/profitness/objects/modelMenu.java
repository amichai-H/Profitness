package com.example.profitness.objects;

import com.google.firebase.firestore.Exclude;

public class modelMenu {

    String breakFast, lunch, dinner, option;

    public modelMenu(){

    }

    public modelMenu(String option, String breakFast, String lunch, String dinner){
        this.option = option;
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
    public String getOption(){return option;}

    public void setBreakFast(String breakFast){
        this.breakFast = breakFast;
    }

    public void setLunch(String lunch){
        this.lunch = lunch;
    }

    public void setDinner(String dinner){
        this.dinner = dinner;
    }

    public void setDay(String option){this.option = option;}

}
