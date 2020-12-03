package com.dynamic_host.pocketerhalchal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.dynamic_host.pocketerhalchal.database.PocketContract.ExpenseEntry;
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

public class ExpenseEditorActivity extends AppCompatActivity {
    private Button btUpdate;
    private EditText etExpense, etDescription;
    private Spinner spExpenseItem;
    private DatePickerDialog.OnDateSetListener myDateSetLister;
    private TextView tvDate;
    private String expenseItem, expenseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme
        if (SharedPreference.getThemeValue(this) == 1)
            setTheme(R.style.DarkTheme);
        else setTheme(R.style.LightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_editor);

        btUpdate = findViewById(R.id.btSubmit);
        etExpense = findViewById(R.id.etExpense);
        tvDate = findViewById(R.id.tvDate);
        etDescription = findViewById(R.id.etDescription);
        spExpenseItem = findViewById(R.id.spExpenseItem);

        Intent intent = getIntent();
        Uri uri = intent.getData();
        String[] projection = {
                ExpenseEntry.EXPENSE_ID,
                ExpenseEntry.COLUMN_EXPENSE_AMOUNT,
                ExpenseEntry.COLUMN_EXPENSE_ITEM,
                ExpenseEntry.COLUMN_EXPENSE_DATE,
                ExpenseEntry.COLUMN_EXPENSE_DESCRIPTION
        };
        Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
        int amountColumnIndex = cursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_AMOUNT);
        int descriptionColumnIndex = cursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_DESCRIPTION);
        if(cursor.moveToNext())
        {
            etExpense.setText(cursor.getString(amountColumnIndex));
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
                DatePickerDialog dialog = new DatePickerDialog(ExpenseEditorActivity.this,
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


        btUpdate.setOnClickListener(new View.OnClickListener() {
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
                    int newRow = getContentResolver().update(uri,values,null,null);
                    if(newRow!=0)
                        Toast.makeText(ExpenseEditorActivity.this,"Update Successful",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(ExpenseEditorActivity.this,"Update Fail",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    private void setupSpinner() {
        ArrayAdapter expenseItemAdapter = ArrayAdapter.createFromResource(ExpenseEditorActivity.this, R.array.expense_item, R.layout.spinner_item);
        expenseItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExpenseItem.setAdapter(expenseItemAdapter);

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