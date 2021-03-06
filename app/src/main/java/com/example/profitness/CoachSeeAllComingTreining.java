package com.example.profitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profitness.objects.DBshort;
import com.example.profitness.objects.MyUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CoachSeeAllComingTreining extends AppCompatActivity implements View.OnClickListener {
    String allTrainings = "allTrainings",hours = "hours",format = "dd.MM.yyyy",pathDay;
    LinearLayout layout;
    TextView currentDay;
    Button chooseDate;
    DBshort mydb;
    FirebaseUser user;
    MyUser myUser;

   // FirebaseFirestore db;
    FirebaseAuth mAuth;
    List<QueryDocumentSnapshot> myTraining = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_see_all_coming_treining);
        //db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mydb = new DBshort();

        chooseDate = findViewById(R.id.chose_date_id);
        currentDay = findViewById(R.id.date_id);
        layout = findViewById(R.id.listLayout_id);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date();
        pathDay  = formatter.format(date);
        currentDay.setText(pathDay);
        addAlltrainingTody();
        chooseDate.setOnClickListener(this);





    }
    private void addAlltrainingList(QueryDocumentSnapshot document){
        layout.removeAllViews();
        if (!((String)document.getData().get("trainee")).equals("")) {
            myTraining.add(document);
        }
    }

    private void addAlltrainingTody() {
        mydb.getAlltrainingHours(user.getUid(),pathDay,hours,this::addAlltrainingList,this::createViewOnScreen);
    }

    private void createViewOnScreen() {
        for (QueryDocumentSnapshot s : myTraining) {
            TextView newTextView = new TextView(this);
            String uid = (String) s.getData().get("trainee");
            newTextView.setText((String) s.getId());
            newTextView.setTextColor(Color.BLACK);
            newTextView.setTextSize(24);
            //newTextView.setBackgroundColor(Color.GRAY);
            newTextView.setPadding(12,12,12,12);
            newTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(s);
                    Intent intent = new Intent(CoachSeeAllComingTreining.this, DetailsTraining.class);
                    intent.putExtra("Uid", uid);
                    startActivity(intent);

                }
            });
            layout.addView(newTextView);
        }
    }
    private void handleDateButton() {
        Toast.makeText(this, "handleDateButton", Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                pathDay = date + "." + (month+1) + "." + year;
                currentDay.setText(pathDay);
                addAlltrainingTody();

            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == chooseDate){
            myTraining = new LinkedList<>();
            handleDateButton();
        }

    }
}