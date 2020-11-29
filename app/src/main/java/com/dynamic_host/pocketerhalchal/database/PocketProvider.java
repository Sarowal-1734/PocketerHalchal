package com.dynamic_host.pocketerhalchal.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.dynamic_host.pocketerhalchal.database.PocketContract.IncomeEntry;
import com.dynamic_host.pocketerhalchal.database.PocketContract.ExpenseEntry;
import com.dynamic_host.pocketerhalchal.database.PocketContract.SignUpEntry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PocketProvider extends ContentProvider {

    public PocketDbHelper dbHelper;  // Created public object PetDbHelper dbHelper
    //URI object to Content URI with Code & Use NO_MATCH as the input for this case
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(PocketContract.CONTENT_AUTHORITY, PocketContract.PATH_SIGNUP, 10);
        sUriMatcher.addURI(PocketContract.CONTENT_AUTHORITY, PocketContract.PATH_SIGNUP +"/#", 11);
        sUriMatcher.addURI(PocketContract.CONTENT_AUTHORITY, PocketContract.PATH_INCOME, 100);
        sUriMatcher.addURI(PocketContract.CONTENT_AUTHORITY, PocketContract.PATH_INCOME +"/#", 101);
        sUriMatcher.addURI(PocketContract.CONTENT_AUTHORITY, PocketContract.PATH_EXPENSE, 200);
        sUriMatcher.addURI(PocketContract.CONTENT_AUTHORITY, PocketContract.PATH_EXPENSE +"/#", 201);
    }


    @Override
    public boolean onCreate() {
        dbHelper = new PocketDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case 10:
                cursor = db.query(SignUpEntry.SIGNUP_TABLE_NAME, projection, selection, selectionArgs, null,null,sortOrder);
                break;
            case 11:
                selection = SignUpEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(SignUpEntry.SIGNUP_TABLE_NAME, projection, selection, selectionArgs, null,null,sortOrder);
                break;
            case 100:
                cursor = db.query(IncomeEntry.INCOME_TABLE_NAME, projection, selection, selectionArgs, null,null,sortOrder);
                break;
            case 101:
                selection = IncomeEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(IncomeEntry.INCOME_TABLE_NAME, projection, selection, selectionArgs, null,null,sortOrder);
                break;
            case 200:
                cursor = db.query(ExpenseEntry.EXPENSE_TABLE_NAME, projection, selection, selectionArgs, null,null,sortOrder);
                break;
            case 201:
                selection = ExpenseEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ExpenseEntry.EXPENSE_TABLE_NAME, projection, selection, selectionArgs, null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot Query unknown URI"+ uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case 100:
                return insertIncome(uri, values);
            case 200:
                return insertExpense(uri, values);
            case 10:
                return insertSignUp(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for "+ uri);
        }

    }

    private Uri insertSignUp(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Long id = db.insert(SignUpEntry.SIGNUP_TABLE_NAME, null, values);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertIncome(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Long id = db.insert(IncomeEntry.INCOME_TABLE_NAME, null, values);
        return ContentUris.withAppendedId(uri, id);
    }
    private Uri insertExpense(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Long id = db.insert(ExpenseEntry.EXPENSE_TABLE_NAME, null, values);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case 100:
                db.delete(IncomeEntry.INCOME_TABLE_NAME, selection, selectionArgs);
                Toast.makeText(getContext(),"All Incomes Deleted", Toast.LENGTH_SHORT).show();
                break;
            case 101:
                selection = IncomeEntry.INCOME_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                db.delete(IncomeEntry.INCOME_TABLE_NAME, selection, selectionArgs);
                Toast.makeText(getContext(),"Item Deleted", Toast.LENGTH_SHORT).show();
                break;
            case 200:
                db.delete(ExpenseEntry.EXPENSE_TABLE_NAME, selection, selectionArgs);
                Toast.makeText(getContext(),"All Incomes Deleted", Toast.LENGTH_SHORT).show();
                break;
            case 201:
                selection = ExpenseEntry.EXPENSE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                db.delete(ExpenseEntry.EXPENSE_TABLE_NAME, selection, selectionArgs);
                Toast.makeText(getContext(),"Item Deleted", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalArgumentException("Cannot Query unknown URI" + uri);
        }
        return 0;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowUpdate;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case 10:
                rowUpdate = db.update(SignUpEntry.SIGNUP_TABLE_NAME, values, selection, selectionArgs);
                break;
            case 11:
                selection = SignUpEntry.SIGNUP_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowUpdate = db.update(SignUpEntry.SIGNUP_TABLE_NAME, values, selection, selectionArgs);
                break;
            case 100:
                rowUpdate = db.update(IncomeEntry.INCOME_TABLE_NAME, values, selection, selectionArgs);
                break;
            case 101:
                selection = IncomeEntry.INCOME_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowUpdate = db.update(IncomeEntry.INCOME_TABLE_NAME, values, selection, selectionArgs);
                break;
            case 200:
                rowUpdate = db.update(ExpenseEntry.EXPENSE_TABLE_NAME, values, selection, selectionArgs);
                break;
            case 201:
                selection = ExpenseEntry.EXPENSE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowUpdate = db.update(ExpenseEntry.EXPENSE_TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot Query unknown URI" + uri);
        }
        return rowUpdate;
    }
}
