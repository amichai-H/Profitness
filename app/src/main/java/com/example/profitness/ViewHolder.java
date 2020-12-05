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

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClickItem(v,getAdapterPosition());
            }
        });

        day = itemView.findViewById(R.id.dayId);
        breakFastTV = itemView.findViewById(R.id.editTextBreakFast);
        lunchTV = itemView.findViewById(R.id.editTextLunch);
        dinnerTV = itemView.findViewById(R.id.editTextDinnerId);
    }

    private ViewHolder.ClickListenr mClickListener;

    //interface with click listener
    public interface ClickListenr{
        void onClickItem(View view, int position);

    }

}
