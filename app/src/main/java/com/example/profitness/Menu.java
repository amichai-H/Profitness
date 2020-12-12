package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu extends AppCompatActivity implements  View.OnClickListener {
    private Spinner spinner;
    private TextView breakFastMenu, lunchMenu, dinnerMenu;

    FirebaseFirestore db;
    ProgressDialog pd;

    private Button save, showList;
    private FirebaseAuth mAuth;

    FirebaseUser user;
    LinearLayout layout;
    String uid;


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

        uid = (String) getIntent().getExtras().get("Uid");

    }




    @Override
    public void onClick(View v) {

        if(v == save) {
            String bf = breakFastMenu.getText().toString().trim();
            String l = lunchMenu.getText().toString().trim();
            String d = dinnerMenu.getText().toString().trim();
            String day = spinner.getSelectedItem().toString().trim();

            saveNewDayMenu(uid,bf, l, d, day);

        }

        if(v == showList){
            Intent intent = new Intent(this, ShowMenuListCoach.class);
            intent.putExtra("Uid", uid);
            startActivity(intent);
        }
    }


    private void saveNewDayMenu(String trainer ,String breakFast, String lunch, String dinner, String day){
        pd.setTitle("Adding data to FireStore");
        pd.show();

        Map<String, Object> doc = new HashMap<>();

        doc.put("breakFast", breakFast);
        doc.put("lunch", lunch);
        doc.put("dinner", dinner);

        db.collection("menu").document(trainer).collection("Days").document(day).set(doc)
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