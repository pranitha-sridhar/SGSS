package com.example.student;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Complaint extends AppCompatActivity {
    Button button;
    FirebaseAuth mAuth;
    Spinner spinner1, spinner2, spinner3;
    EditText editText;
    Switch aSwitch, bSwitch;
    String[] level = {"Department", "College", "University"};
    String[] category = {"Admission", "Academics", "Personal"};
    String[] sub1 = {"Admission", "Finance", "Fee issue"};
    String[] sub2 = {"Examination", "Lecture Timings", "Paper Revaluation", "Attendence", "Faculties", "Syllabus issue"};
    String[] sub3 = {"Health", "Ragging", "Complaint Against Professors"};
    String slevel, scategory, ssubcategory, complaint, username, usernameid, upermission = "Public";
    int flag = 0;
    int flag1 = 0, flag2 = 0;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        button = findViewById(R.id.button);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        editText = findViewById(R.id.editt);
        aSwitch = findViewById(R.id.swiitchh);
        bSwitch = findViewById(R.id.switchpp);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    usernameid = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag1 = 0;
                if (isChecked) flag1 = 1;


            }
        });
        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag2 = 0;
                if (isChecked) flag2 = 1;
            }
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, level);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                slevel = level[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, category);
        spinner2.setAdapter(arrayAdapter1);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scategory = category[position];
                flag = position;
                if (flag == 0) {

                    ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, sub1);
                    spinner3.setAdapter(arrayAdapter2);
                    spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ssubcategory = sub1[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                if (flag == 1) {

                    ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, sub2);
                    spinner3.setAdapter(arrayAdapter2);
                    spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ssubcategory = sub2[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                if (flag == 2) {

                    ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, sub3);
                    spinner3.setAdapter(arrayAdapter2);
                    spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ssubcategory = sub3[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaint = editText.getText().toString();
                if (complaint.isEmpty()) {
                    editText.setError("Field should not be empty");
                    editText.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                if (flag1 == 1) username = "Anonymous";
                else username = usernameid;
                if (flag2 == 1) upermission = "Private";
                if (flag2 == 0) upermission = "Public";


                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Complaints");
                String childid = databaseReference.push().getKey();
                CardItem cardItem = new CardItem(username, slevel, scategory, ssubcategory, complaint, "not checked", "no reply", mAuth.getCurrentUser().getUid(), upermission, childid);
                databaseReference.child(childid).setValue(cardItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), HomeS.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 1000);

                    }
                });


            }
        });
    }
}
