package com.lck.capston_design;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.ItemViewHolder> implements ItemTouchHelperListener {
    ArrayList<DietItem> items = new ArrayList<>();

    static String item="notnew";

    public DietAdapter(){

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        //LayoutInflater를 이용해서 원하는 레이아웃을 띄워줌
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.diet_listview, parent, false);
        return new ItemViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //ItemViewHolder가 생성되고 넣어야할 코드들을 넣어준다.
        holder.onBind(items.get(position));
    }
    @Override
    public int getItemCount() { return items.size(); }
    public void addItem(DietItem person){ items.add(person); }
    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        DietItem person = items.get(from_position);
        //이동할 객체 삭제
        items.remove(from_position);
        //이동하고 싶은 position에 추가
        items.add(to_position,person);
        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position,to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,items.size());

    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,items.size());

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView list_name,list_age,list_null;
        public ItemViewHolder(View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.dietlist_textview);
            list_age = itemView.findViewById(R.id.dietlist_textview2);

        }
        public void onBind(DietItem person) {
            list_name.setText(person.getName());
            list_age.setText(String.valueOf(person.getNutirition()));

        }
    }

}
