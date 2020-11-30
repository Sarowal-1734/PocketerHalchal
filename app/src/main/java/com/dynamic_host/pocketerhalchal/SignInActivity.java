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

    EditText etPassword;
    Button btSignIn;
    public String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etPassword = findViewById(R.id.etPassword);
        btSignIn = findViewById(R.id.btSignIn);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPassword = etPassword.getText().toString().trim();
                String[] signUpProjection = {SignUpEntry.COLUMN_SIGNUP_PASSWORD};
                Cursor signUpCursor = getContentResolver().query(SignUpEntry.CONTENT_SIGNUP_URI, signUpProjection, null, null, null);
                int flag = 0;
                while (signUpCursor.moveToNext())
                {
                    int userPasswordColumnIndex = signUpCursor.getColumnIndex(SignUpEntry.COLUMN_SIGNUP_PASSWORD);
                    if (userPassword.equals(signUpCursor.getString(userPasswordColumnIndex))){
                        flag = 1;
                        PocketContract.CURSOR_POSITION = signUpCursor.getPosition();
                        Toast.makeText(SignInActivity.this,"Welcome to Pocketer Halchal!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                if (flag == 0)
                    Toast.makeText(SignInActivity.this,"Wrong Pin! Please Try Again.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}