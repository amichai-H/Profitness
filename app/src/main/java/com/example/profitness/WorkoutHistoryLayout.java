package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WorkoutHistoryLayout extends AppCompatActivity  {
   // private Button ShowMore;
    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ArrayList<TrainData>  ListtrainData= new ArrayList<>();
    ListView listView_trainData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history_layout);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        listView_trainData =findViewById(R.id.list_view_history);
        showDate();


    }

    private void showDate() {

        db.collection("users/"+user.getUid()+"/trainings")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc: task.getResult()){
                            String date=doc.getId().toString();
                            con(doc,date);

                    }
                }
            }
        });



    }

public void con(QueryDocumentSnapshot doc,String date){
    doc.getReference().collection("hours").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        ArrayList<String> listOfHours = new ArrayList<>();
                        for(QueryDocumentSnapshot docHours:task.getResult()){
                            listOfHours.add(docHours.getId());

                        }
                        TrainData trainData=new TrainData(date,listOfHours);
                        ListtrainData.add(trainData);


                    }
                    displayData();
                }
            });
}

    public void displayData(){
        HistoryAdapter adapter= new HistoryAdapter(this, ListtrainData);
        listView_trainData.setAdapter(adapter);
    }


}

