package com.example.profitness;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    ShowMenuListCoach showMenuListCoach;
    List<modelMenu> modelMenuList;
    Context context;

    public CustomAdapter(ShowMenuListCoach showMenuListCoach, List<modelMenu> modelMenuList){
        this.showMenuListCoach = showMenuListCoach;
        this.modelMenuList = modelMenuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_menu_layout, parent,false);

        ViewHolder viewHolder = new ViewHolder(itemview);

        viewHolder.setOnClickListener(new ViewHolder.ClickListenr() {
            @Override
            public void onClickItem(View view, int position) {
                //this will call went coach select item

            }

            @Override
            public void onLongClickItem(View view, int position) {
                //this will call went coach select long item
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind view / set data
        holder.breakFastTV.setText(modelMenuList.get(position).getBreakFast());
        holder.lunchTV.setText(modelMenuList.get(position).getLunch());
        holder.dinnerTV.setText(modelMenuList.get(position).getDinner());
    }

    @Override
    public int getItemCount() {
        return modelMenuList.size();
    }
}
