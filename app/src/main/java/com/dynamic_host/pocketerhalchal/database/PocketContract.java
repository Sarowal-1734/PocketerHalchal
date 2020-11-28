package com.dynamic_host.pocketerhalchal.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class PocketContract {

    public static final String CONTENT_AUTHORITY = "com.dynamic_host.pocketerhalchal";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SIGNUP = "sign_up_table";
    public static final String PATH_INCOME = "income_table";
    public static final String PATH_EXPENSE = "expense_table";
    public static int CURSOR_POSITION = -1;

    public PocketContract() {}

    public static final class SignUpEntry implements BaseColumns {
        public static final Uri CONTENT_SIGNUP_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SIGNUP);

        public final static String SIGNUP_TABLE_NAME = "sign_up_table";
        public final static String SIGNUP_ID = BaseColumns._ID;
        public final static String COLUMN_SIGNUP_USERNAME = "User_Name";
        public final static String COLUMN_SIGNUP_EMAIL = "User_Email";
        public final static String COLUMN_SIGNUP_PASSWORD = "User_Password";
    }

    public static final class IncomeEntry implements BaseColumns {
        public static final Uri CONTENT_INCOME_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INCOME);

        public final static String INCOME_TABLE_NAME = "income_table";
        public final static String INCOME_ID = BaseColumns._ID;
        public final static String COLUMN_INCOME_AMOUNT = "Income_Amount";
        public final static String COLUMN_INCOME_SOURCE = "Income_Source";
        public final static String COLUMN_INCOME_DATE = "Income_Date";
        public final static String COLUMN_INCOME_DESCRIPTION = "Income_Description";
    }
    public static final class ExpenseEntry implements BaseColumns {

        public static final Uri CONTENT_EXPENSE_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXPENSE);

        public final static String EXPENSE_TABLE_NAME = "expense_table";
        public final static String EXPENSE_ID = BaseColumns._ID;
        public final static String COLUMN_EXPENSE_AMOUNT = "Expense_Amount";
        public final static String COLUMN_EXPENSE_ITEM = "Expense_Item";
        public final static String COLUMN_EXPENSE_DATE = "Expense_Date";
        public final static String COLUMN_EXPENSE_DESCRIPTION = "Expense_Description";
    }
}
