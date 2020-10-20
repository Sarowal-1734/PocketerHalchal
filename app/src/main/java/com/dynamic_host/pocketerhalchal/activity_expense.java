package com.dynamic_host.pocketerhalchal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class activity_expense extends AppCompatActivity {

    Button btSubmit;
    EditText etExpense, etItem, etDate, etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        btSubmit = findViewById(R.id.btSubmit);
        etExpense = findViewById(R.id.etExpense);
        etItem = findViewById(R.id.etItem);
        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Expense Added Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_expense.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}