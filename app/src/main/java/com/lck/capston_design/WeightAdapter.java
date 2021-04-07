package com.lck.capston_design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeightAdapter extends RecyclerView.Adapter {
    ArrayList<WeightItem> items;
    Context context;


    public WeightAdapter(ArrayList<WeightItem> items, Context context){
        this.items=items;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(context).inflate(R.layout.yoga_listview,parent,false);

        WeightAdapter.VH vh = new WeightAdapter.VH(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position){
        WeightAdapter.VH vh=(WeightAdapter.VH)holder;

        WeightItem item = items.get(position);
        vh.date.setText(item.getDate());
        vh.weight.setText(item.getWeight());
        vh.ect.setText(item.getEct());
    }

    @Override
    public int getItemCount(){
        return items.size();
    }


    public WeightItem getItem(int position){
        return items.get(position);
    }

    class VH extends RecyclerView.ViewHolder{
        TextView date,weight,ect;

        public VH(@NonNull final View itemView){
            super(itemView);

            date=itemView.findViewById(R.id.info_weight_date);
            weight = itemView.findViewById(R.id.info_weight_weight);
            ect = itemView.findViewById(R.id.info_weight_ect);


        }
    }
}
