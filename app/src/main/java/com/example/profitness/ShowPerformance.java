package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ShowPerformance extends AppCompatActivity {
    String weight, bmi, fatPercentage, muscleMass;
    String uid;
    TextView weightTextView, BMITextView, fatPercentageTextView, muscleMassTextView;
//    DBshort mydb;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_performance);

        weightTextView = (TextView)findViewById(R.id.theWeightTraineeId);
        BMITextView = (TextView)findViewById(R.id.theBMI_Id);
        fatPercentageTextView = (TextView)findViewById(R.id.the_fat_pecentage_id);
        muscleMassTextView = (TextView)findViewById(R.id.the_muscle_mass_trainee_id);

//        mydb = new DBshort();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uid = (String) getIntent().getExtras().get("Uid");

        takeDataFromPerformanceDB();

    }

    private void takeDataFromPerformanceDB()  {
        db.collection("performance").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Map<String, Object> data = new HashMap<>();
                        assert documentSnapshot != null;
                        data = documentSnapshot.getData();
                        assert data != null;
                        weight = (String) data.get("Weight");
                        bmi = (String) data.get("BMI");
                        fatPercentage = (String)data.get("Fat Percentage");
                        muscleMass = (String)data.get("Muscle Mass");

                        weightTextView.setText(weight);
                        BMITextView.setText(bmi);
                        fatPercentageTextView.setText(fatPercentage);
                        muscleMassTextView.setText(muscleMass);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowPerformance.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}





