package com.dynamic_host.pocketerhalchal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamic_host.pocketerhalchal.database.PocketContract;
import com.dynamic_host.pocketerhalchal.database.PocketContract.IncomeEntry;

import java.util.Calendar;

public class IncomeEditorActivity extends AppCompatActivity {

    private Button btUpdate;
    private EditText etIncome, etDescription;
    private Spinner spSource;
    private DatePickerDialog.OnDateSetListener myDateSetLister;
    private TextView tvDate, tvSource;
    private String incomeSource, incomeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme
        if (PocketContract.CURRENT_THEME == 1)
            setTheme(R.style.DarkTheme);
        else setTheme(R.style.LightTheme);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_income);

        btUpdate = findViewById(R.id.btSubmit);
        etIncome = findViewById(R.id.etIncome);
        tvSource = findViewById(R.id.tvSource);
        tvDate = findViewById(R.id.tvDate);
        etDescription = findViewById(R.id.etDescription);
        spSource = findViewById(R.id.spSource);

        Intent intent = getIntent();
        Uri uri = intent.getData();
        String[] projection = {
                IncomeEntry.INCOME_ID,
                IncomeEntry.COLUMN_INCOME_AMOUNT,
                IncomeEntry.COLUMN_INCOME_SOURCE,
                IncomeEntry.COLUMN_INCOME_DATE,
                IncomeEntry.COLUMN_INCOME_DESCRIPTION
        };
        Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
        int amountColumnIndex = cursor.getColumnIndex(IncomeEntry.COLUMN_INCOME_AMOUNT);
        int descriptionColumnIndex = cursor.getColumnIndex(IncomeEntry.COLUMN_INCOME_DESCRIPTION);
        if(cursor.moveToNext())
        {
            etIncome.setText(cursor.getString(amountColumnIndex));
            etDescription.setText(cursor.getString(descriptionColumnIndex));
        }

        setupSpinner();

        tvDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(IncomeEditorActivity.this,
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
                incomeDate = dayOfMonth+"/"+month+"/"+year;
                tvDate.setText(incomeDate);
            }
        };


        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String incomeAmount = etIncome.getText().toString().trim();
                String incomeDescription = etDescription.getText().toString().trim();
                if (TextUtils.isEmpty(incomeAmount))
                    etIncome.setError("Enter Income Amount");
                else if (TextUtils.isEmpty(incomeDate))
                    tvDate.setError("Select Date");
                else{
                    ContentValues values = new ContentValues();
                    values.put(IncomeEntry.COLUMN_INCOME_AMOUNT,incomeAmount);
                    values.put(IncomeEntry.COLUMN_INCOME_SOURCE,incomeSource);
                    values.put(IncomeEntry.COLUMN_INCOME_DATE,incomeDate);
                    values.put(IncomeEntry.COLUMN_INCOME_DESCRIPTION,incomeDescription);
                    int newRow = getContentResolver().update(uri,values,null,null);
                    if(newRow!=0)
                        Toast.makeText(IncomeEditorActivity.this,"Update Successful",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(IncomeEditorActivity.this,"Update Fail",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    private void setupSpinner() {
        ArrayAdapter incomeSourceAdapter = ArrayAdapter.createFromResource(IncomeEditorActivity.this, R.array.income_source, R.layout.spinner_item);
        incomeSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSource.setAdapter(incomeSourceAdapter);

        spSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                incomeSource = (String) parent.getItemAtPosition(position);
                //tvSource.setText("Source: "+selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}