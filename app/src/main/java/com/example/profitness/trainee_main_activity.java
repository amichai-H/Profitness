package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class trainee_main_activity extends AppCompatActivity implements View.OnClickListener{

    Button sched_btn;
    Button perf_btn;
    Button menu_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_main_activity);

        sched_btn = findViewById(R.id.sched_wrk_btn);
        perf_btn = findViewById(R.id.performance_btn);
        menu_btn = findViewById(R.id.menu_btn);

        sched_btn.setOnClickListener(this);
        perf_btn.setOnClickListener(this);
        menu_btn.setOnClickListener(this);

    }

    //@Override
    public void onClick(View v) {
        if (v == sched_btn){

            startActivity(new Intent(getApplicationContext(), Calander.class));

        }
        else if (v == perf_btn){

        }
        else if (v == menu_btn){

        }

    }

}