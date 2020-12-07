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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class trainee_main_activity extends AppCompatActivity implements View.OnClickListener{

    Button sched_btn;
    Button my_trainings_btn;
    Button perf_btn;
    Button menu_btn;
    TextView userName_tv;

    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_main_activity);

        sched_btn = findViewById(R.id.sched_wrk_btn);
        perf_btn = findViewById(R.id.performance_btn);
        menu_btn = findViewById(R.id.menu_btn);
        userName_tv = findViewById(R.id.userNameTv);
        my_trainings_btn = findViewById(R.id.my_trainings_btn);


        sched_btn.setOnClickListener(this);
        perf_btn.setOnClickListener(this);
        menu_btn.setOnClickListener(this);
        my_trainings_btn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        setUserName();


    }

    //@Override
    public void onClick(View v) {
        if (v == sched_btn){

            startActivity(new Intent(getApplicationContext(), Calander.class));

        }
        else if (v == my_trainings_btn) {

            startActivity(new Intent(getApplicationContext(), TraineeNextTrainingView.class));

        }
        else if (v == perf_btn){

        }
        else if (v == menu_btn){
            Intent intent = new Intent(this, ShowMenuListCoach.class);
            intent.putExtra("Uid", user.getUid());
            startActivity(intent);
            finish();
        }

    }

    private void setUserName() {
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //mUser = new MyUser((String)document.getData().get("first"), (String)document.getData().get("last"), );
                    String userFirstNameString = (String)document.getData().get("first");
                    String userLastNameString = (String)document.getData().get("last");
                    userName_tv.setText("Hello " + userFirstNameString + " " + userLastNameString);

                    if (document.exists()) {
                        Log.d("readData", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("notFound", "No such document");
                        String e404 = "Error 404 user not found";
                        userName_tv.setText(e404);
                    }
                } else {
                    Log.d("NotConnected", "get failed with ", task.getException());
                    String e502 = "Error 502 failed to connect to server";
                    userName_tv.setText(e502);
                }
            }
        });
    }

}