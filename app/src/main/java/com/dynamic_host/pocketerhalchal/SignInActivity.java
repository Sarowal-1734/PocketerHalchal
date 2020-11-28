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

    EditText etUserName, etPassword;
    Button btSignIn;
    TextView tvSignUp;
    public String userName, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btSignIn = findViewById(R.id.btSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);


        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = etUserName.getText().toString().trim();
                userPassword = etPassword.getText().toString().trim();
                String[] signUpProjection = {SignUpEntry.SIGNUP_ID,
                        SignUpEntry.COLUMN_SIGNUP_USERNAME,
                        SignUpEntry.COLUMN_SIGNUP_PASSWORD};
                Cursor signUpCursor = getContentResolver().query(SignUpEntry.CONTENT_SIGNUP_URI, signUpProjection, null, null, null);
                int flag = 0;
                while (signUpCursor.moveToNext())
                {
                    int userNameColumnIndex = signUpCursor.getColumnIndex(SignUpEntry.COLUMN_SIGNUP_USERNAME);
                    int userPasswordColumnIndex = signUpCursor.getColumnIndex(SignUpEntry.COLUMN_SIGNUP_PASSWORD);
                    if (userName.equals(signUpCursor.getString(userNameColumnIndex)) && userPassword.equals(signUpCursor.getString(userPasswordColumnIndex))){
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

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}