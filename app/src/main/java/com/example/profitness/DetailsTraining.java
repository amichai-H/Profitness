package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailsTraining extends AppCompatActivity implements View.OnClickListener {
    private Button editMenu;
    String uid;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    TextView showTraineeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_training);

        editMenu = (Button)findViewById(R.id.menuBtn);
        uid = (String) getIntent().getExtras().get("Uid");
        showTraineeName = findViewById(R.id.showTrainneName);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editMenu.setOnClickListener(this);
        updateUI();
    }

    private void updateUI() {
        setUserName();

    }

    @Override
    public void onClick(View v) {
        if(v == editMenu){
            Intent intent = new Intent(this, Menu.class);
            intent.putExtra("Uid", uid);
            startActivity(intent);
        }
    }
    private void setUserName() {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //mUser = new MyUser((String)document.getData().get("first"), (String)document.getData().get("last"), );
                    String userFirstNameString = (String)document.getData().get("first");
                    String userLastNameString = (String)document.getData().get("last");
                    showTraineeName.setText("Trainee " + userFirstNameString + " " + userLastNameString);

                    if (document.exists()) {
                        Log.d("readData", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("notFound", "No such document");
                        String e404 = "Error 404 user not found";
                        showTraineeName.setText(e404);
                    }
                } else {
                    Log.d("NotConnected", "get failed with ", task.getException());
                    String e502 = "Error 502 failed to connect to server";
                    showTraineeName.setText(e502);
                }
            }
        });
    }
}