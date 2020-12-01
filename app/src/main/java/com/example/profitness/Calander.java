package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Calander extends AppCompatActivity implements View.OnClickListener{

    private String dateString;
    private String timeString;

    private Button pick_btn, sched_btn;
    private TextView dateTimeTextv, doneTv;
    private Calendar calendar;

    private Spinner dateSpinner;
    private Spinner hourSpinner;

    String date;
    String time;

    boolean timeSelected, dateSelected; //if user select something in spinner

    FirebaseFirestore db;
    List<String> availableDatesList;
    List<String> availableHoursList;


    String availableDates;
    String hours;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander);



        /* Clickable */
        doneTv = findViewById(R.id.done_tv);
        doneTv.setOnClickListener(this);

        sched_btn = findViewById(R.id.sched_btn);
        sched_btn.setOnClickListener(this);

        /* Only Text View */
        dateTimeTextv = findViewById(R.id.emptyTV);

        /* Other */
        //calendar = Calendar.getInstance();

        hourSpinner = findViewById(R.id.hourSpinner);
        dateSpinner = findViewById(R.id.dateSpinner);

        timeSelected = dateSelected = false;

        availableDates = "availableDates";
        hours = "hours";

        date = "";
        time = "";

        availableDatesList = new ArrayList<>();
        availableHoursList = new ArrayList<>();

        /* db init */
        db = FirebaseFirestore.getInstance();


        /* spinner init */
        getAvailableDatesFromDB();

    }

    @Override
    public void onClick(View v) {
        if(v == sched_btn){
            if(!dateSelected)
                Toast.makeText(this, "Select valid date", Toast.LENGTH_SHORT).show();
            else if(!timeSelected)
                Toast.makeText(this, "Select valid time", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(this, "The training was set up!", Toast.LENGTH_SHORT).show();//DB add action
            }
        }
        else if(v == doneTv){
            finish();
        }
    }

    private void dateSpinnerInit(){

        //String[] showList = availableDatesList.toArray(new String[0]);
        // String[] showList = {"Choose date:", "01/11/2020", "02/11/2020", "03/11/2020", "04/11/2020"};// need to take available dates from DB
        ArrayAdapter aa = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                availableDatesList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(aa);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                dateSelected = true;
                date = adapterView.getItemAtPosition(position).toString();
                dateTimeTextv.setText(date);
                getAvailableHoursFromDB();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dateSelected = false;

            }
        });
    }

    private void hourSpinnerInit(){

        //String[] showList = {"07:00", "08:00", "09:00", "10:00", "11:00"};// need to take available hours from DB
        ArrayAdapter aa;
        aa = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                availableHoursList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(aa);
        hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                timeSelected = true;
                time = adapterView.getItemAtPosition(position).toString();
                dateTimeTextv.setText(time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                timeSelected = false;
            }
        });
    }

    void getAvailableHoursFromDB(){

        db.collection(availableDates + "/" + date + "/" + hours)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            availableHoursList.clear();
                            for(DocumentSnapshot doc: myListOfDocuments){
                                availableHoursList.add(doc.getId());
                            }
                            hourSpinnerInit();
                        }
                    }
                });
    }

    void getAvailableDatesFromDB(){

        db.collection(availableDates)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            for(DocumentSnapshot doc: myListOfDocuments){
                                availableDatesList.add(doc.getId());
                            }
                            dateSpinnerInit();
                            //hourSpinnerInit();
                            //getAvailableHoursFromDB();
                        }
                    }
                });
    }



    /*
    private void handleDateButton(){
        //Toast.makeText(this, "handleDateButton", Toast.LENGTH_SHORT).show();


        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                dateString = date + "/" + month + "/" + year;

            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    private void handleTimeButton(){
        //Toast.makeText(this, "handleTimeButton", Toast.LENGTH_SHORT).show();


        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        int SECOND = calendar.get(Calendar.SECOND);

        boolean is24HourFormat = true;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker datePicker, int hour, int minute) {
                timeString = hour + ":" + minute;
                String dateAndTime = dateString + "\n" + timeString;
                dateTimeTextv.setText(dateAndTime);

            }
        }, HOUR, MINUTE, is24HourFormat);
        timePickerDialog.show();
    }

     */
}