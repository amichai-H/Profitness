package com.example.profitness;

import com.google.firebase.firestore.DocumentSnapshot;

public interface TaskToRun {
    public void run(DocumentSnapshot documentSnapshot);
}
