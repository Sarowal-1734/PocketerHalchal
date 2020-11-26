package com.dynamic_host.pocketerhalchal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.dynamic_host.pocketerhalchal.database.PocketContract.IncomeEntry;
import com.dynamic_host.pocketerhalchal.database.PocketContract.ExpenseEntry;
import com.dynamic_host.pocketerhalchal.database.PocketContract.SignUpEntry;

public class PocketDbHelper extends SQLiteOpenHelper {

    public static final int DATADASE_VERSION = 1;
    public static final String DATADASE_NAME = "pocketerHalchal";

    public PocketDbHelper(Context context) {
        super(context, DATADASE_NAME, null, DATADASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_SIGNUP_TABLE = "CREATE TABLE "+ SignUpEntry.SIGNUP_TABLE_NAME +
                " ("+SignUpEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                SignUpEntry.COLUMN_SIGNUP_USERNAME+" TEXT, "+
                SignUpEntry.COLUMN_SIGNUP_EMAIL+" TEXT, "+
                SignUpEntry.COLUMN_SIGNUP_PASSWORD+" TEXT);";
        db.execSQL(SQL_CREATE_SIGNUP_TABLE);

        String SQL_CREATE_INCOME_TABLE = "CREATE TABLE "+ IncomeEntry.INCOME_TABLE_NAME+
                " ("+IncomeEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                IncomeEntry.COLUMN_INCOME_AMOUNT+" TEXT, "+
                IncomeEntry.COLUMN_INCOME_SOURCE+" TEXT, "+
                IncomeEntry.COLUMN_INCOME_DATE+" TEXT, "+
                IncomeEntry.COLUMN_INCOME_DESCRIPTION+" TEXT DEFAULT 0);";
        db.execSQL(SQL_CREATE_INCOME_TABLE);

        String SQL_CREATE_EXPENSE_TABLE = "CREATE TABLE "+ ExpenseEntry.EXPENSE_TABLE_NAME+
                " ("+ExpenseEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ExpenseEntry.COLUMN_EXPENSE_AMOUNT+" TEXT NOT NULL, "+
                ExpenseEntry.COLUMN_EXPENSE_ITEM+" TEXT, "+
                ExpenseEntry.COLUMN_EXPENSE_DATE+" TEXT NOT NULL, "+
                ExpenseEntry.COLUMN_EXPENSE_DESCRIPTION+" TEXT DEFAULT 0);";
        db.execSQL(SQL_CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IncomeEntry.INCOME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ExpenseEntry.EXPENSE_TABLE_NAME);
        onCreate(db);
    }
}

