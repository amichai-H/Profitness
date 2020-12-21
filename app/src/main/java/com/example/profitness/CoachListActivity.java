package com.example.profitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.profitness.objects.DBshort;
import com.example.profitness.objects.MyUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;

public class CoachListActivity extends AppCompatActivity {
    FirebaseFirestore db;
    List<QueryDocumentSnapshot> myTrainers;
    FirebaseAuth mAuth;
    FirebaseUser user;
    LinearLayout layout;
    MyUser myUser;
    DBshort mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_list);
        db = FirebaseFirestore.getInstance();
        mydb = new DBshort();
        myTrainers = new LinkedList<>();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myUser = new MyUser();
        mydb.getUser(user.getUid(),(doc)->{
            myUser.init(doc);
        });
        preferList();
        layout= (LinearLayout) findViewById(R.id.listLayout);

    }
    private void docToList(QueryDocumentSnapshot document){
        String uidC = (String) document.get("coach");
        assert uidC != null;
        if (uidC.equals(user.getUid())) {
                myTrainers.add(document);
            }
    }

    private void createViewOnScreen() {
 //       for (int i = 0;i<20;i++) {
            for (QueryDocumentSnapshot s : myTrainers) {
                TextView newTextView = new TextView(this);
                newTextView.setText((String) s.getData().get("first"));
                newTextView.setTextColor(Color.BLACK);
                newTextView.setTextSize(24);
                //newTextView.setBackgroundColor(Color.GRAY);
                newTextView.setPadding(12,12,12,12);
                newTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println(s);
                        Intent intent = new Intent(CoachListActivity.this, DetailsTraining.class);
                        intent.putExtra("Uid", s.getId());
                        startActivity(intent);

                    }
                });

                layout.addView(newTextView);
            }
 //       }
    }

    void preferList(){
        mydb.getAllUsers(this::docToList,this::createViewOnScreen);
    }
}
