package com.dynamic_host.pocketerhalchal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    Button btLogout;
    TextView tvUserName;
    CircleImageView ivProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btLogout = findViewById(R.id.btLogout);
        tvUserName = findViewById(R.id.tvUserName);
        ivProfilePic = findViewById(R.id.ivProfilePic);

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this,"Add Photos from gallery",Toast.LENGTH_SHORT).show();
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

                //Setting Negative "Save" Button
                ab.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                tvUserName.setText(userName.getText().toString());
                                Toast.makeText(SettingsActivity.this,"Saved",Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(SettingsActivity.this,activity_signIn.class);
                startActivity(intent);
                finish();
            }
        });

    }
}