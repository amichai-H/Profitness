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
    //FirebaseFirestore db;

    CustomAdapter adapter;
    ProgressDialog pd;
    String uid;
    DBshort mydb;
    MyUser myUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_menu_list_coach);

        mRecyclerView = findViewById(R.id.recycler_view);
        mAuth = FirebaseAuth.getInstance();

        //set recycler view properties
        mydb = new DBshort();

        user = mAuth.getCurrentUser();
        assert user != null;
        myUser = new MyUser();
        mydb.getUser(user.getUid(),(doc)->{
            myUser.init(doc);
        });
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //db = FirebaseFirestore.getInstance();


        pd = new ProgressDialog(this);
        uid = (String) getIntent().getExtras().get("Uid");


        showData();

    }

    //show data in recyclerView
    private void showData() {
        String path ="";
        if (myUser.isTrainee())
            path = "trainingInformation/"+myUser.getCoach()+"/menu/"+uid+"/Options";
        else
            path = "trainingInformation/"+user.getUid()+"/menu/"+uid+"/Options";


        pd.setTitle("Loading Data...");
        pd.show();
        pd.dismiss();
        mydb.getCollection(path,(documentSnapshot)->{
            modelMenu m = documentSnapshot.toObject(modelMenu.class);
            m.setDay(documentSnapshot.getId());
            String option = m.getOption();


            modelMenu modelMenu = new modelMenu(option,documentSnapshot.getString("breakFast"),
                    documentSnapshot.getString("lunch"), documentSnapshot.getString("dinner"));

            list.add(modelMenu);
        },()->{
            adapter = new CustomAdapter(ShowMenuListCoach.this, list);
            mRecyclerView.setAdapter(adapter);
        });
//        db.collection("menu").document(uid).collection("Options")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (DocumentSnapshot documentSnapshot: task.getResult()){
//                    modelMenu m = documentSnapshot.toObject(modelMenu.class);
//                    m.setDay(documentSnapshot.getId());
//                    String option = m.getOption();
//
//
//                    modelMenu modelMenu = new modelMenu(option,documentSnapshot.getString("breakFast"),
//                            documentSnapshot.getString("lunch"), documentSnapshot.getString("dinner"));
//
//                    list.add(modelMenu);
//                }
//                adapter = new CustomAdapter(ShowMenuListCoach.this, list);
//                mRecyclerView.setAdapter(adapter);
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        pd.dismiss();
//                        Toast.makeText(ShowMenuListCoach.this,  e.getMessage(),Toast.LENGTH_SHORT).show();
//
//                    }
//                });
    }
}