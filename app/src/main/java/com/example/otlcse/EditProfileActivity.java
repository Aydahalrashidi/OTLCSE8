package com.example.otlcse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    EditText Editemail , Editphone ,Editpassword;
    Button Update_Button ;
    String emailuser , phoneuser , passworduser ,usernameUser, userId;

    DatabaseReference reference ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        Editemail = findViewById(R.id.edit_email);
        Editphone = findViewById(R.id.edit_phone);
//        Editpassword = findViewById(R.id.edit_password);
        Update_Button = findViewById(R.id.Update_button);

        showData();

        Update_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmailChange() || isPhoneChange() || isPasswordChange()){
                    Toast.makeText(EditProfileActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EditProfileActivity.this, "No Change!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isEmailChange(){
        if (!emailuser.equals(Editemail.getText().toString())){

            reference.child(userId).child("email").setValue(Editemail.getText().toString());
            emailuser=Editemail.getText().toString();
            return true ;
        }
        else {return false ;}
    }

    public boolean isPhoneChange(){
        if (!phoneuser.equals(Editphone.getText().toString())){

            reference.child(userId).child("phone").setValue(Editphone.getText().toString());
            phoneuser=Editphone.getText().toString();
            return true ;
        } else {return false ;}
    }

    public boolean isPasswordChange(){
        if (!passworduser.equals(Editpassword.getText().toString())){

            reference.child(userId).child("password").setValue(Editpassword.getText().toString());

            passworduser=Editpassword.getText().toString();
            return true ;
        } else {return false ;}
    }

    public void showData(){

        Intent intent =getIntent();
        usernameUser = intent.getStringExtra("username");
        emailuser = intent.getStringExtra("email");
        phoneuser = intent.getStringExtra("phone");
        passworduser = intent.getStringExtra("password");
        userId = intent.getStringExtra("userid");


        Editemail.setText(emailuser);
        Editphone.setText(phoneuser);
//        Editpassword.setText(passworduser);

    }
}