package com.example.capston_design;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Signup3Adapter extends RecyclerView.Adapter<Signup3Adapter.VH>{

    ArrayList<Signup3dietItem> items;
    Context context;

    static int count = 0;

    public Signup3Adapter(ArrayList<Signup3dietItem> items,Context context){
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(context).inflate(R.layout.getdiet_list,parent,false);

        VH vh = new VH(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder,int position){

        Signup3dietItem item = items.get(position);

        holder.name.setText(item.getName());
        holder.idx.setText(item.getIdx());
        holder.name.setBackgroundResource(item.isSelected()? R.drawable.button_selectborder:R.drawable.button_border);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!item.isSelected()){
                    count +=1;
                } else {
                    if (count <1){
                        count = 0;
                    }
                    else {
                        count -=1;
                    }
                }

                Signup3Activity.progressBar.setProgress(count);
                Signup3Activity.progresstext.setText(count+"/10");

                item.setSelected(!item.isSelected());
                holder.name.setBackgroundResource(item.isSelected()? R.drawable.button_selectborder:R.drawable.button_border);


            }
        });


    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public Signup3dietItem getItem(int position){
        return items.get(position);
    }
    class VH extends RecyclerView.ViewHolder{
        TextView state,idx,name;
        View view;

        public VH(@NonNull final View itemView){
            super(itemView);

            view = itemView;
            idx = itemView.findViewById(R.id.getdietlist_idx);
            name = itemView.findViewById(R.id.getdietlist_button);
            state = itemView.findViewById(R.id.getdietlist_state);

        }
    }
}
