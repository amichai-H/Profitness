package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelfCoachScreen extends AppCompatActivity implements View.OnClickListener {
    Button goToList,goToSchedTimeToWork,scheduledTraining;
    TextView helloCoach;
    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    DBshort mybd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_coach_screen);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mybd = new DBshort();
        user = mAuth.getCurrentUser();

        helloCoach = findViewById(R.id.helloCa);
        goToSchedTimeToWork = findViewById(R.id.timeTT);
        goToList = findViewById(R.id.listOfT);
        scheduledTraining = findViewById(R.id.Scheduled_training_id);

        goToList.setOnClickListener(this);
        goToSchedTimeToWork.setOnClickListener(this);
        scheduledTraining.setOnClickListener(this);
        setUserName();

    }

    @Override
    public void onClick(View v) {
        if (v == goToList){
            Intent intent=new Intent(this,CoachListActivity.class);
            startActivity(intent);
        }
        else if(v== goToSchedTimeToWork){
            Intent intent=new Intent(this,SchedAvailability.class);
            startActivity(intent);

        }
        else if(v==scheduledTraining){
            Intent intent=new Intent(this,CoachSeeAllComingTreining.class);
            startActivity(intent);
        }
    }
    private void setUserName() {
        mybd.getUser(user.getUid(),(document)->{
            String userFirstNameString = (String)document.getData().get("first");
            String userLastNameString = (String)document.getData().get("last");
            helloCoach.setText("Hello Coach " + userFirstNameString + " " + userLastNameString);
        });
    }
}