package com.dynamic_host.pocketerhalchal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamic_host.pocketerhalchal.database.PocketContract;
import com.dynamic_host.pocketerhalchal.database.PocketContract.SignUpEntry;

public class SignInActivity extends AppCompatActivity {

    EditText etUserMail, etPassword;
    Button btSignIn;
    TextView tvSignUp, tvForgotPassword;
    public String userName, userMail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etUserMail = findViewById(R.id.etUserMail);
        etPassword = findViewById(R.id.etPassword);
        btSignIn = findViewById(R.id.btSignIn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);


        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMail = etUserMail.getText().toString().trim();
                userPassword = etPassword.getText().toString().trim();
                String[] signUpProjection = {SignUpEntry.SIGNUP_ID,
                        SignUpEntry.COLUMN_SIGNUP_EMAIL,
                        SignUpEntry.COLUMN_SIGNUP_PASSWORD};
                Cursor signUpCursor = getContentResolver().query(SignUpEntry.CONTENT_SIGNUP_URI, signUpProjection, null, null, null);
                int flag = 0;
                while (signUpCursor.moveToNext())
                {
                    int userMailColumnIndex = signUpCursor.getColumnIndex(SignUpEntry.COLUMN_SIGNUP_EMAIL);
                    int userPasswordColumnIndex = signUpCursor.getColumnIndex(SignUpEntry.COLUMN_SIGNUP_PASSWORD);
                    if (userMail.equals(signUpCursor.getString(userMailColumnIndex)) && userPassword.equals(signUpCursor.getString(userPasswordColumnIndex))){
                        flag = 1;
                        PocketContract.CURSOR_POSITION = signUpCursor.getPosition();
                        Toast.makeText(SignInActivity.this,"Welcome to Pocketer Halchal!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                if (flag == 0)
                    Toast.makeText(SignInActivity.this,"Invalid Information! Try Again.",Toast.LENGTH_SHORT).show();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
}