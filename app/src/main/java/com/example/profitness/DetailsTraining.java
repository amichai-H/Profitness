package com.example.profitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DetailsTraining extends AppCompatActivity implements View.OnClickListener {
    private Button editMenu;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_training);

        editMenu = (Button)findViewById(R.id.menuBtn);
        uid = (String) getIntent().getExtras().get("Uid");

        editMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == editMenu){
            Intent intent = new Intent(this, Menu.class);
            intent.putExtra("Uid", uid);
            startActivity(intent);
        }
    }
}