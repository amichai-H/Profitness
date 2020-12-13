package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

    private void setAsTaken() { ////when trainee select free hour in some date, the hour is set to be taken and its not free anymore
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


    private void addToAllTrainings(String uid) { //when trainee select free hour in some date, it will be added to the coach list of trainings

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    /* get the name of the trainee */
                    DocumentSnapshot document = task.getResult();
                    String userFirstNameString = (String)document.getData().get("first");
                    String userLastNameString = (String)document.getData().get("last");
                    userName = userFirstNameString + " " + userLastNameString;

                    /* set the data we want to update */
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("trainee", uid);

                    /* put into db */
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

    private void addToUserTrainings() {//when trainee select free hour in some date, it will be added to the trainee list of trainings
        Map<String, Object> docData = new HashMap<>();
        docData.put("isOver", false);

        Map<String, Object> docData2 = new HashMap<>();
        docData2.put("isRelevant", true);

        db.collection("users").document(user.getUid()).collection("trainings").document(date)
                .set(docData2)
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

    private void dateSpinnerInit(){//create the date spinner using the dates inside availableDatesList (need to reload the dates from the db first)

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
                dateTimeTextv.setText(date + " " + time);
                getAvailableHoursFromDB(); //only after clicking on some date we need to update the hourSpinner
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dateSelected = false;

            }
        });
    }

    private void hourSpinnerInit(){//after the use of getAvailableHoursFromDB(), this func creates the hours spinner

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
                dateTimeTextv.setText(date + " " + time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                timeSelected = false;
            }
        });
    }

    void getAvailableHoursFromDB(){//for a given date, this func reload the non taken hours of the date to availableHoursList

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

                                if((Boolean)doc.get("isFree")){
                                    isDateAvailable = true;
                                    availableHoursList.add(doc.getId());
                                }
                            }
                            if(!isDateAvailable){ //in case all the hours of the selected date are taken, update the db that the selected date is taken and reload again the relevant dates
                                removeDateFromList(date);
                            }
                            //sortHoursList(availableHoursList);
                            hourSpinnerInit();
                        }
                    }
                });
    }

    private void removeDateFromList(String date) {//if while we see that all the hours of some date are taken -> we update the date to be not relevant and reload the spinner
        makeDateNotRelevant(date);
        dateSpinnerInit();
    }

    private void makeDateNotRelevant(String dateToChange) { //we use this func if all hours of this date are taken -> it will not added to the list of the available dates
        Map<String, Object> docData = new HashMap<>();
        docData.put("isRelevant", false);

        db.collection("availableDates").document(dateToChange)
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
                                boolean isRelevantByDate = isRelevantByDate(doc.getId());
                                if((boolean)doc.get("isRelevant")){

                                    if( isRelevantByDate ){
                                        availableDatesList.add(doc.getId());
                                    }
                                    else{
                                        makeDateNotRelevant(doc.getId());
                                    }
                                }
                            }
                            sortDatesList(availableDatesList);
                            dateSpinnerInit();
                        }
                    }
                });
    }

    public static boolean isRelevantByDate(String stringDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        Calendar today = Calendar.getInstance();

        return calendarDate.compareTo(today) > 0;
    }


    public static boolean isRelevantByHour(String hour) {

        String timeStamp = new SimpleDateFormat("HH:mm").format(new java.util.Date());
        try {
            return new SimpleDateFormat("hh:mm").parse(hour).compareTo(new SimpleDateFormat("hh:mm").parse(timeStamp)) < 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;

    }

    public static void sortDatesList(List<String> availableDatesList) {// supposed to sort the list availableDatesList
        Collections.sort(availableDatesList, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("dd.MM.yyyy");
            @Override
            public int compare(String o1, String o2) {
                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    //throw new IllegalArgumentException(e);
                    return 0;
                }
            }
        });
    }

    public static void sortHoursList(List<String> availableDatesList) {// supposed to sort the list availableDatesList
        Collections.sort(availableDatesList, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                try {
                    return new SimpleDateFormat("hh:mm").parse(o1).compareTo(new SimpleDateFormat("hh:mm").parse(o2));
                } catch (ParseException e) {
                    return 0;
                }
            }
        });
    }

//    private String getUserName(String uid){
//        String tmpUserName = "";
//
//        DocumentReference docRef = db.collection("users").document(uid);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//
//                    /* get the name of the trainee */
//                    DocumentSnapshot document = task.getResult();
//                    String userFirstNameString = (String)document.getData().get("first");
//                    String userLastNameString = (String)document.getData().get("last");
//                    tmpUserName = userFirstNameString + " " + userLastNameString;
//
//
//                    if (document.exists()) {
//                        Log.d("readData", "DocumentSnapshot data: " + document.getData());
//                    } else {
//                        Log.d("notFound", "No such document");
//                    }
//                } else {
//                    Log.d("NotConnectedGetUserName", "get failed with ", task.getException());
//                }
//            }
//        });
//        return tmpUserName;
//
//
//    }



}