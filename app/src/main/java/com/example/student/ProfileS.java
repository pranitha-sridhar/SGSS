package com.example.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileS extends AppCompatActivity {

    final int IMAGE_REQUEST = 100;
    EditText name, colname, addno, dept;
    String gender;
    RadioGroup radioGroup;
    FirebaseAuth mAuth;
    Button save;
    ProgressBar progressBar;
    Uri filepath;
    FirebaseStorage storage;
    DatabaseReference db;
    ImageView imageView, camera;
    String picurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_s);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        name = findViewById(R.id.textname);
        colname = findViewById(R.id.textc);
        addno = findViewById(R.id.texta);
        dept = findViewById(R.id.textd);
        radioGroup = findViewById(R.id.rgroup);
        save = findViewById(R.id.saveb);
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseDatabase.getInstance().getReference("Users");
        imageView = findViewById(R.id.usericon);
        camera = findViewById(R.id.camera);
        picurl = "https://firebasestorage.googleapis.com/v0/b/sgss-f6a32.appspot.com/o/images%2Fusericon.png?alt=media&token=0c440220-3af0-4e6d-b3c8-f0e5c818c36f";

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
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + mAuth.getCurrentUser().getUid());
                    storageReference.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        picurl = uri.toString();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Picture Uploaded", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Picture failed to be Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });


                }


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();

                    String displayname = name.getText().toString();
                    String college_name = colname.getText().toString();
                    String addnumber = addno.getText().toString();
                    String dept_name = dept.getText().toString();

                    if (displayname.isEmpty()) {
                        name.setError("Field should not be empty");
                        name.requestFocus();
                        return;
                    }
                    if (college_name.isEmpty()) {
                        colname.setError("Field should not be empty");
                        colname.requestFocus();
                        return;
                    }
                    if (addnumber.isEmpty()) {
                        addno.setError("Field should not be empty");
                        addno.requestFocus();
                        return;
                    }
                    if (dept_name.isEmpty()) {
                        dept.setError("Field should not be empty");
                        dept.requestFocus();
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    final ProfileDatabase profileDatabase = new ProfileDatabase(displayname, college_name, addnumber, dept_name, gender, picurl);
                    db.child(uid).setValue(profileDatabase).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Saved. Verify your email id", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProfileS.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });

                }
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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            filepath = data.getData();
            Glide.with(this).load(filepath).into(imageView);

        }
    }


}
