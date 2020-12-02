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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamic_host.pocketerhalchal.database.PocketContract;
import com.dynamic_host.pocketerhalchal.database.PocketContract.SignUpEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    Switch swDarkMod, swAppLock;
    private TextView tvUserName;
    private CircleImageView ivProfilePic;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    private Bitmap bitmap;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme
        if (PocketContract.CURRENT_THEME == 1)
            setTheme(R.style.DarkTheme);
        else setTheme(R.style.LightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        swDarkMod = findViewById(R.id.swDarkMode);
        swAppLock = findViewById(R.id.swAppLock);
        tvUserName = findViewById(R.id.tvUserName);
        ivProfilePic = findViewById(R.id.ivProfilePic);

        displayUserPhoto();
        displayUserName();
        displaySwitches();

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
                    ContentValues values = new ContentValues();
                    values.put(SignUpEntry.COLUMN_SIGNUP_THEME,1);
                    //Setup Row Id
                    Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,1);
                    getContentResolver().update(uri,values,null,null);
                    Toast.makeText(SettingsActivity.this,"Dark Mode Enabled!",Toast.LENGTH_SHORT).show();
                    restartApp();
                }
                else {
                    ContentValues values = new ContentValues();
                    values.put(SignUpEntry.COLUMN_SIGNUP_THEME,0);
                    //Setup Row Id
                    Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,1);
                    getContentResolver().update(uri,values,null,null);
                    Toast.makeText(SettingsActivity.this,"Dark Mode Disable!",Toast.LENGTH_SHORT).show();
                    restartApp();
                }
            }
        });

        //App Lock Setup
        swAppLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swAppLock.isChecked()){
                    ContentValues values = new ContentValues();
                    values.put(SignUpEntry.COLUMN_SIGNUP_LOGINPIN,1);
                    //Setup Row Id
                    Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,1);
                    getContentResolver().update(uri,values,null,null);
                    Toast.makeText(SettingsActivity.this,"App Lock Enabled!",Toast.LENGTH_SHORT).show();
                }
                else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(SignUpEntry.COLUMN_SIGNUP_LOGINPIN,0);
                    //Setup Row Id
                    Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,1);
                    getContentResolver().update(uri,contentValues,null,null);
                    Toast.makeText(SettingsActivity.this,"App Lock Disabled!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }

    private void restartApp() {
        startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
        finish();
    }

    private void displaySwitches() {
        String[] projection = {SignUpEntry.COLUMN_SIGNUP_THEME, SignUpEntry.COLUMN_SIGNUP_LOGINPIN};
        Cursor cursor = getContentResolver().query(SignUpEntry.CONTENT_SIGNUP_URI,projection,null,null,null);
        cursor.moveToPosition(0);
        if (cursor.getInt(cursor.getColumnIndex(SignUpEntry.COLUMN_SIGNUP_THEME)) == 1)
            swDarkMod.setChecked(true);
        else swDarkMod.setChecked(false);
        if (cursor.getInt(cursor.getColumnIndex(SignUpEntry.COLUMN_SIGNUP_LOGINPIN)) == 1)
            swAppLock.setChecked(true);
        else swAppLock.setChecked(false);
        cursor.close();
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
                            cursor.moveToPosition(PocketContract.CURSOR_POSITION);
                            if(oldPassword.equals(cursor.getString(userPassColumnIndex)) && newPassword.equals(confirmPassword)){
                                ContentValues values = new ContentValues();
                                values.put(SignUpEntry.COLUMN_SIGNUP_PASSWORD,newPassword);
                                //Setup Row Id
                                Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,PocketContract.CURSOR_POSITION+1);
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