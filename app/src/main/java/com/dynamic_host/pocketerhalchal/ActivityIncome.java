package com.dynamic_host.pocketerhalchal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityIncome extends AppCompatActivity {

    Button btSubmit;
    EditText etIncome, etSource, etDate, etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        btSubmit = findViewById(R.id.btSubmit);
        etIncome = findViewById(R.id.etIncome);
        etSource = findViewById(R.id.etSource);
        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Income Added Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityIncome.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}