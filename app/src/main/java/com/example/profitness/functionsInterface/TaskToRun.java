package com.example.profitness.functionsInterface;

import com.google.firebase.firestore.DocumentSnapshot;

public interface TaskToRun {
    public void run(DocumentSnapshot documentSnapshot);
}
