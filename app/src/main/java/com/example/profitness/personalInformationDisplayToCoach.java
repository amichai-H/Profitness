package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class personalInformationDisplayToCoach extends AppCompatActivity {
    String uid;
    TextView mFullName,mAge,mPhone,mSex;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_display_to_coach);
        mFullName = findViewById(R.id.id_full_name);
        mAge = findViewById(R.id.age_id);
        mPhone = findViewById(R.id.phone_id);
        mSex = findViewById(R.id.sex_id);
        uid = (String) getIntent().getExtras().get("Uid");
        db = FirebaseFirestore.getInstance();
        takeDataFromDB();
    }

    private void takeDataFromDB() {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    assert document != null;
                    updateUI(document);
                    if (document.exists()) {
                        Log.d("readData", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("notFound", "No such document");
                        String e404 = "Error 404 user not found";
                        mFullName.setText(e404);
                    }
                } else {
                    Log.d("NotConnected", "get failed with ", task.getException());
                    String e502 = "Error 502 failed to connect to server";
                    mFullName.setText(e502);
                }
            }
        });
    }

    private void updateUI(DocumentSnapshot document) {
        String userFirstNameString,userLastNameString,dateBorn,phone;
        long sex;
       // if (document != null) {
            Map<String, Object> data = document.getData();
           // if (data != null) {
        assert data != null;

        userFirstNameString= (String) data.get("first");
                userLastNameString = (String) data.get("last");
                dateBorn = (String) data.get("born");
                phone =  (String) data.get("phone");
                sex = (long) data.get("sex");
                mFullName.setText("Trainee " + userFirstNameString + " " + userLastNameString);
                updateAge(dateBorn);
                mPhone.setText("phone: " +phone);
                if (sex==0)
                    mSex.setText("sex: Male");
                else
                    mSex.setText("sex: female");





            //}
        //}
    }

    private void updateAge(String dateBorn) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = sdf.parse(dateBorn);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int date = c.get(Calendar.DATE);
            mAge.setText("Age: " + getAge(year,month,date));

        }
        catch (Exception e){
            System.out.println("problem at AGE");
        }
    }
    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

}