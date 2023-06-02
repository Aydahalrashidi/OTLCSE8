package com.example.otlcse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import javax.xml.namespace.QName;

public class ProfileActivity extends AppCompatActivity {

    TextView profilename, profilelastname, profileemail, profileusername, profilephone, profileID;
    String password;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, profile, contactus, logout, map;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button EditBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        contactus = findViewById(R.id.contactus);
        map = findViewById(R.id.googlemap);
        logout = findViewById(R.id.logout);
        EditBTN = findViewById(R.id.editbtn);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, MainActivity.class);
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, ContactUsActivity.class);
            }
        });

        logout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
                sharedPref.edit().remove("userid").commit();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }));

        map.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, googlemap.class);
                startActivity(intent);
            }
        }));


        //retrieve data
        profilename = findViewById(R.id.profilename);
        profilelastname = findViewById(R.id.laname);
        profileID = findViewById(R.id.userIDD);
        profileusername = findViewById(R.id.username6);
        profileemail = findViewById(R.id.useremail);
        profilephone = findViewById(R.id.userphone);
//        showUserData();

        EditBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });
    }

    @Override
    protected void onResume() {

        showUserData();
        super.onResume();
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class second) {
        Intent intent = new Intent(activity, second);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    String userid;
    public void showUserData() {
        Intent intent = getIntent();

        SharedPreferences shared = getSharedPreferences("application", MODE_PRIVATE);
        userid = (shared.getString("userid", ""));
//        HelperClass helperClass = new HelperClass(first_name,last_name,phone,nid, email, username, MD5String);
        databaseReference = firebaseDatabase.getReference("users").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                System.out.println(snapshot);
                HelperClass value = snapshot.getValue(HelperClass.class);


                profilename.setText(value.firstname);
                profilelastname.setText(value.lastname);
                profileID.setText(value.nid);
                profileusername.setText(value.username);
                profilephone.setText(value.phone);
                profileemail.setText(value.email);

                password = value.password;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(ProfileActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void passUserData(){
        String username = profileusername.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    String usernamefromDB = snapshot.child(username).child("username").getValue(String.class);
                    String phonefromDB = snapshot.child(username).child("phone").getValue(String.class);
                    String emailfromDB = snapshot.child(username).child("email").getValue(String.class);
                    String passfromDB = snapshot.child(username).child("password").getValue(String.class);


                    Intent intent = new Intent(ProfileActivity.this ,EditProfileActivity.class);

                    intent.putExtra("username" , profileusername.getText());
                    intent.putExtra("phone" , profilephone.getText());
                    intent.putExtra("email" , profileemail.getText());
                    intent.putExtra("password" , password);
                    intent.putExtra("userid" , userid);

                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}