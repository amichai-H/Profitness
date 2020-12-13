package com.example.profitness;

import android.app.ListActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HolderForHistory> {

    WorkoutHistoryLayout listHistory; // list of dates and th hours on this date
    List<TrainData> DataList; //  list of template - date and hours

    public HistoryAdapter(WorkoutHistoryLayout listHistory, List<TrainData> dataList) {
        this.listHistory = listHistory;
        DataList = dataList;
    }

    @NonNull
    @Override
    public HolderForHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.train_data_layout, parent,false);

        return new HolderForHistory(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderForHistory holder, int position) {
        holder.dateData.setText(DataList.get(position).getDate());
        holder.hoursData.setText((CharSequence) DataList.get(position).getHours());

    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView= LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.train_data_layout, parent,false);
//
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.
//    }
//
//    @Override
//    public int getItemCount() {
//        return DataList.size();
//    }
}
