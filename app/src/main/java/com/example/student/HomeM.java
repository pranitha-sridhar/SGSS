package com.example.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeM extends AppCompatActivity {
    public static String CHANNEL_ID = "SSS";
    ImageView closeb;
    FloatingActionButton filterb, signout;
    ImageView filterbutton;
    FirebaseAuth mAuth;
    ArrayList<CardItem> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    AdapterCM adapter;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout layout;
    int back = 0;
    CheckBox l1, l2, l3, c1, c2, c3, s11, s12, s13, s21, s22, s23, s24, s25, s26, s31, s32, s33, all;
    int fl1 = 0, fl2 = 0, fl3 = 0, fc1 = 0, fc2 = 0, fc3 = 0, fs11 = 0, fs12 = 0, fs13 = 0, fs21 = 0, fs22 = 0, fs23 = 0, fs24 = 0, fs25 = 0, fs26 = 0, fs31 = 0, fs32 = 0, fs33 = 0, a = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_m);

        signout = findViewById(R.id.signout);
        filterb = findViewById(R.id.filter);
        layout = findViewById(R.id.layout3);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        if (fl1 == 0 && fl2 == 0 && fl3 == 0 && fc1 == 0 && fc2 == 0 && fc3 == 0 && fs11 == 0 && fs12 == 0 && fs13 == 0 && fs21 == 0 && fs22 == 0 && fs23 == 0 && fs31 == 0 && fs32 == 0 && fs33 == 0 && fs24 == 0 && fs25 == 0 && fs26 == 0) {
            recyclerView.removeAllViews();
            arrayList.clear();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Complaints");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String childid = ds.getKey();
                        String uname = ds.child("username").getValue().toString();
                        String ulevel = ds.child("level").getValue().toString();
                        String uadd = ds.child("addno").getValue().toString();
                        String ucat = ds.child("category").getValue().toString();
                        String usub = ds.child("subcategory").getValue().toString();
                        String ubody = ds.child("body").getValue().toString();
                        String ureply = ds.child("reply").getValue().toString();
                        String ustatus = ds.child("status").getValue().toString();
                        String uper = ds.child("permission").getValue().toString();
                        String uid = ds.child("userid").getValue().toString();
                        arrayList.add(new CardItem(uname, uadd, ulevel, ucat, usub, ubody, ustatus, ureply, uid, uper, childid));


                    }
                    if (arrayList.isEmpty()) {
                        Toast.makeText(getApplication(), "Empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    adapter = new AdapterCM(arrayList);
                    recyclerView.setAdapter(adapter);


                    adapter.setOnItemClickListener(new AdapterCM.OnItemClickListener() {
                        @Override
                        public void onItemClick(final int position, View v) {

                            Toast.makeText(getApplicationContext(), "Opened", Toast.LENGTH_SHORT).show();
                            final AlertDialog.Builder alert = new AlertDialog.Builder(HomeM.this);
                            ViewGroup viewGroup = findViewById(R.id.content);
                            View customView = LayoutInflater.from(v.getContext()).inflate(R.layout.member_reply, viewGroup, false);

                            final TextView name = customView.findViewById(R.id.susernam);
                            final TextView addnumber = customView.findViewById(R.id.admissionnoo);
                            final TextView leve = customView.findViewById(R.id.sleve);
                            final TextView cat = customView.findViewById(R.id.scategor);
                            final TextView sub = customView.findViewById(R.id.ssubcategor);
                            final TextView bod = customView.findViewById(R.id.sbod);
                            final TextView statu = customView.findViewById(R.id.statu);
                            final EditText repl = customView.findViewById(R.id.repl);
                            Button save = customView.findViewById(R.id.savebutton);
                            ImageView close = customView.findViewById(R.id.closeButton);
                            final CardItem cardItem = arrayList.get(position);

                            alert.setView(customView);
                            final AlertDialog alertDialog = alert.create();
                            alertDialog.show();

                            name.setText(cardItem.getUsername());
                            leve.setText(cardItem.getLevel());
                            cat.setText(cardItem.getCategory());
                            sub.setText(cardItem.getSubcategory());
                            bod.setText(cardItem.getBody());
                            statu.setText(cardItem.getStatus());
                            addnumber.setText(cardItem.getAddno());


                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    recyclerView.removeAllViews();
                                    arrayList.clear();
                                    final String sreply = repl.getText().toString();
                                    cardItem.status = "Checked";
                                    cardItem.reply = sreply;

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complaints").child(cardItem.getChildid());
                                    reference.setValue(cardItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(v.getContext(), "Saved", Toast.LENGTH_SHORT).show();


                                        }
                                    });
                                    alertDialog.dismiss();

                                }
                            });
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(HomeM.this, "Signed Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        filterb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(HomeM.this);
                ViewGroup viewGroup = findViewById(R.id.content);
                View custom = LayoutInflater.from(v.getContext()).inflate(R.layout.filter_out, viewGroup, false);

                closeb = custom.findViewById(R.id.closeb);
                filterbutton = custom.findViewById(R.id.filbut);
                l1 = custom.findViewById(R.id.deptbox);
                l2 = custom.findViewById(R.id.collbox);
                l3 = custom.findViewById(R.id.unibox);
                c1 = custom.findViewById(R.id.addbox);
                c2 = custom.findViewById(R.id.acedembox);
                c3 = custom.findViewById(R.id.perbox);
                s11 = custom.findViewById(R.id.add1box);
                s12 = custom.findViewById(R.id.finbox);
                s13 = custom.findViewById(R.id.feebox);
                s21 = custom.findViewById(R.id.exambox);
                s22 = custom.findViewById(R.id.lectbox);
                s23 = custom.findViewById(R.id.revbox);
                s24 = custom.findViewById(R.id.attdbox);
                s25 = custom.findViewById(R.id.facbox);
                s26 = custom.findViewById(R.id.sylbox);
                s31 = custom.findViewById(R.id.healthbox);
                s32 = custom.findViewById(R.id.ragbox);
                s33 = custom.findViewById(R.id.profbox);
                all = custom.findViewById(R.id.allbox);

                alert.setView(custom);
                final AlertDialog alertDialog1 = alert.create();
                alertDialog1.show();

                if (fl1 == 1) l1.setChecked(true);
                if (fl2 == 1) l2.setChecked(true);
                if (fl3 == 1) l3.setChecked(true);
                if (fc1 == 1) c1.setChecked(true);
                if (fc2 == 1) c2.setChecked(true);
                if (fc3 == 1) c3.setChecked(true);
                if (fs11 == 1) s11.setChecked(true);
                if (fs12 == 1) s12.setChecked(true);
                if (fs13 == 1) s13.setChecked(true);
                if (fs21 == 1) s21.setChecked(true);
                if (fs22 == 1) s22.setChecked(true);
                if (fs23 == 1) s23.setChecked(true);
                if (fs24 == 1) s24.setChecked(true);
                if (fs25 == 1) s25.setChecked(true);
                if (fs26 == 1) s26.setChecked(true);
                if (fs31 == 1) s31.setChecked(true);
                if (fs32 == 1) s32.setChecked(true);
                if (fs33 == 1) s33.setChecked(true);
                if (a == 1) all.setChecked(true);


                closeb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });

                filterbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fl1 = 0;
                        fl2 = 0;
                        fl3 = 0;
                        fc1 = 0;
                        fc2 = 0;
                        fc3 = 0;
                        fs11 = 0;
                        fs12 = 0;
                        fs13 = 0;
                        fs21 = 0;
                        fs22 = 0;
                        fs23 = 0;
                        fs24 = 0;
                        fs25 = 0;
                        fs26 = 0;
                        fs31 = 0;
                        fs32 = 0;
                        fs33 = 0;
                        a = 0;

                        recyclerView.removeAllViews();
                        arrayList.clear();

                        if (all.isChecked()) {
                            a = 1;
                            s11.setChecked(false);
                            s12.setChecked(false);
                            s13.setChecked(false);
                            s21.setChecked(false);
                            s22.setChecked(false);
                            s23.setChecked(false);
                            s24.setChecked(false);
                            s25.setChecked(false);
                            s26.setChecked(false);
                            s31.setChecked(false);
                            s32.setChecked(false);
                            s33.setChecked(false);
                            l1.setChecked(false);
                            l2.setChecked(false);
                            l3.setChecked(false);
                            c1.setChecked(false);
                            c2.setChecked(false);
                            c3.setChecked(false);
                        }
                        if (c1.isChecked()) {
                            s11.setChecked(true);
                            s12.setChecked(true);
                            s13.setChecked(true);
                            fc1 = 1;
                            fs11 = 1;
                            fs12 = 1;
                            fs13 = 1;
                        }
                        if (c2.isChecked()) {
                            s21.setChecked(true);
                            s22.setChecked(true);
                            s23.setChecked(true);
                            s24.setChecked(true);
                            s25.setChecked(true);
                            s26.setChecked(true);
                            fc2 = 1;
                            fs21 = 1;
                            fs22 = 1;
                            fs23 = 1;
                            fs24 = 1;
                            fs25 = 1;
                            fs26 = 1;
                        }
                        if (c3.isChecked()) {
                            s31.setChecked(true);
                            s32.setChecked(true);
                            s33.setChecked(true);
                            fs31 = 1;
                            fs32 = 1;
                            fs33 = 1;
                            fc3 = 1;
                        }
                        if (l1.isChecked()) fl1 = 1;
                        if (l2.isChecked()) fl2 = 1;
                        if (l3.isChecked()) fl3 = 1;
                        if (s11.isChecked()) fs11 = 1;
                        if (s12.isChecked()) fs12 = 1;
                        if (s13.isChecked()) fs13 = 1;
                        if (s21.isChecked()) fs21 = 1;
                        if (s22.isChecked()) fs22 = 1;
                        if (s23.isChecked()) fs23 = 1;
                        if (s24.isChecked()) fs24 = 1;
                        if (s25.isChecked()) fs25 = 1;
                        if (s26.isChecked()) fs26 = 1;
                        if (s31.isChecked()) fs31 = 1;
                        if (s32.isChecked()) fs32 = 1;
                        if (s33.isChecked()) fs33 = 1;
                        Toast.makeText(getApplicationContext(), "Filtered", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog1.dismiss();
                            }
                        }, 500);


                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        Query query = firebaseDatabase.getReference("Complaints").orderByChild("permission").equalTo("Public");
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String childid = ds.getKey();
                                    String uname = ds.child("username").getValue().toString();
                                    String uadd = ds.child("addno").getValue().toString();
                                    String ulevel = ds.child("level").getValue().toString();
                                    String ucat = ds.child("category").getValue().toString();
                                    String usub = ds.child("subcategory").getValue().toString();
                                    String ubody = ds.child("body").getValue().toString();
                                    String ureply = ds.child("reply").getValue().toString();
                                    String ustatus = ds.child("status").getValue().toString();
                                    String uper = ds.child("permission").getValue().toString();
                                    String uid = ds.child("userid").getValue().toString();
                                    if ((fl1 == 1 && ulevel.equals("Department")) || (fl2 == 1 && ulevel.equals("College")) || (fl3 == 1 && ulevel.equals("University")) || (fl1 == 0 && fl2 == 0 && fl3 == 0 && fc1 == 0 && fc2 == 0 && fc3 == 0 && fs11 == 0 && fs12 == 0 && fs13 == 0 && fs21 == 0 && fs22 == 0 && fs23 == 0 && fs31 == 0 && fs32 == 0 && fs33 == 0 && fs24 == 0 && fs25 == 0 && fs26 == 0) ||
                                            (fs11 == 1 && usub.equals("Admission")) || (fs12 == 1 && usub.equals("Finance")) || (fs13 == 1 && usub.equals("Fee Issue")) ||
                                            (fs21 == 1 && usub.equals("Examination")) || (fs22 == 1 && usub.equals("Lecture Timings")) || (fs23 == 1 && usub.equals("Paper Revaluation")) || (fs24 == 1 && usub.equals("Attendence")) || (fs25 == 1 && usub.equals("Faculties")) || (fs26 == 1 && usub.equals("Syllabus Issue")) ||
                                            (fs31 == 1 && usub.equals("Health")) || (fs32 == 1 && usub.equals("Ragging")) || (fs33 == 1 && usub.equals("Complaint Against Professors")))
                                        arrayList.add(new CardItem(uname, uadd, ulevel, ucat, usub, ubody, ustatus, ureply, uid, uper, childid));


                                }
                                if (arrayList.isEmpty()) {
                                    Toast.makeText(getApplication(), "Empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                adapter = new AdapterCM(arrayList);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new AdapterCM.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(final int position, View v) {
                                        Toast.makeText(getApplicationContext(), "Opened", Toast.LENGTH_SHORT).show();
                                        final AlertDialog.Builder alert = new AlertDialog.Builder(HomeM.this);
                                        ViewGroup viewGroup = findViewById(R.id.content);
                                        View customView = LayoutInflater.from(v.getContext()).inflate(R.layout.member_reply, viewGroup, false);

                                        final TextView name = customView.findViewById(R.id.susernam);
                                        final TextView addno = customView.findViewById(R.id.admissionnoo);
                                        final TextView leve = customView.findViewById(R.id.sleve);
                                        final TextView cat = customView.findViewById(R.id.scategor);
                                        final TextView sub = customView.findViewById(R.id.ssubcategor);
                                        final TextView bod = customView.findViewById(R.id.sbod);
                                        final TextView statu = customView.findViewById(R.id.statu);
                                        final EditText repl = customView.findViewById(R.id.repl);

                                        Button save = customView.findViewById(R.id.savebutton);
                                        ImageView close = customView.findViewById(R.id.closeButton);
                                        final CardItem cardItem = arrayList.get(position);

                                        name.setText(cardItem.getUsername());
                                        leve.setText(cardItem.getLevel());
                                        cat.setText(cardItem.getCategory());
                                        sub.setText(cardItem.getSubcategory());
                                        bod.setText(cardItem.getBody());
                                        statu.setText(cardItem.getStatus());
                                        repl.setText(cardItem.getReply());
                                        addno.setText(cardItem.getAddno());

                                        alert.setView(customView);
                                        final AlertDialog alertDialog = alert.create();
                                        alertDialog.show();

                                        close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertDialog.dismiss();
                                            }
                                        });
                                        save.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(final View v) {
                                                final String sreply = repl.getText().toString();
                                                cardItem.reply = sreply;
                                                cardItem.status = "Checked";

                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complaints").child(cardItem.getChildid());
                                                reference.setValue(cardItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(v.getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                alertDialog.dismiss();

                                            }
                                        });
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        });


    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder alert = new AlertDialog.Builder(HomeM.this);
        alert.setTitle("EXIT");
        alert.setMessage("Do you want to exit the application?");
        alert.setCancelable(false);
        alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();


    }
}
