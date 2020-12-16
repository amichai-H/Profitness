package com.example.profitness;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public interface TTRCollection {
    public void run(QueryDocumentSnapshot document);
}
