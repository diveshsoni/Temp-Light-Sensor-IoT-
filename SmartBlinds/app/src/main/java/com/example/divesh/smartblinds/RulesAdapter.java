package com.example.divesh.smartblinds;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Divesh on 10/6/2015.
 */
public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.RulesViewHolder> {

    private static List<Rules> allRules;

    public RulesAdapter(List<Rules> allRules) {
        this.allRules = allRules;
    }

    @Override
    public RulesAdapter.RulesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.rules_layout, parent, false);
        return new RulesViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(RulesAdapter.RulesViewHolder holder, int position) {
        Rules rule = allRules.get(position);
        holder.ruleString.setText(rule.ruleMsg);
    }

    @Override
    public int getItemCount() {
        return allRules.size();
    }

    public static class RulesViewHolder extends RecyclerView.ViewHolder {

        protected TextView ruleString;

        public RulesViewHolder(View v) {
            super(v);
            ruleString =  (TextView) v.findViewById(R.id.ruleTV);
        }
    }

    public  void swap(List<Rules> rules){
        allRules.clear();
        allRules.addAll(rules);
        notifyDataSetChanged();
    }
}
