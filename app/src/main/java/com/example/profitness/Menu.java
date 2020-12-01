package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Menu extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner spinner;
    private TextView breakFastMenu, lunchMenu, dinnerMenu;
    FirebaseFirestore db;
    ProgressDialog pd;

    private Button save, showList;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Data");

        spinner = (Spinner)findViewById(R.id.spinnerId);
        breakFastMenu = (TextView)findViewById(R.id.breakfastEditText);
        lunchMenu = (EditText)findViewById(R.id.LunchEditText);
        dinnerMenu = (EditText)findViewById(R.id.DinnerEditText);

        save = (Button) findViewById(R.id.saveId);
        showList = (Button) findViewById(R.id.showListBtn);

        showList.setOnClickListener(this);
        save.setOnClickListener(this);

        pd = new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Day, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String choice = parent.getItemAtPosition(position).toString();
        if(position == 0) {
            Toast.makeText(this,"Sunday",Toast.LENGTH_LONG).show();


        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(),"Please Select Day",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {
        if(v == save){
            String one = breakFastMenu.getText().toString().trim();
            String two = lunchMenu.getText().toString().trim();
            String tree = dinnerMenu.getText().toString().trim();
            String day = spinner.getSelectedItem().toString().trim();
            uploadData(one, two , tree, day);

            Toast.makeText(this,day,Toast.LENGTH_LONG).show();

        }

        if(v == showList){
            startActivity(new Intent(this, ShowMenuListCoach.class));
            finish();
        }
    }

    private void uploadData(String breakFast, String lunch, String dinner, String day) {
        pd.setTitle("Adding data to FireStore");
        pd.show();

        Map<String, Object> doc = new HashMap<>();
        doc.put("day", day);
        doc.put("breakFast", breakFast);
        doc.put("lunch", lunch);
        doc.put("dinner", dinner);

        System.out.println("id: " + user.getUid());

        db.collection("menu").document(user.getUid()).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(Menu.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Menu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}