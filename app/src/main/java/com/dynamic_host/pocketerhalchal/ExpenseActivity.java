package com.dynamic_host.pocketerhalchal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.dynamic_host.pocketerhalchal.database.PocketContract.ExpenseEntry;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamic_host.pocketerhalchal.database.PocketContract;
import com.dynamic_host.pocketerhalchal.database.SharedPreference;

import java.util.Calendar;

public class ExpenseActivity extends AppCompatActivity {

    private Button btSubmit;
    private EditText etExpense, etDescription;
    private Spinner spExpenseItem;
    private TextView tvDate;
    private String expenseItem, expenseDate;
    private DatePickerDialog.OnDateSetListener myDateSetLister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme
        if (SharedPreference.getThemeValue(this) == 1)
            setTheme(R.style.DarkTheme);
        else setTheme(R.style.LightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        btSubmit = findViewById(R.id.btSubmit);
        etExpense = findViewById(R.id.etExpense);
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
                expenseDate = dayOfMonth+"/"+month+"/"+year;
                tvDate.setText(expenseDate);
            }
        };



        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expenseAmount = etExpense.getText().toString().trim();
                String expenseDescription = etDescription.getText().toString().trim();
                if (TextUtils.isEmpty(expenseAmount))
                    etExpense.setError("Enter Expense Amount");
                else if (TextUtils.isEmpty(expenseDate))
                    tvDate.setError("Select Date");
                else{
                    ContentValues values = new ContentValues();
                    values.put(ExpenseEntry.COLUMN_EXPENSE_AMOUNT,expenseAmount);
                    values.put(ExpenseEntry.COLUMN_EXPENSE_ITEM,expenseItem);
                    values.put(ExpenseEntry.COLUMN_EXPENSE_DATE,expenseDate);
                    values.put(ExpenseEntry.COLUMN_EXPENSE_DESCRIPTION,expenseDescription);
                    Uri newUri = getContentResolver().insert(ExpenseEntry.CONTENT_EXPENSE_URI,values);
                    if (newUri == null)
                        Toast.makeText(ExpenseActivity.this, "Error with saving data!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(ExpenseActivity.this, "New Expense Data Saved!", Toast.LENGTH_SHORT).show();
                    finish();
                }
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
                expenseItem = (String) parent.getItemAtPosition(position);
                //tvSource.setText("Source: "+selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}