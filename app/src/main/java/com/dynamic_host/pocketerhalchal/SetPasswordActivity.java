package com.dynamic_host.pocketerhalchal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dynamic_host.pocketerhalchal.database.PocketContract;
import com.dynamic_host.pocketerhalchal.database.PocketContract.SignUpEntry;

public class SetPasswordActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etRetypePassword;
    Button btSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme
        setTheme(R.style.DarkTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etRetypePassword = findViewById(R.id.etRetypePassword);
        btSignUp = findViewById(R.id.btSignUp);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUsername.getText().toString().trim();
                String userPassword = etPassword.getText().toString().trim();
                String userConfirmPassword = etRetypePassword.getText().toString().trim();
                if (TextUtils.isEmpty(userName))
                    etUsername.setError("User Name Can't be empty");
                else if (TextUtils.isEmpty(userPassword))
                    etPassword.setError("Password can't be empty");
                else if (!userPassword.equals(userConfirmPassword))
                    etRetypePassword.setError("Password doesn't match");
                else {
                    ContentValues values = new ContentValues();
                    values.put(SignUpEntry.COLUMN_SIGNUP_USERNAME,userName);
                    values.put(SignUpEntry.COLUMN_SIGNUP_PASSWORD,userPassword);
                    values.put(SignUpEntry.COLUMN_SIGNUP_LOGINPIN,1); //AppLock Enable == 1
                    values.put(SignUpEntry.COLUMN_SIGNUP_THEME,1); //DarkMode == 1
                    Uri newUri = getContentResolver().insert(SignUpEntry.CONTENT_SIGNUP_URI,values);
                    if (newUri == null)
                        Toast.makeText(SetPasswordActivity.this, "Registration Failed! Try Again.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(SetPasswordActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SetPasswordActivity.this,SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}