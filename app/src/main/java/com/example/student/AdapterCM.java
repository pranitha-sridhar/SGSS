package com.example.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCM extends RecyclerView.Adapter<AdapterCM.holder> {
    private ArrayList<CardItem> cardItemArrayList;
    private OnItemClickListener mlistener;

    public AdapterCM(ArrayList<CardItem> cardItems) {
        this.cardItemArrayList = cardItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mlistener = listener;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list, parent, false);
        AdapterCM.holder vH = new AdapterCM.holder(view, mlistener);
        return vH;


    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int position) {
        CardItem cardItem = cardItemArrayList.get(position);
        holder.username.setText(cardItem.getUsername());
        holder.level.setText(cardItem.getLevel());
        holder.category.setText(cardItem.getCategory());
        holder.subcat.setText(cardItem.getSubcategory());
        holder.body.setText(cardItem.getBody());
        holder.status.setText(cardItem.getStatus());
        holder.reply.setText(cardItem.getReply());
        holder.addno.setText(cardItem.getAddno());
    }

    @Override
    public int getItemCount() {
        return cardItemArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView username, level, category, subcat, body, status, reply, addno;

        public holder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            username = itemView.findViewById(R.id.susername);
            level = itemView.findViewById(R.id.slevel);
            category = itemView.findViewById(R.id.scategory);
            subcat = itemView.findViewById(R.id.ssubcategory);
            body = itemView.findViewById(R.id.sbody);
            status = itemView.findViewById(R.id.status);
            reply = itemView.findViewById(R.id.reply);
            addno = itemView.findViewById(R.id.admissionnumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        listener.onItemClick(position, v);
                    }
                }
            });
        }


    }

}
