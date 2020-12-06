package com.dynamic_host.pocketerhalchal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.dynamic_host.pocketerhalchal.database.PocketContract;
import com.dynamic_host.pocketerhalchal.database.SharedPreference;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme
        setTheme(R.style.DarkTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the SignInActivity
         * and close this Splash-Screen after some seconds.*/
        String[] signUpProjection = {PocketContract.SignUpEntry.COLUMN_SIGNUP_USERNAME};
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
                    if (SharedPreference.getAppLockValue(getApplicationContext()) == 1){
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