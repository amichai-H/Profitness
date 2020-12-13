package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
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
   // ListView listViewHistoryOfTraining;
HistoryAdapter adapter;
    List<DocumentSnapshot> myDatesHistoryTrainings;
    List<DocumentSnapshot> myHoursHistoryTrainings;
    List<String> listOfDates;
    HashMap<String, List<String>> datesAndHours;
    List<TrainData>  ListtrainData= new ArrayList<>();
    RecyclerView listViewHistoryOfTraining;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history_layout);

        listViewHistoryOfTraining=findViewById(R.id.listHistory);
        listViewHistoryOfTraining.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        listViewHistoryOfTraining.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        myDatesHistoryTrainings =new ArrayList<>();
        listOfDates=new ArrayList<>();
        datesAndHours= new HashMap<>();
        myHoursHistoryTrainings= new LinkedList<>();
        showDate();








    }

    private void showDate() {

        db.collection("users/"+user.getUid()+"/trainings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    listOfDates.clear(); // need to refresh
                    myDatesHistoryTrainings = task.getResult().getDocuments(); // the dates
                    for(DocumentSnapshot doc: myDatesHistoryTrainings){

                        System.out.println(" doc og dates " + doc.getId());
                        listOfDates.add(doc.getId()); // list of string from myhistory list


                    }
                    System.out.println("one: " + listOfDates);

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
                                            System.out.println(" doc og hours " + doc.getId());
                                            listOfHours.add(doc.getId());

                                        }
                                        datesAndHours.put(dateName,listOfHours);
                                        TrainData data= new TrainData(dateName, listOfHours);
                                    }
                                }
                            });
                        }
                        adapter= new HistoryAdapter(WorkoutHistoryLayout.this, ListtrainData);
                        listViewHistoryOfTraining.setAdapter(adapter);
                    }
                }
            }
        });

        ArrayAdapter<String> HistoryList= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfDates);
       // listViewHistoryOfTraining.setAdapter(HistoryList);
    }


}

