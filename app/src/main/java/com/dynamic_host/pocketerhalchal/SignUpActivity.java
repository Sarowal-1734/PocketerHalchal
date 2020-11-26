package com.dynamic_host.pocketerhalchal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dynamic_host.pocketerhalchal.database.PocketContract;
import com.dynamic_host.pocketerhalchal.database.PocketContract.SignUpEntry;

public class SignUpActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etPassword, etRetypePassword;
    Button btSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etRetypePassword = findViewById(R.id.etRetypePassword);
        btSignUp = findViewById(R.id.btSignUp);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUsername.getText().toString().trim();
                String userEmail = etEmail.getText().toString().trim();
                String userPassword = etPassword.getText().toString().trim();
                ContentValues values = new ContentValues();
                values.put(SignUpEntry.COLUMN_SIGNUP_USERNAME,userName);
                values.put(SignUpEntry.COLUMN_SIGNUP_EMAIL,userEmail);
                values.put(SignUpEntry.COLUMN_SIGNUP_PASSWORD,userPassword);
                Uri newUri = getContentResolver().insert(SignUpEntry.CONTENT_SIGNUP_URI,values);
                if (newUri == null)
                    Toast.makeText(SignUpActivity.this, "Registration Failed! Try Again.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SignUpActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}