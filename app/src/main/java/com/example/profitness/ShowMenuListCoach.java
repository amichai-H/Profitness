package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowMenuListCoach extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<modelMenu> list = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    CustomAdapter adapter;
    ProgressDialog pd;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_menu_list_coach);

        mRecyclerView = findViewById(R.id.recycler_view);


        //set recycler view properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        pd = new ProgressDialog(this);
        uid = (String) getIntent().getExtras().get("Uid");


        showData();

    }

    //show data in recyclerView
    private void showData() {

        pd.setTitle("Loading Data...");
        pd.show();

        db.collection("menu").document(uid).collection("Days")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                pd.dismiss();

                for (DocumentSnapshot documentSnapshot: task.getResult()){
                    modelMenu m = documentSnapshot.toObject(modelMenu.class);
                    m.setDay(documentSnapshot.getId());
                    String day = m.getDay();


                    modelMenu modelMenu = new modelMenu(day,documentSnapshot.getString("breakFast"),
                            documentSnapshot.getString("lunch"), documentSnapshot.getString("dinner"));

                    list.add(modelMenu);
                }
                adapter = new CustomAdapter(ShowMenuListCoach.this, list);
                mRecyclerView.setAdapter(adapter);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ShowMenuListCoach.this,  e.getMessage(),Toast.LENGTH_SHORT).show();


                    }
                });
    }
}