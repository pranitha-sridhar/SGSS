package com.example.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    public ArrayList<CardItem> cardItems;

    public StatusAdapter(ArrayList<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_s_status, parent, false);
        StatusAdapter.StatusViewHolder vH = new StatusAdapter.StatusViewHolder(view);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        CardItem cardItem = cardItems.get(position);
        holder.username.setText(cardItem.getUsername());
        holder.level.setText(cardItem.getLevel());
        holder.category.setText(cardItem.getCategory());
        holder.subcat.setText(cardItem.getSubcategory());
        holder.body.setText(cardItem.getBody());
        holder.status.setText(cardItem.getStatus());
        holder.reply.setText(cardItem.getReply());

    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {

        TextView username, level, category, subcat, body, status, reply;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.susername);
            level = itemView.findViewById(R.id.slevel);
            category = itemView.findViewById(R.id.scategory);
            subcat = itemView.findViewById(R.id.ssubcategory);
            body = itemView.findViewById(R.id.sbody);
            status = itemView.findViewById(R.id.status);
            reply = itemView.findViewById(R.id.reply);
        }
    }
}
