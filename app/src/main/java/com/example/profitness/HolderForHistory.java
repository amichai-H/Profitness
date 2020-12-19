package com.example.profitness;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HolderForHistory extends RecyclerView.ViewHolder {
TextView dateData, hoursData;
View myView;

    public HolderForHistory(@NonNull View itemView) {
        super(itemView);
        myView=itemView;
        dateData=itemView.findViewById(R.id.text_view_dateData);
        hoursData=itemView.findViewById(R.id.list_view_hoursData);

    }
}
