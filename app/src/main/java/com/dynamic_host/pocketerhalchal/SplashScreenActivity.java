package com.dynamic_host.pocketerhalchal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.dynamic_host.pocketerhalchal.database.PocketContract;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the SignInActivity
         * and close this Splash-Screen after some seconds.*/
        String[] signUpProjection = {PocketContract.SignUpEntry.COLUMN_SIGNUP_LOGINPIN};
        Cursor cursor = getContentResolver().query(PocketContract.SignUpEntry.CONTENT_SIGNUP_URI, signUpProjection, null, null, null);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (cursor.getCount()==0){
                    startActivity(new Intent(SplashScreenActivity.this,SetPasswordActivity.class));
                    finish();
                }
                else{
                    cursor.moveToPosition(0);
                    if(cursor.getInt(cursor.getColumnIndex(PocketContract.SignUpEntry.COLUMN_SIGNUP_LOGINPIN)) == 1){
                        startActivity(new Intent(SplashScreenActivity.this,SignInActivity.class));
                        cursor.close();
                        finish();
                    }
                    else{
                        startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                        cursor.close();
                        finish();
                    }
                }
            }
        }, 500);
    }
}