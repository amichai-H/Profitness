package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class CoachListActivity extends AppCompatActivity {
    FirebaseFirestore db;
    List<QueryDocumentSnapshot> myTrainers;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_list);
        db = FirebaseFirestore.getInstance();
        myTrainers = new LinkedList<>();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        preferList();
        layout= (LinearLayout) findViewById(R.id.listLayout);

    }

    private void createViewOnScreen() {
        for(QueryDocumentSnapshot s : myTrainers)
        {
            TextView newTextView = new TextView(this);
            newTextView.setText((String) s.getData().get("first"));
            newTextView.setTextColor(Color.BLACK);
            newTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    System.out.println(s);
                    //Intent intent=new Intent(this,SecondActivity.class);
                    //intent.putExtra("Uid", s.getId());
                    //startActivity(intent);

                }
            });

            layout.addView(newTextView);
        }
    }

    void preferList(){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String uid = document.getId();
                                if (!uid.equals(user.getUid()))
                                    myTrainers.add(document);
                            }
                            createViewOnScreen();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}