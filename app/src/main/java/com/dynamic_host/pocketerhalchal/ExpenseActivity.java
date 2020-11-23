package com.dynamic_host.pocketerhalchal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ExpenseActivity extends AppCompatActivity {

    Button btSubmit;
    EditText etExpense, etDescription;
    Spinner spExpenseItem;
    TextView tvDate, tvItem;
    private DatePickerDialog.OnDateSetListener myDateSetLister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        btSubmit = findViewById(R.id.btSubmit);
        etExpense = findViewById(R.id.etExpense);
        tvItem = findViewById(R.id.tvItem);
        spExpenseItem = findViewById(R.id.spExpenseItem);
        tvDate = findViewById(R.id.tvDate);
        etDescription = findViewById(R.id.etDescription);

        setupSpinner();

        tvDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(ExpenseActivity.this,
                        android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                        myDateSetLister,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        myDateSetLister = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                tvDate.setText(date);
            }
        };



        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Expense Added Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    private void setupSpinner() {
        ArrayAdapter incomeSourceAdapter = ArrayAdapter.createFromResource(ExpenseActivity.this, R.array.expense_item, R.layout.spinner_item);
        incomeSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExpenseItem.setAdapter(incomeSourceAdapter);

        spExpenseItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                //tvSource.setText("Source: "+selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}