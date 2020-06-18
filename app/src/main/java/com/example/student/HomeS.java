package com.example.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;


public class HomeS extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    int backpress = 0;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_s);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        drawerLayout.closeDrawer(GravityCompat.START);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new frag_home(), "homeTag").commit();
        navigationView.setCheckedItem(R.id.home);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new frag_home(), "homeTag").commit();
                        break;
                    case R.id.status:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new frag_status(), "statusTag").commit();
                        break;
                    case R.id.profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new frag_profile(), "profileTag").commit();
                        break;
                    case R.id.contact:
                        Toast.makeText(getApplicationContext(), "Contact", Toast.LENGTH_SHORT).show();
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:pranithas2001@gmail.com"));
                        startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                        break;
                    case R.id.signout:
                        mAuth.signOut();
                        if (mAuth.getCurrentUser() == null) {
                            finish();
                            Toast.makeText(getApplicationContext(), "Signing Out", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(HomeS.this, Splash.class));
                        }
                        break;
                }
                return true;
            }
        });

        final View headerView = navigationView.getHeaderView(0);


        final String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String namedis = dataSnapshot.child("name").getValue().toString();
                String collegedis = dataSnapshot.child("college").getValue().toString();
                String picurl = dataSnapshot.child("picurl").getValue().toString();

                TextView navUsername = headerView.findViewById(R.id.nav_name);
                navUsername.setText(namedis);


                TextView navCollege = headerView.findViewById(R.id.nav_college);
                navCollege.setText(collegedis);

                ImageView imageView = headerView.findViewById(R.id.userpic);
                Glide.with(getApplication()).load(picurl).into(imageView);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            assert current != null;
            assert current.getTag() != null;
            if (current.getTag().equals("homeTag")) {
                backpress++;
                if (backpress == 1)
                    Toast.makeText(this, "Enter Back again", Toast.LENGTH_SHORT).show();
                if (backpress > 1) super.onBackPressed();
            }
            if (current.getTag().equals("profileTag") || current.getTag().equals("statusTag")) {
                backpress = 0;
                super.onBackPressed();
                drawerLayout.closeDrawer(GravityCompat.START);
            }

        }
    }

}
