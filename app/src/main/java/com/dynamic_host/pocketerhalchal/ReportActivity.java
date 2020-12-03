package com.dynamic_host.pocketerhalchal;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.dynamic_host.pocketerhalchal.database.PocketContract;
import com.dynamic_host.pocketerhalchal.database.PocketContract.IncomeEntry;
import com.dynamic_host.pocketerhalchal.database.PocketContract.ExpenseEntry;
import com.dynamic_host.pocketerhalchal.database.PocketCursorAdapter;
import com.dynamic_host.pocketerhalchal.database.SharedPreference;

public class ReportActivity extends AppCompatActivity{
    CheckBox cbIncome;
    CheckBox cbExpense;
    ListView listView;
    String source, incomeDescription, expenseDescription, item;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Theme
        if (SharedPreference.getThemeValue(this) == 1)
            setTheme(R.style.DarkTheme);
        else setTheme(R.style.LightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__report);
        cbIncome = findViewById(R.id.cbIncome);
        cbExpense = findViewById(R.id.cbExpense);
        listView = findViewById(R.id.list_report);   //activity_catalog->ListView->id

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder ab = new AlertDialog.Builder(ReportActivity.this);
                ab.setTitle("Delete Or Edit Item");

                if(cbIncome.isChecked()){
                    uri = ContentUris.withAppendedId(IncomeEntry.CONTENT_INCOME_URI,id);
                    String[] incomeProjection = {
                            IncomeEntry.COLUMN_INCOME_SOURCE,
                            IncomeEntry.COLUMN_INCOME_DESCRIPTION
                    };
                    Cursor cursor = getContentResolver().query(uri,incomeProjection,null,null,null);
                    int sourceColumnIndex = cursor.getColumnIndex(IncomeEntry.COLUMN_INCOME_SOURCE);
                    int descriptionColumnIndex = cursor.getColumnIndex(IncomeEntry.COLUMN_INCOME_DESCRIPTION);
                    if(cursor.moveToNext())
                    {
                        source = cursor.getString(sourceColumnIndex);
                        incomeDescription = cursor.getString(descriptionColumnIndex);
                    }
                    if (TextUtils.isEmpty(incomeDescription))
                        ab.setMessage("Source: "+source);
                    else
                        ab.setMessage("Source: "+source+"\nDescription: "+incomeDescription);

                }
                else {
                    uri = ContentUris.withAppendedId(ExpenseEntry.CONTENT_EXPENSE_URI,id);
                    String[] expenseProjection = {
                            ExpenseEntry.COLUMN_EXPENSE_ITEM,
                            ExpenseEntry.COLUMN_EXPENSE_DESCRIPTION
                    };
                    Cursor cursor = getContentResolver().query(uri,expenseProjection,null,null,null);
                    int itemColumnIndex = cursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_ITEM);
                    int descriptionColumnIndex = cursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_DESCRIPTION);
                    if(cursor.moveToNext())
                    {
                        item = cursor.getString(itemColumnIndex);
                        expenseDescription = cursor.getString(descriptionColumnIndex);
                    }
                    if (TextUtils.isEmpty(expenseDescription))
                        ab.setMessage("Item: "+item);
                    else
                        ab.setMessage("Item: "+item+"\nDescription: "+expenseDescription);
                }

                //Setting positive "Save" Button
                ab.setPositiveButton("Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                if (cbIncome.isChecked()){
                                    Intent intent = new Intent(ReportActivity.this,IncomeEditorActivity.class);
                                    Uri uri = ContentUris.withAppendedId(IncomeEntry.CONTENT_INCOME_URI,id);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                                else {
                                    Intent intent = new Intent(ReportActivity.this,ExpenseEditorActivity.class);
                                    Uri uri = ContentUris.withAppendedId(ExpenseEntry.CONTENT_EXPENSE_URI,id);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            }
                        });
                //Setting Negative "Cancel" Button
                ab.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getContentResolver().delete(uri,null,null);
                        display();
                    }
                });
                ab.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(cbIncome.isChecked() || cbExpense.isChecked()){
            display();
        }
    }

    private void display() {
        if(cbIncome.isChecked()){
            String[] incomeProjection = {IncomeEntry.INCOME_ID,
                    IncomeEntry.COLUMN_INCOME_DATE,
                    IncomeEntry.COLUMN_INCOME_SOURCE,
                    IncomeEntry.COLUMN_INCOME_AMOUNT};
            Cursor incomeCursor = getContentResolver().query(IncomeEntry.CONTENT_INCOME_URI, incomeProjection, null, null, null);
            //Setup an adapter to create a list in the cursor
            PocketCursorAdapter incomeAdapter = new PocketCursorAdapter(this, incomeCursor, 1);
            listView.setAdapter(incomeAdapter);//Attach the adapter to the ListView
        }
        else if (cbExpense.isChecked()){
            String[] expenseProjection = {ExpenseEntry.EXPENSE_ID,
                    ExpenseEntry.COLUMN_EXPENSE_DATE,
                    ExpenseEntry.COLUMN_EXPENSE_ITEM,
                    ExpenseEntry.COLUMN_EXPENSE_AMOUNT};
            Cursor expenseCursor = getContentResolver().query(ExpenseEntry.CONTENT_EXPENSE_URI, expenseProjection, null, null, null);
            //Setup an adapter to create a list in the cursor
            PocketCursorAdapter ExpenseAdapter = new PocketCursorAdapter(this, expenseCursor,2);
            listView.setAdapter(ExpenseAdapter);//Attach the adapter to the ListView
        }
    }

    public void onCheckBoxClicked(View view) {
        switch (view.getId())
        {
            case R.id.cbIncome:
                cbExpense.setChecked(false);
                display();
                break;
            case R.id.cbExpense:
                cbIncome.setChecked(false);
                display();
                break;
        }
        if (!cbIncome.isChecked() && !cbExpense.isChecked()){
            listView.setAdapter(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu_option,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.deleteAll:
                if(cbIncome.isChecked()){
                    getContentResolver().delete(IncomeEntry.CONTENT_INCOME_URI, null, null);
                    Toast.makeText(ReportActivity.this, "All Incomes Deleted!", Toast.LENGTH_SHORT).show();
                    display();
                    break;
                }
                else if(cbExpense.isChecked()){
                    getContentResolver().delete(ExpenseEntry.CONTENT_EXPENSE_URI, null, null);
                    Toast.makeText(ReportActivity.this, "All Expenses Deleted!", Toast.LENGTH_SHORT).show();
                    display();
                    break;
                }
                else {
                    Toast.makeText(ReportActivity.this,"Select Income/Expense First",Toast.LENGTH_SHORT).show();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}