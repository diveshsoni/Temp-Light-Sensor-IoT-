package com.example.divesh.smartblinds;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divesh on 10/11/2015.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<Readings> historyValues;

    public HistoryAdapter(List<Readings> historyValues){
        this.historyValues = historyValues;
    }

    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.history_layout, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.HistoryViewHolder holder, int position) {
        Readings sensorReading = historyValues.get(position);
        holder.history.setText(sensorReading.sensorReading);
    }

    @Override
    public int getItemCount() {return historyValues.size();    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        protected TextView history;

        public HistoryViewHolder(View v) {
            super(v);
            history =  (TextView) v.findViewById(R.id.historyTV);
        }
    }
}
