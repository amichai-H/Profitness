package com.example.profitness;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView breakFastTV, lunchTV, dinnerTV;
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

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onLongClickItem(v,getAdapterPosition());
                return true;
            }
        });

        breakFastTV = itemView.findViewById(R.id.breakFastId2);
        lunchTV = itemView.findViewById(R.id.lunchId2);
        dinnerTV = itemView.findViewById(R.id.dinnerId2);
    }

    private ViewHolder.ClickListenr mClickListener;
    public interface ClickListenr{
        void onClickItem(View view, int position);
        void onLongClickItem(View view, int position);

    }

    public void setOnClickListener(ViewHolder.ClickListenr clickListener){
        mClickListener = clickListener;
    }
}
