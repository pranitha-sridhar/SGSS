package com.example.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class frag_status extends Fragment {

    FirebaseAuth mAuth;
    FloatingActionButton createbutton;
    ArrayList<CardItem> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar pBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_status_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recycler2);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        pBar = view.findViewById(R.id.pbar);
        createbutton = view.findViewById(R.id.createb);

        createbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBar.setVisibility(View.GONE);
                startActivity(new Intent(getContext(), Complaint.class));
            }
        });

        Query query = FirebaseDatabase.getInstance().getReference("Complaints").orderByChild("userid").equalTo(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //arrayList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String childid = ds.getKey();
                        String uname = ds.child("username").getValue().toString();
                        String uaddno = ds.child("addno").getValue().toString();
                        String ulevel = ds.child("level").getValue().toString();
                        String ucat = ds.child("category").getValue().toString();
                        String usub = ds.child("subcategory").getValue().toString();
                        String ubody = ds.child("body").getValue().toString();
                        String ureply = ds.child("reply").getValue().toString();
                        String ustatus = ds.child("status").getValue().toString();
                        String uuserid = ds.child("userid").getValue().toString();
                        String uper = ds.child("permission").getValue().toString();
                        arrayList.add(new CardItem(uname, uaddno, ulevel, ucat, usub, ubody, ustatus, ureply, uuserid, uper, childid));
                    }
                    if (arrayList.isEmpty()) {
                        Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
                        pBar.setVisibility(View.GONE);
                        return;
                    }
                    adapter = new StatusAdapter(arrayList);
                    recyclerView.setAdapter(adapter);
                    pBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
