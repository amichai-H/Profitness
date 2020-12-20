package com.example.profitness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends ArrayAdapter<TrainData> {
    public HistoryAdapter(@NonNull Context context, @NonNull List<TrainData> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TrainData data=getItem(position);
        if (convertView==null){
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.train_data_item_layout,parent,false);

        }
        TextView dateTraining=(TextView)convertView.findViewById(R.id.text_view_dateData);
        dateTraining.setText(data.getDate());// just one hour
        ListView hoursTraining=(ListView)convertView.findViewById(R.id.list_view_hoursData);
        List<String> hoursList=(ArrayList<String>)data.getHours();
        //ArrayAdapter<String> adapter=new ArrayAdapter<String>(this.getContext(),R.layout.hour_list,hoursList);
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this.getContext(),R.layout.hour_list,R.id.singel_hour_item,hoursList);
        hoursTraining.setAdapter(adapter);
        return convertView;
    }






}
