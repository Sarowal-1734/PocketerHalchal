package com.dynamic_host.pocketerhalchal;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dynamic_host.pocketerhalchal.database.PocketContract.ExpenseEntry;
import com.dynamic_host.pocketerhalchal.database.PocketContract.IncomeEntry;
import com.dynamic_host.pocketerhalchal.database.PocketCursorAdapter;
import com.dynamic_host.pocketerhalchal.database.SharedPreference;

public class ReportActivity extends AppCompatActivity{
    CheckBox cbIncome;
    CheckBox cbExpense;
    SwipeMenuListView listView;
    String source, incomeDescription, expenseDescription, incomeDate, expenseDate, item;
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
        listView = (SwipeMenuListView) findViewById(R.id.list_report);

        //Creating swipe option on listview item for delete a edit
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // Create edit item
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                editItem.setBackground(new ColorDrawable(Color.rgb(32,98,35)));
                editItem.setWidth(150);
                editItem.setIcon(R.drawable.ic_edit_item);
                menu.addMenuItem(editItem);
                // create delete item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F,25)));
                deleteItem.setWidth(150);
                deleteItem.setIcon(R.drawable.ic_delete_item);
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        //OnClick listner for listview Item in Swipe option
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                int  id = (int) listView.getItemIdAtPosition(position);
                switch (index){
                    case 0:
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
                        break;
                    case 1:
                        AlertDialog.Builder ab = new AlertDialog.Builder(ReportActivity.this);
                        ab.setTitle("Are you sure to delete?");
                        //Setting positive "Ok" Button
                        ab.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {
                                        if(cbIncome.isChecked()){
                                            uri = ContentUris.withAppendedId(IncomeEntry.CONTENT_INCOME_URI,position+1);
                                            getContentResolver().delete(uri,null,null);
                                            display();
                                        }
                                        else {
                                            uri = ContentUris.withAppendedId(ExpenseEntry.CONTENT_EXPENSE_URI,position+1);
                                            getContentResolver().delete(uri,null,null);
                                            display();
                                        }
                                    }
                                });
                        //Setting Negative "Cancel" Button
                        ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();
                            }
                        });
                        ab.show();
                        break;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // To show the description of the item
                AlertDialog.Builder ab = new AlertDialog.Builder(ReportActivity.this);
                ab.setTitle("Item Information");

                if(cbIncome.isChecked()){
                    uri = ContentUris.withAppendedId(IncomeEntry.CONTENT_INCOME_URI,id);
                    String[] incomeProjection = {
                            IncomeEntry.COLUMN_INCOME_SOURCE,
                            IncomeEntry.COLUMN_INCOME_DESCRIPTION,
                            IncomeEntry.COLUMN_INCOME_DATE
                    };
                    Cursor cursor = getContentResolver().query(uri,incomeProjection,null,null,null);
                    if(cursor.moveToNext())
                    {
                        source = cursor.getString(cursor.getColumnIndex(IncomeEntry.COLUMN_INCOME_SOURCE));
                        incomeDescription = cursor.getString(cursor.getColumnIndex(IncomeEntry.COLUMN_INCOME_DESCRIPTION));
                        incomeDate = cursor.getString(cursor.getColumnIndex(IncomeEntry.COLUMN_INCOME_DATE));
                    }
                    if (TextUtils.isEmpty(incomeDescription))
                        ab.setMessage("Source: "+source+"\n\n"+incomeDate);
                    else
                        ab.setMessage("Source: "+source+"\nDescription: "+incomeDescription+"\n\n"+incomeDate);

                }
                else {
                    uri = ContentUris.withAppendedId(ExpenseEntry.CONTENT_EXPENSE_URI,id);
                    String[] expenseProjection = {
                            ExpenseEntry.COLUMN_EXPENSE_ITEM,
                            ExpenseEntry.COLUMN_EXPENSE_DESCRIPTION,
                            ExpenseEntry.COLUMN_EXPENSE_DATE
                    };
                    Cursor cursor = getContentResolver().query(uri,expenseProjection,null,null,null);
                    if(cursor.moveToNext())
                    {
                        item = cursor.getString(cursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_ITEM));
                        expenseDescription = cursor.getString(cursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_DESCRIPTION));
                        expenseDate = cursor.getString(cursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_DATE));
                    }
                    if (TextUtils.isEmpty(expenseDescription))
                        ab.setMessage("Item: "+item+"\n\n"+expenseDate);
                    else
                        ab.setMessage("Item: "+item+"\nDescription: "+expenseDescription+"\n\n"+expenseDate);
                }
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

    // Display Item on List view
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
            listView.setAdapter(new PocketCursorAdapter(this,null,0));
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