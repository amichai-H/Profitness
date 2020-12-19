package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditPerformance extends AppCompatActivity implements View.OnClickListener {
     Button savePerormanceBtn, showPerormanceBtn;
    TextView weightTextview, bmiTextView, fatPercentageTextView, muscleMassTextView;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_performance);

        weightTextview = (TextView)findViewById(R.id.editWeightID);
        bmiTextView = (TextView)findViewById(R.id.editBmiId);
        fatPercentageTextView = (TextView)findViewById(R.id.edit_fat_percentageId);
        muscleMassTextView = (TextView)findViewById(R.id.edit_muscle_massId);

        savePerormanceBtn = (Button) findViewById(R.id.savePerformanceBtn);
        showPerormanceBtn = (Button) findViewById(R.id.show_performanceBtn);

        savePerormanceBtn.setOnClickListener(this);
        showPerormanceBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uid = (String) getIntent().getExtras().get("Uid");
    }


    @Override
    public void onClick(View v) {

        if(v == savePerormanceBtn){
            String weight = weightTextview.getText().toString().trim();
            String bmi = bmiTextView.getText().toString().trim();
            String fatPercentage = fatPercentageTextView.getText().toString().trim();
            String muscleMass = muscleMassTextView.getText().toString().trim();

            saveNewPerformance(uid, weight, bmi, fatPercentage, muscleMass);
        }
    }

    private void saveNewPerformance(String trainee, String weight, String bmi, String fatPercentage, String muscleMass) {

        Map<String, Object> doc = new HashMap<>();

        doc.put("Weight ", weight);
        doc.put("BMI ", bmi);
        doc.put("Fat Percentage ", fatPercentage);
        doc.put("Muscle Mass ", muscleMass);


        db.collection("performance").document(trainee).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditPerformance.this,"Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditPerformance.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}