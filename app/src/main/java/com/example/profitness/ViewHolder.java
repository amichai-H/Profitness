package com.example.profitness;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView breakFastTV, lunchTV, dinnerTV, option;
    View mView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        option = itemView.findViewById(R.id.optionId);
        breakFastTV = itemView.findViewById(R.id.editTextBreakFast);
        lunchTV = itemView.findViewById(R.id.editTextLunch);
        dinnerTV = itemView.findViewById(R.id.editTextDinnerId);
    }

}
