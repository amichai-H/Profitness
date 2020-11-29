package com.example.profitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Calander extends AppCompatActivity implements View.OnClickListener{

    private String dateString;
    private String timeString;

    private Button pick_btn, sched_btn;
    private TextView dateTimeTextv, doneTv;
    private Calendar calendar;

    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander);

        /* Clickable */
        pick_btn = findViewById(R.id.pick_btn);
        pick_btn.setOnClickListener(this);

        doneTv = findViewById(R.id.done_tv);
        doneTv.setOnClickListener(this);

        sched_btn = findViewById(R.id.sched_btn);
        sched_btn.setOnClickListener(this);

        /* Only Text View */
        dateTimeTextv = findViewById(R.id.emptyTV);

        /* spinner init */
        spinner = findViewById(R.id.spinner);
        String[] avaiableDates = {"a", "b", "c", "d"};// need to take available dates from DB
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, avaiableDates);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String spinnerChoose = adapterView.getItemAtPosition(position).toString();
                dateTimeTextv.setText(spinnerChoose);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /* Other */
        calendar = Calendar.getInstance();

    }

    @Override
    public void onClick(View v) {
        if(v == pick_btn){
            handleTimeButton();
            handleDateButton();

        }
        else if(v == sched_btn){
            // do someting with FireBase
        }
        else if(v == doneTv){
            finish();
        }

    }

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
}