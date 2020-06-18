package com.example.student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
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

public class SignUp extends AppCompatActivity {
    EditText email, pswrd, cpswrd;
    Button SignIn;
    SignInButton googleSignin;
    RadioGroup radioGroup;
    int flag, flag1 = 1009;
    GoogleSignInClient signInClient;
    ProgressBar progressBar;
    String name, textc, texta, textd, gender, propic;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.text);
        pswrd = findViewById(R.id.text2);
        SignIn = findViewById(R.id.button);
        radioGroup = findViewById(R.id.rgroup);
        cpswrd = findViewById(R.id.text3);
        progressBar = findViewById(R.id.progressBar);
        googleSignin = findViewById(R.id.googlebutton);

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

        SignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String emailidd = email.getText().toString();
                final String password = pswrd.getText().toString();
                final String cpassword = cpswrd.getText().toString();

                if (!password.equals(cpassword)) {
                    cpswrd.setError("Passwords are not matching");
                    cpswrd.requestFocus();
                    return;
                }
                if (emailidd.isEmpty()) {
                    email.setError("Email ID should not be empty");
                    email.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    pswrd.setError("Password should not be empty");
                    pswrd.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailidd).matches()) {
                    email.setError("Mail ID is invalid");
                    email.requestFocus();
                    return;
                }
                if (flag == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(emailidd, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(SignUp.this, ProfileS.class);
                                            startActivity(intent);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            Toast.makeText(getApplicationContext(), "Signed In.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    }
                                });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(emailidd, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(SignUp.this, ProfileM.class);
                                            startActivity(intent);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            Toast.makeText(getApplicationContext(), "Signed In.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    }
                                });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        signInClient = GoogleSignIn.getClient(this, signInOptions);

        googleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent signInintent = signInClient.getSignInIntent();
                startActivityForResult(signInintent, flag1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                                mAuth.getCurrentUser();

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
                                startActivity(new Intent(getApplicationContext(), HomeS.class));


                            }
                        }
                    });
                }
            } catch (ApiException apiEx) {
                apiEx.printStackTrace();
            }

        }

    }
}
