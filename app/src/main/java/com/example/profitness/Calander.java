package com.example.profitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Calander extends AppCompatActivity implements View.OnClickListener{

    String dateString;
    String timeString;

    Button pick_btn, sched_btn;
    TextView dateTimeTextv, doneTv;



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

    }

    @Override
    public void onClick(View v) {
        if(v == pick_btn){
            handleTimeButton();
            handleDateButton();


        }
        else if(v == sched_btn){
            // someting
        }
        else if(v == doneTv){
            finish();
        }

    }

    private void handleDateButton(){
        //Toast.makeText(this, "handleDateButton", Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance();
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

        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        int SECOND = calendar.get(Calendar.SECOND);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker datePicker, int hour, int minute) {
                timeString = hour + ":" + minute;
                String dateAndTime = dateString + "\n" + timeString;
                dateTimeTextv.setText(dateAndTime);

            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }
}