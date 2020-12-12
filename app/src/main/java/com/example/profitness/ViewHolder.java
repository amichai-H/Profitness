package com.example.profitness;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView breakFastTV, lunchTV, dinnerTV, day;
    View mView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        day = itemView.findViewById(R.id.dayId);
        breakFastTV = itemView.findViewById(R.id.editTextBreakFast);
        lunchTV = itemView.findViewById(R.id.editTextLunch);
        dinnerTV = itemView.findViewById(R.id.editTextDinnerId);
    }

}
