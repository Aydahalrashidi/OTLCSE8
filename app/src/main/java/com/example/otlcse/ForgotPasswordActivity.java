package com.example.otlcse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText email = findViewById(R.id.input_email);

        findViewById(R.id.btnResetPassword).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String enteredEmail = email.getText().toString();
                if(enteredEmail.isEmpty())
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(enteredEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Log.d("TAG", "Email sent.");
                                    Toast.makeText(ForgotPasswordActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                                else
                                {
                                    Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}