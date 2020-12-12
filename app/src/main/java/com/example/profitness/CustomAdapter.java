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


    public CustomAdapter(ShowMenuListCoach showMenuListCoach, List<modelMenu> modelMenuList){
        this.showMenuListCoach = showMenuListCoach;
        this.modelMenuList = modelMenuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_menu_layout, parent,false);

        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind view / set data
        holder.day.setText(modelMenuList.get(position).getDay());
        holder.breakFastTV.setText(modelMenuList.get(position).getBreakFast());
        holder.lunchTV.setText(modelMenuList.get(position).getLunch());
        holder.dinnerTV.setText(modelMenuList.get(position).getDinner());
    }

    @Override
    public int getItemCount() {
        return modelMenuList.size();
    }
}
