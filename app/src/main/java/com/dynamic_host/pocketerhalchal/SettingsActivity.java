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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamic_host.pocketerhalchal.database.PocketContract;
import com.dynamic_host.pocketerhalchal.database.PocketContract.SignUpEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button btLogout;
    private TextView tvUserName;
    private CircleImageView ivProfilePic;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    private Bitmap bitmap;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btLogout = findViewById(R.id.btLogout);
        tvUserName = findViewById(R.id.tvUserName);
        ivProfilePic = findViewById(R.id.ivProfilePic);

        displayUserPhoto();
        displayUserName();

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
                                ContentValues values = new ContentValues();
                                values.put(SignUpEntry.COLUMN_SIGNUP_USERNAME,userName.getText().toString());
                                //Setup Row Id
                                Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,PocketContract.CURSOR_POSITION+1);
                                getContentResolver().update(uri,values,null,null);
                                Toast.makeText(SettingsActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                                displayUserName();
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

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                AlertDialog alertDialog;
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Toast.makeText(SettingsActivity.this,"Logging Out",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(SettingsActivity.this,"Welcome Back",Toast.LENGTH_SHORT).show();
                    }
                });
                // Creating Dialog
                alertDialog = builder.create();
                alertDialog.setTitle("Logout Alert");
                alertDialog.show();
            }
        });
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
                Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,PocketContract.CURSOR_POSITION+1);
                long id = getContentResolver().update(uri,values,null,null);
                if (id!= -1)
                    Toast.makeText(SettingsActivity.this,"Image Update Successful!",Toast.LENGTH_SHORT).show();
                displayUserPhoto();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
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

            case R.id.setupLoginPin:
                ab = new AlertDialog.Builder(SettingsActivity.this);
                ab.setTitle("Setup Login Pin");
                //Setting positive "Update" Button
                ab.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        ContentValues values = new ContentValues();
                        values.put(SignUpEntry.COLUMN_SIGNUP_LOGINPIN,1);
                        //Setup Row Id
                        Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,1);
                        getContentResolver().update(uri,values,null,null);
                        Toast.makeText(SettingsActivity.this,"Login Pin Enabled!",Toast.LENGTH_SHORT).show();
                    }
                });
                //Setting Negative "Cancel" Button
                ab.setNegativeButton("Disable", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(SignUpEntry.COLUMN_SIGNUP_LOGINPIN,0);
                        //Setup Row Id
                        Uri uri = ContentUris.withAppendedId(SignUpEntry.CONTENT_SIGNUP_URI,1);
                        getContentResolver().update(uri,contentValues,null,null);
                        Toast.makeText(SettingsActivity.this,"Login Pin Disabled!",Toast.LENGTH_SHORT).show();
                    }
                });
                ab.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}