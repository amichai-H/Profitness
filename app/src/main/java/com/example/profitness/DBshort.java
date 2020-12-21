package com.example.profitness;

import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.profitness.functionsInterface.TTRCollection;
import com.example.profitness.functionsInterface.TTrcollS;
import com.example.profitness.functionsInterface.TaskToRun;
import com.example.profitness.functionsInterface.VoidFunc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class DBshort {
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseAuth mAuth;


    final String availableDates = "availableDates", users = "users",hours = "hours",dates = "dates",trainingInformation = "trainingInformation";
    String allTrainings = "allTrainings",format = "dd.MM.yyyy";
    public DBshort(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

    }

    public void insertDocToUser(String uid,Map<String, Object> userDB, VoidFunc onSucces) {
        insertDoc("users/"+uid,userDB,onSucces);
    }


    public void getUser(String uid, TaskToRun taskToRun ){
        getDocument(users+"/"+uid,taskToRun);
    }

    public void getAllUsers(TTRCollection taskToRun, VoidFunc endFor){
        getCollection("users",taskToRun,endFor);
    }

    public void getAvailableDates(String cUid,TTRCollection taskToRun,VoidFunc endFor){
        getCollection(trainingInformation+"/"+cUid+"/"+availableDates,taskToRun,endFor);
    }

    public void getHoursAvailableDates(String cUid,String date,TTRCollection taskToRun,VoidFunc endFor){
        getCollection(trainingInformation+"/"+cUid+"/"+availableDates+"/"+date+"/"+hours,taskToRun,endFor);
    }

    public void getAlltrainingHours(String cUid,String pathDay, String hours, TTRCollection addToList, VoidFunc onEnd) {
        getCollection(trainingInformation+"/"+cUid+"/"+allTrainings+'/'+pathDay+'/'+hours,addToList,onEnd);
    }

    public void getCollection(String path,TTRCollection taskToRun,VoidFunc endFor){
        db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                taskToRun.run(document);
                            }
                            endFor.run();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getDocument(String path,TaskToRun taskToRun){
        DocumentReference docRef = db.document(path);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    taskToRun.run(document);
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


    public void insertDoc(String path, Map<String, Object> userDB,VoidFunc onSucces){
        db.document(path)
                .set(userDB)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("add new user to db", "DocumentSnapshot successfully written!");
                        onSucces.run();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("fail add new user to db", "Error writing document", e);
                    }
                });
    }

    public void insertDocAvaDates(String cUid,String dateString, Map<String, Object> docData2,VoidFunc onSucces) {
        insertDoc(trainingInformation+"/"+cUid+"/"+availableDates+"/"+dateString,docData2,onSucces);
    }

    public void insertDocAvaDatesHours(String cUid,String date,String hour , Map<String, Object> docData,VoidFunc onSucces) {
        insertDoc(trainingInformation+"/"+cUid+"/"+availableDates+"/"+date+"/"+hours+"/"+hour,docData,onSucces);
    }
    public void getCollection(String path, TTrcollS taskToRun, VoidFunc endFor, LinearLayout linearLayout){
        db.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                taskToRun.run(document,linearLayout);
                            }
                            endFor.run();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



}
