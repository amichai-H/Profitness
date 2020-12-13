package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WorkoutHistoryLayout extends AppCompatActivity  {
   // private Button ShowMore;
    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ListView listViewHistoryOfTraining;

    List<DocumentSnapshot> myDatesHistoryTrainings;
    List<DocumentSnapshot> myHoursHistoryTrainings;
    List<String> listOfDates;
    HashMap<String, List<String>> datesAndHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history_layout);

        listViewHistoryOfTraining=findViewById(R.id.listViewOfHistory);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        myDatesHistoryTrainings =new ArrayList<>();
        listOfDates=new ArrayList<>();
        datesAndHours= new HashMap<>();
        myHoursHistoryTrainings= new LinkedList<>();

        db.collection("users/"+user.getUid()+"/trainings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    listOfDates.clear(); // need to refresh
                    myDatesHistoryTrainings = task.getResult().getDocuments(); // the dates
                    for(DocumentSnapshot doc: myDatesHistoryTrainings){
                        listOfDates.add(doc.getId()); // list of string from myhistory list
                    }

                }
            }
        });
        if(!listOfDates.isEmpty()) {
             for(String dateName: listOfDates){
                 db.collection("users/"+user.getUid()+"/trainings/"+dateName+"/hours")
                         .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if(task.isSuccessful()){
                             datesAndHours.clear(); // need to refresh
                             myHoursHistoryTrainings = task.getResult().getDocuments(); // the dates
                             List<String> listOfHours=new LinkedList<>();
                             for(DocumentSnapshot doc: myHoursHistoryTrainings){
                                 listOfHours.add(doc.getId());
                             }
                             datesAndHours.put(dateName,listOfHours);
                         }
                     }
                 });
             }
        }
        ArrayAdapter<String> HistoryList= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfDates);
        listViewHistoryOfTraining.setAdapter(HistoryList);



//        listViewHistoryOfTraining.setAdapter(HistoryList)
            //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, datesAndHours, R.id.listViewOfHistory);


        // take date and the time




    }


    }

