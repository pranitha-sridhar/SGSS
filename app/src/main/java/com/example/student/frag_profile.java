package com.example.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class frag_profile extends Fragment {
    final int IMAGE_REQUEST = 100;
    FirebaseAuth mAuth;
    RadioGroup radioGroup;
    String gender, picurl, propic;
    EditText textn, textc, texta, textd;
    Button saveb, camera, deleteb;
    ImageView imageView;
    ProgressBar progressBar;
    FirebaseStorage storage;
    Uri filepath;
    StorageReference reference;
    DatabaseReference db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        textn = view.findViewById(R.id.textname);
        textc = view.findViewById(R.id.textcollege);
        texta = view.findViewById(R.id.textaddno);
        textd = view.findViewById(R.id.textdept);
        radioGroup = view.findViewById(R.id.rgroup);
        saveb = view.findViewById(R.id.saveb);
        imageView = view.findViewById(R.id.profilepic);
        progressBar = view.findViewById(R.id.progressBar2);
        camera = view.findViewById(R.id.camera);
        deleteb = view.findViewById(R.id.deleteb);
        storage = FirebaseStorage.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Users");


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb1)
                    gender = "Male";
                else if (checkedId == R.id.rb2)
                    gender = "Female";
                else
                    gender = "Others";
            }
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    textn.setText(dataSnapshot.child("name").getValue().toString());
                    textc.setText(dataSnapshot.child("college").getValue().toString());
                    texta.setText(dataSnapshot.child("addNo").getValue().toString());
                    textd.setText(dataSnapshot.child("dept").getValue().toString());
                    gender = dataSnapshot.child("gender").getValue().toString();
                    picurl = dataSnapshot.child("picurl").getValue().toString();
                    Glide.with(view).load(picurl).into(imageView);
                    if (gender.equals("Male")) radioGroup.check(R.id.rb1);
                    if (gender.equals("Female")) radioGroup.check(R.id.rb2);
                    if (gender.equals("Others")) radioGroup.check(R.id.rb3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath != null) {
                    final StorageReference srefer = FirebaseStorage.getInstance().getReference().child("images/" + mAuth.getCurrentUser().getUid());

                    progressBar.setVisibility(View.VISIBLE);
                    srefer.delete();
                    reference = FirebaseStorage.getInstance().getReference().child("images/" + mAuth.getCurrentUser().getUid());
                    reference.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        picurl = uri.toString();
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(getContext(), "Picture Uploaded", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Picture failed to be Uploaded", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
                deleteb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + mAuth.getCurrentUser().getUid());
                        ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), "Picture Deleted", Toast.LENGTH_SHORT).show();
                                    picurl = "https://firebasestorage.googleapis.com/v0/b/sgss-f6a32.appspot.com/o/images%2Fusericon.png?alt=media&token=0c440220-3af0-4e6d-b3c8-f0e5c818c36f";
                                    Glide.with(getContext()).load(picurl).into(imageView);

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), "Picture failed to be deleted", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                });
            }
        });


        saveb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();

                    String displayname = textn.getText().toString();
                    String college_name = textc.getText().toString();
                    String addnumber = texta.getText().toString();
                    String dept_name = textd.getText().toString();

                    if (displayname.isEmpty()) {
                        textn.setError("Field should not be empty");
                        textn.requestFocus();
                        return;
                    }
                    if (college_name.isEmpty()) {
                        textc.setError("Field should not be empty");
                        textc.requestFocus();
                        return;
                    }
                    if (addnumber.isEmpty()) {
                        texta.setError("Field should not be empty");
                        texta.requestFocus();
                        return;
                    }
                    if (dept_name.isEmpty()) {
                        textd.setError("Field should not be empty");
                        textd.requestFocus();
                        return;
                    }


                    final ProfileDatabase profileDatabase = new ProfileDatabase(displayname, college_name, addnumber, dept_name, gender, picurl);
                    db.child(uid).setValue(profileDatabase).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(getContext(), "Profiled Updated.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), HomeS.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });

                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            filepath = data.getData();
            Glide.with(this).load(filepath).into(imageView);
        }
    }

}







