package com.example.profitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CoachPractisTimeOfUser extends AppCompatActivity {

    DBshort mydb ;
    MyUser myUser;
    List<QueryDocumentSnapshot> myTrainers;
    LinearLayout layout;
    String uid;
    List<QueryDocumentSnapshot> myHours;
    Integer index =0;
    ArrayList<TextView> order = new ArrayList<TextView>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_practis_time_of_user);
        layout = findViewById(R.id.listTraining);

        myTrainers = new LinkedList<>();
        myUser = new MyUser();
        myUser.init();
        mydb = new DBshort();
        uid = (String) getIntent().getExtras().get("Uid");
        mydb.getCollection("/users/"+uid+"/trainings",this::createView,()->{});

    }
    private void createView(QueryDocumentSnapshot s){
        String date = s.getId();
        LinearLayout templayout = new LinearLayout(this);
        layout.addView(templayout);
        myHours = new LinkedList<>();
        TextView viewDate = new TextView(this);
        viewDate.setText(date);
        viewDate.setTextColor(Color.BLACK);
        viewDate.setTextSize(22);
        viewDate.setPadding(12, 12, 12, 12);
        templayout.addView(viewDate);
        mydb.getCollection("/users/" + uid + "/trainings/" + date + "/hours", (doc,linearLayout) -> {
            TextView viewHour = new TextView(this);
            viewHour.setText(doc.getId());
            System.out.println(doc.getId());
            viewHour.setTextColor(Color.BLACK);
            viewHour.setTextSize(18);
            viewHour.setPadding(12, 3, 12, 3);
           linearLayout.addView(viewHour);

        }, () -> {
        },templayout);



    }

//    private void docToList(QueryDocumentSnapshot document){
//        myTrainers.add(document);
//    }
//    private void createViewOnScreen() {//fix it
//        //       for (int i = 0;i<20;i++) {
//        for (QueryDocumentSnapshot s : myTrainers) {
//            String date = s.getId();
//            myHours = new LinkedList<>();
//
//            System.out.println("/users/" + uid + "/trainings/" + date + "/hours");
//            mydb.getCollection("/users/" + uid + "/trainings/" + date + "/hours", (doc) -> {
//                myHours.add(doc);
//                System.out.println(doc.getId());
//            }, () -> {
//            });
//            TextView viewDate = new TextView(this);
//            viewDate.setText(date);
//            viewDate.setTextColor(Color.BLACK);
//            viewDate.setTextSize(26);
//
//            //newTextView.setBackgroundColor(Color.GRAY);
//            viewDate.setPadding(12, 12, 12, 12);
////            viewDate.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    System.out.println(s);
////                    Intent intent = new Intent(CoachPractisTimeOfUser.this, DetailsTraining.class);
////                    intent.putExtra("Uid", s.getId());
////                    startActivity(intent);
////
////                }
////            });
//
//            layout.addView(viewDate);
//            for (QueryDocumentSnapshot h : myHours) {
//
//                TextView viewHour = new TextView(this);
//                viewHour.setText(h.getId());
//                System.out.println(h.getId());
//                viewDate.setTextColor(Color.BLACK);
//                viewDate.setTextSize(18);
//
//                //newTextView.setBackgroundColor(Color.GRAY);
//                viewDate.setPadding(12, 3, 12, 3);
////                viewDate.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        System.out.println(s);
////                        Intent intent = new Intent(CoachPractisTimeOfUser.this, DetailsTraining.class);
////                        intent.putExtra("Uid", s.getId());
////                        startActivity(intent);
////
////                    }
////                });
//
//                layout.addView(viewHour);
//
//            }
//        }
//        //       }
//    }
}