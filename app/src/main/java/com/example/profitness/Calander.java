package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calander extends AppCompatActivity implements View.OnClickListener{

    private String dateString;
    private String timeString;

    private Button pick_btn, sched_btn;
    private TextView dateTimeTextv;

    private Spinner dateSpinner;
    private Spinner hourSpinner;

    String date;
    String time;

    boolean timeSelected, dateSelected; //if user select something in spinner

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;

    List<String> availableDatesList;
    List<String> availableHoursList;

    String availableDates;
    String hoursString;
    String isRelevantString;
    String userName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander);



        /* Clickable */

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        sched_btn = findViewById(R.id.sched_btn);
        sched_btn.setOnClickListener(this);

        /* Only Text View */
        dateTimeTextv = findViewById(R.id.emptyTV);

        /* Other */
        //calendar = Calendar.getInstance();

        hourSpinner = findViewById(R.id.hourSpinner);
        dateSpinner = findViewById(R.id.dateSpinner);

        timeSelected = dateSelected = false;

        /* Strings Init */
        availableDates = "availableDates";
        hoursString = "hours";
        isRelevantString = "isRelevant";

        date = "";
        time = "";
        userName = "";

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
                setAsTaken();
                Toast.makeText(this, "The training was set up!", Toast.LENGTH_SHORT).show();//DB add action
                finish();
                }
        }
    }

    private void setAsTaken() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("isFree", false);
        docData.put("user", user.getUid());
        db.collection(availableDates).document(date)
                .collection(hoursString).document(time).set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("set as taken", "DocumentSnapshot successfully written!");
                        addToUserTrainings();
                        addToAllTrainings(user.getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("fail set as taken", "Error writing document", e);
                    }
                });
    }


    private void addToAllTrainings(String uid) {

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String userFirstNameString = (String)document.getData().get("first");
                    String userLastNameString = (String)document.getData().get("last");
                    userName = userFirstNameString + " " + userLastNameString;

                    /* put into db */
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("trainee", userName);

                    db.collection("allTrainings").document(date).collection("hours").document(time)
                            .set(docData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("addToUserTrainings", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("fail addToUserTrainings", "Error writing document", e);
                                }
                            });

                    if (document.exists()) {

                        Log.d("readData", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("notFound", "No such document");
                    }
                } else {
                    Log.d("NotConnected", "get failed with ", task.getException());
                }
            }
        });

    }

    private void addToUserTrainings() {//not finished
        Map<String, Object> docData = new HashMap<>();
        docData.put("isOver", false);

        db.collection("users").document(user.getUid()).collection("trainings").document(date).collection("hours").document(time)
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addToUserTrainings", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("fail addToUserTrainings", "Error writing document", e);
                    }
                });

    }

    private void dateSpinnerInit(){

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
                getAvailableHoursFromDB(); //only after clicking on some date we need to update the hourSpinner
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dateSelected = false;

            }
        });
    }

    private void hourSpinnerInit(){

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

        db.collection(availableDates + "/" + date + "/" + hoursString)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                            availableHoursList.clear();

                            boolean isDateAvailable = false;
                            for(DocumentSnapshot doc: myListOfDocuments){


                                if((Boolean)doc.get("isFree"))
                                    isDateAvailable = true;
                                    availableHoursList.add(doc.getId());
                            }
                            if(!isDateAvailable){
                                removeDateFromList(date);

                            }
                            hourSpinnerInit();
                        }
                    }
                });
    }

    private void removeDateFromList(String date) {
        makeDateNotRelevant();
        dateSpinnerInit();
    }

    private void makeDateNotRelevant() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("isRelevant", false);

        db.collection("availableDates").document(date)
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("makeDateNotRelevant", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("fail makeDateNotRelevant", "Error writing document", e);
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
                                if((boolean)doc.get("isRelevant")){
                                    availableDatesList.add(doc.getId());
                                }
                            }
                            dateSpinnerInit();
                        }
                    }
                });
    }
}