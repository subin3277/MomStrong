package com.lck.capston_design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class YogaAdapter extends RecyclerView.Adapter implements OnPersonItemClickListener_yoga {

    ArrayList<YogaItem> items;
    Context context;
    OnPersonItemClickListener_yoga listener;

    public YogaAdapter(ArrayList<YogaItem> items, Context context){
        this.items=items;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){

        View itemView = LayoutInflater.from(context).inflate(R.layout.yoga_listview,parent,false);

        VH vh = new VH(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position){
        VH vh=(VH)holder;

        YogaItem item = items.get(position);
        vh.pose.setText(item.getPose());

        Glide.with(context).load(item.getPath()).into(vh.path);
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public void setOnItemClicklistener(OnPersonItemClickListener_yoga listener){
        this.listener=listener;
    }

    @Override
    public void onItemClick(VH holder, View view, int position) {
        if (listener!=null){
            listener.onItemClick(holder,view,position);
        }
    }

    public YogaItem getItem(int position){
        return items.get(position);
    }

    class VH extends RecyclerView.ViewHolder{
        TextView pose;
        ImageView path;

        public VH(@NonNull final View itemView){
            super(itemView);

            pose=itemView.findViewById(R.id.listview_pose);
            path = itemView.findViewById(R.id.listview_path);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if (listener!=null){
                        listener.onItemClick(VH.this,view,pos);
                    }

                }
            });
        }
    }
}
