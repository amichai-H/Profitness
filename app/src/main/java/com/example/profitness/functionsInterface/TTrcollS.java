package com.example.profitness.functionsInterface;

import android.widget.LinearLayout;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public interface TTrcollS {
    public void run(QueryDocumentSnapshot document, LinearLayout linearLayout);

}
