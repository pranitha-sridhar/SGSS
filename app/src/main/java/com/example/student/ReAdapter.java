package com.example.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReAdapter extends RecyclerView.Adapter<ReAdapter.viewHolder> {
    private ArrayList<CardItem> cardItemArrayList;

    public ReAdapter(ArrayList<CardItem> arrayList) {
        cardItemArrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_complaints, parent, false);
        viewHolder vH = new viewHolder(view);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CardItem cardItem = cardItemArrayList.get(position);
        holder.username.setText(cardItem.getUsername());
        holder.level.setText(cardItem.getLevel());
        holder.category.setText(cardItem.getCategory());
        holder.subcat.setText(cardItem.getSubcategory());
        holder.body.setText(cardItem.getBody());

    }

    @Override
    public int getItemCount() {
        return cardItemArrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView username, level, category, subcat, body;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            level = itemView.findViewById(R.id.level);
            category = itemView.findViewById(R.id.category);
            subcat = itemView.findViewById(R.id.subcategory);
            body = itemView.findViewById(R.id.body);
        }
    }


}