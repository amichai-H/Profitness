package com.example.profitness;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class DBshort {
    FirebaseFirestore db;
    final String availableDates = "availableDates", users = "users",hours = "hours";
    String allTrainings = "allTrainings",format = "dd.MM.yyyy";
    public DBshort(){
        db = FirebaseFirestore.getInstance();
    }

    public void insertDocToUser(String uid,Map<String, Object> userDB, VoidFunc onSucces) {
        insertDoc("users/"+uid,userDB,onSucces);
    }

    public void getUser(String uid,TaskToRun taskToRun ){
        getDocument("users/"+uid,taskToRun);
    }

    public void getAllUsers(TTRCollection taskToRun,VoidFunc endFor){
        getCollection("users",taskToRun,endFor);
    }

    public void getAvailableDates(TTRCollection taskToRun,VoidFunc endFor){
        getCollection("availableDates",taskToRun,endFor);
    }

    public void getHoursAvailableDates(String date,TTRCollection taskToRun,VoidFunc endFor){
        getCollection(availableDates+"/"+date+"/"+hours,taskToRun,endFor);
    }

    public void getAlltrainingHours(String pathDay, String hours, TTRCollection addToList, VoidFunc onEnd) {
        getCollection(allTrainings+'/'+pathDay+'/'+hours,addToList,onEnd);
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

    public void insertDocAvaDates(String dateString, Map<String, Object> docData2,VoidFunc onSucces) {
        insertDoc(availableDates+"/"+dateString,docData2,()->{});
    }

    public void insertDocAvaDatesHours(String date,String hour , Map<String, Object> docData,VoidFunc onSucces) {
        insertDoc(availableDates+"/"+date+"/"+hours+"/"+hour,docData,()->{});
    }


}
