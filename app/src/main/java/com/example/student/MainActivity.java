package com.example.student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    EditText email, pswrd;
    Button login;
    TextView Sign, forgetpass;
    RadioGroup radioGroup;
    int flag, flag1 = 0;
    ProgressBar progressBar;
    SignInButton googleSignInButton;
    GoogleSignInClient signInClient;
    String textc, texta, textd, gender, name, propic;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        {
            if (mAuth.getCurrentUser() != null) {
                if (mAuth.getCurrentUser().isEmailVerified()) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                                startActivity(new Intent(MainActivity.this, HomeM.class));
                            } else startActivity(new Intent(MainActivity.this, HomeS.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == flag1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);

                if (googleSignInAccount != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

                    mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (flag == 0) {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                                                textc = "Enter College name";
                                                texta = "Enter Admission No";
                                                textd = "Enter your department";
                                                gender = "Male";
                                                name = mAuth.getCurrentUser().getDisplayName();
                                                propic = String.valueOf(mAuth.getCurrentUser().getPhotoUrl());
                                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                                                final ProfileDatabase profileDatabase = new ProfileDatabase(name, textc, texta, textd, gender, propic);
                                                db.setValue(profileDatabase);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {


                                        }
                                    });
                                    progressBar.setVisibility(View.GONE);

                                    Toast.makeText(getApplicationContext(), "Log In Successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), HomeS.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();


                                } else {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users_Prof");
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                                                textc = "Enter College name";
                                                texta = "Enter Employee Code";
                                                textd = "Enter your department";
                                                name = mAuth.getCurrentUser().getDisplayName();
                                                propic = String.valueOf(mAuth.getCurrentUser().getPhotoUrl());
                                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users_Prof").child(mAuth.getCurrentUser().getUid());
                                                final ProfileDatabase profileDatabase = new ProfileDatabase(name, textc, texta, textd, "", propic);
                                                db.setValue(profileDatabase);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {


                                        }
                                    });
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Log In Successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), HomeM.class));
                                }
                            }
                        }
                    });
                }
            } catch (ApiException apiEx) {
                apiEx.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.text);
        pswrd = findViewById(R.id.text2);
        login = findViewById(R.id.button);
        Sign = findViewById(R.id.button2);
        radioGroup = findViewById(R.id.rgroup);
        googleSignInButton = findViewById(R.id.googlebutton);
        forgetpass = findViewById(R.id.forgotb);
        progressBar = findViewById(R.id.progressBar);


        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        signInClient = GoogleSignIn.getClient(this, signInOptions);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent signInintent = signInClient.getSignInIntent();
                startActivityForResult(signInintent, flag1);
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radio1) {
                    flag = 0;
                } else if (checkedId == R.id.radio2) {
                    flag = 1;
                }

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailid = email.getText().toString();
                final String password = pswrd.getText().toString();
                if (emailid.isEmpty()) {
                    email.setError("Email ID should not be empty");
                    email.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    pswrd.setError("Password should not be empty");
                    pswrd.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailid).matches()) {
                    email.setError("Mail ID is invalid");
                    email.requestFocus();
                    return;
                }

                if (flag == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference dreference = FirebaseDatabase.getInstance().getReference().child("Users");
                                dreference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(MainActivity.this, "User is not a student", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                                progressBar.setVisibility(View.GONE);
                                                finish();
                                                Intent intent = new Intent(MainActivity.this, HomeS.class);
                                                startActivity(intent);
                                                Toast.makeText(getApplicationContext(), "Logged In ", Toast.LENGTH_SHORT).show();
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Please verify email id", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            } else
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference dreference = FirebaseDatabase.getInstance().getReference().child("Users_Prof");
                                dreference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(MainActivity.this, "User is not a Committee Member", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                                progressBar.setVisibility(View.GONE);
                                                finish();
                                                Intent intent = new Intent(MainActivity.this, HomeM.class);
                                                startActivity(intent);
                                                Toast.makeText(getApplicationContext(), "Logged In ", Toast.LENGTH_SHORT).show();
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Please verify email id", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            } else
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }


            }
        });

        Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String emailid = email.getText().toString();
                if (emailid.isEmpty()) {
                    email.setError("Field required");
                    email.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(emailid).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Password link is sent to your mail id. Please check", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
