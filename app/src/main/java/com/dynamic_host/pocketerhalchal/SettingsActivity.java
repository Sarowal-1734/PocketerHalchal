package com.dynamic_host.pocketerhalchal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamic_host.pocketerhalchal.database.PocketContract;
import com.dynamic_host.pocketerhalchal.database.PocketContract.SignUpEntry;
import com.dynamic_host.pocketerhalchal.database.SharedPreference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Switch swDarkMod, swAppLock;
    private RelativeLayout tvLanguage;
    private TextView tvUserName, tvCurrentLanguage, tvEnglish, tvBangla;
    private CircleImageView ivProfilePic;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    private Bitmap bitmap;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme
        if (SharedPreference.getThemeValue(this) == 1)
            setTheme(R.style.DarkTheme);
        else setTheme(R.style.LightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        swDarkMod = findViewById(R.id.swDarkMode);
        swAppLock = findViewById(R.id.swAppLock);
        tvLanguage = findViewById(R.id.tvLanguage);
        tvCurrentLanguage = findViewById(R.id.tvCurrentLanguage);
        tvUserName = findViewById(R.id.tvUserName);
        ivProfilePic = findViewById(R.id.ivProfilePic);



        //User Photo setup
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // making implicit intent to pick photo from external gallery
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery,PICK_IMAGE);
            }
        });

        //Username setup
        tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(SettingsActivity.this);
                ab.setTitle("Edit Username:");
                EditText userName = new EditText(SettingsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                userName.setLayoutParams(lp);
                userName.setText(tvUserName.getText().toString());  //Set Previous string to EditText
                userName.setInputType(InputType.TYPE_CLASS_TEXT); //Single Line EditText
                ab.setView(userName);

                //Setting positive "Save" Button
                ab.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                if (TextUtils.isEmpty(userName.getText().toString()))
                                    Toast.makeText(SettingsActivity.this,"Username Can't be Empty",Toast.LENGTH_SHORT).show();
                                else {
                                    ContentValues values = new ContentValues();
                                    values.put(SignUpEntry.COLUMN_SIGNUP_USERNAME,userName.getText().toString());
                                    //Setup Row Id
                                    Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,1);
                                    getContentResolver().update(uri,values,null,null);
                                    Toast.makeText(SettingsActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                                    displayUserName();
                                }
                            }
                        });
                //Setting Negative "Cancel" Button
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });
                ab.show();
            }
        });

        //Dark Mode Setup
        swDarkMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swDarkMod.isChecked()){
                    SharedPreference.setThemeValue(getApplicationContext(),1);
                    restartApp();
                }
                else {
                    SharedPreference.setThemeValue(getApplicationContext(),0);
                    restartApp();
                }
            }
        });

        //App Lock Setup
        swAppLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swAppLock.isChecked()) {
                    SharedPreference.setAppLockValue(getApplicationContext(), 1);
                    Toast.makeText(SettingsActivity.this,"App Lock Enabled",Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreference.setAppLockValue(getApplicationContext(), 0);
                    Toast.makeText(SettingsActivity.this,"App Lock Disabled",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Language setup
        tvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View languageView = inflater.inflate(R.layout.languages, null);
                AlertDialog.Builder ab = new AlertDialog.Builder(SettingsActivity.this);
                ab.setTitle("Choose Language");
                ab.setView(languageView);
                AlertDialog alertDialog = ab.create();
                alertDialog.show();
                tvEnglish = languageView.findViewById(R.id.lgEnglish);
                tvBangla = languageView.findViewById(R.id.lgBangla);
                tvEnglish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreference.setLanguageValue(getApplicationContext(),1);
                        Toast.makeText(SettingsActivity.this,"Language Changed To English",Toast.LENGTH_SHORT).show();
                        restartApp();
                        alertDialog.dismiss();
                    }
                });
                tvBangla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreference.setLanguageValue(getApplicationContext(),0);
                        Toast.makeText(SettingsActivity.this,"Language Changed To Bangla",Toast.LENGTH_SHORT).show();
                        restartApp();
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayUserPhoto();
        displayUserName();
        displaySwitches();
        displayLanguage();
    }

    private void restartApp() {
        startActivity(new Intent(this,SettingsActivity.class));
        finish();
    }

    private void displaySwitches() {

        if (SharedPreference.getThemeValue(this) == 1)
            swDarkMod.setChecked(true);
        else swDarkMod.setChecked(false);
        if (SharedPreference.getAppLockValue(this) == 1)
            swAppLock.setChecked(true);
        else swAppLock.setChecked(false);
    }

    //Setup userName From Database
    private void displayUserName() {
        String[] projection = {SignUpEntry.COLUMN_SIGNUP_USERNAME};
        Cursor cursor = getContentResolver().query(SignUpEntry.CONTENT_SIGNUP_URI,projection,null,null,null);
        int userNameColumnIndex = cursor.getColumnIndex(SignUpEntry.COLUMN_SIGNUP_USERNAME);
        cursor.moveToPosition(0);
        tvUserName.setText(cursor.getString(userNameColumnIndex));
        cursor.close();
    }
    //Setup userImage From Database
    private void displayUserPhoto() {
        String[] projection = {SignUpEntry.COLUMN_SIGNUP_IMAGE};
        Cursor cursor = getContentResolver().query(SignUpEntry.CONTENT_SIGNUP_URI,projection,null,null,null);
        int userImageColumnIndex = cursor.getColumnIndex(SignUpEntry.COLUMN_SIGNUP_IMAGE);
        cursor.moveToPosition(0);
        byte[] imageByte = cursor.getBlob(userImageColumnIndex);
        if (imageByte != null){
            Bitmap bt = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);
            ivProfilePic.setImageBitmap(bt);
            cursor.close();
        }
    }

    //Show Current Language From Database
    private void displayLanguage() {
        if (SharedPreference.getLanguageValue(this) == 1)
            tvCurrentLanguage.setText("English");
        else tvCurrentLanguage.setText("Bangla");
    }

    // Picking photo from external storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            try {
                //Update Image to Database
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                imageInBytes = byteArrayOutputStream.toByteArray();
                ContentValues values = new ContentValues();
                values.put(SignUpEntry.COLUMN_SIGNUP_IMAGE,imageInBytes);
                Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,1);
                long id = getContentResolver().update(uri,values,null,null);
                if (id!= -1)
                    Toast.makeText(SettingsActivity.this,"Image Update Successful!",Toast.LENGTH_SHORT).show();
                displayUserPhoto();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.change_password_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_change_password, null);
        switch (item.getItemId())
        {
            case R.id.changePassword:
                AlertDialog.Builder ab = new AlertDialog.Builder(SettingsActivity.this);
                ab.setTitle("Change Password");

                //Setting positive "Update" Button
                ab.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        EditText OldPassword = dialogView.findViewById(R.id.etOldPassword);
                        EditText NewPassword = dialogView.findViewById(R.id.etNewPassword);
                        EditText ConfirmPassword = dialogView.findViewById(R.id.etConfirmPassword);
                        String oldPassword = OldPassword.getText().toString();
                        String newPassword = NewPassword.getText().toString();
                        String confirmPassword = ConfirmPassword.getText().toString();
                        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword))
                            Toast.makeText(SettingsActivity.this,"Opps! Failed to Update Pin!",Toast.LENGTH_SHORT).show();
                        else{
                            //Checking Old Password
                            String[] projection = {SignUpEntry.COLUMN_SIGNUP_PASSWORD};
                            Cursor cursor = getContentResolver().query(SignUpEntry.CONTENT_SIGNUP_URI,projection,null,null,null);
                            int userPassColumnIndex = cursor.getColumnIndex(SignUpEntry.COLUMN_SIGNUP_PASSWORD);
                            cursor.moveToPosition(0);
                            if(oldPassword.equals(cursor.getString(userPassColumnIndex)) && newPassword.equals(confirmPassword)){
                                ContentValues values = new ContentValues();
                                values.put(SignUpEntry.COLUMN_SIGNUP_PASSWORD,newPassword);
                                //Setup Row Id
                                Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,1);
                                getContentResolver().update(uri,values,null,null);
                                Toast.makeText(SettingsActivity.this,"Password Changed!",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(SettingsActivity.this,"Password Doesn't Match!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                //Setting Negative "Cancel" Button
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });
                ab.setView(dialogView);
                ab.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}