package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SchedAvailability extends AppCompatActivity implements View.OnClickListener {


    String dateString,timeString;
    Button pick_btn,pickStartH,pickEndH,send;
    TextView showDate,showStartH,showEndH;
    int hStart,hEnd,minStart,minend,tH,tM;

    FirebaseUser user;
    //FirebaseFirestore db;
    FirebaseAuth mAuth;
    DBshort mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sched_availability);
        mAuth = FirebaseAuth.getInstance();
        //db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        mydb = new DBshort();

        showDate = findViewById(R.id.showMycDate);
        showStartH =   findViewById(R.id.strTime);
        showEndH =   findViewById(R.id.endTime);

        pickEndH = findViewById(R.id.endHB);
        pickStartH = findViewById(R.id.strHB);
        pick_btn = findViewById(R.id.choosDateForCoach);
        send = findViewById(R.id.sendBT);

        pick_btn.setOnClickListener(this);
        pickStartH.setOnClickListener(this);
        pickEndH.setOnClickListener(this);
        send.setOnClickListener(this);

        hStart = hEnd = minStart = tH = tM = -1;
    }





    @Override
    public void onClick(View v) {
        if (v==pick_btn){
            handleDateButton();

        }
        else if (v==pickStartH){
            handleTimeButton(showStartH);
            hStart = tH;
            minStart = tM;
        }
        else if (v==pickEndH){
            handleTimeButton(showEndH);
            hEnd = tH;
            minend = tM;
        }
        else if (v==send){
            if(hStart != -1 && minStart != -1 && hEnd != -1 && minend != -1 ){
                sendToDB();
                Toast.makeText(this, "The trainings was set up!", Toast.LENGTH_SHORT).show();//DB add action
            }
            else{
                Toast.makeText(this, "Please select valid time!", Toast.LENGTH_SHORT).show();//DB add action
            }

        }


    }

    private void sendToDB() {
        List<String> allDates = new LinkedList<>();
        for (int i = hStart;i<=hEnd;i++){
            if (i!=hEnd) {
                String hh = "";
                if (i <= 9) {
                    hh = "0" + i + ":";
                } else {
                    hh = i + ":";
                }
                if (minStart <= 9) {
                    hh = hh + "0" + minStart;
                } else {
                    hh = hh + minStart;
                }
                allDates.add(hh);
            }
            else {
                if (minStart<minend){
                    String hh = "";
                    if (i <= 9) {
                        hh = "0" + i + ":";
                    } else {
                        hh = i + ":";
                    }
                    if (minStart <= 9) {
                        hh = hh + "0" + minStart;
                    } else {
                        hh = hh + minStart;
                    }
                    allDates.add(hh);
                }
            }
        }
        Map<String, Object> docData2 = new HashMap<>();
        docData2.put("isRelevant", true);
        mydb.insertDocAvaDates(user.getUid(),dateString,docData2,()->{});
//        db.collection("availableDates").document(dateString
//        ).set(docData2)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("TAG", "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("TAG", "Error writing document", e);
//                    }
//                });
        for (String d:allDates) {
            Map<String, Object> docData = new HashMap<>();
            docData.put("isFree", true);
            mydb.insertDocAvaDatesHours(user.getUid(),dateString,d,docData,()->{});
//            db.collection("availableDates").document(dateString
//            ).collection("hours").document(d).set(docData)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d("TAG", "DocumentSnapshot successfully written!");
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w("TAG", "Error writing document", e);
//                        }
//                    });

        }
    }

    private void handleDateButton() {
        //Toast.makeText(this, "handleDateButton", Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                dateString = date + "." + (month+1) + "." + year;
                showDate.setText(dateString);

            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }
    private void handleTimeButton(TextView view){
        //Toast.makeText(this, "handleTimeButton", Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        boolean is24HourFormat = true;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker datePicker, int hour, int minute) {
                if (view == showEndH) {
                    minend = minute;
                    hEnd = hour;
                }
                if (view == showStartH){
                    minStart = minute;
                    hStart = hour;
                }
                timeString = hour + ":" + minute;
                view.setText(timeString);

            }
        }, HOUR, MINUTE, is24HourFormat);
        timePickerDialog.show();
    }
}