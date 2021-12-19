package com.dynamic_host.pocketerhalchal.database;
import com.dynamic_host.pocketerhalchal.database.PocketContract.IncomeEntry;
import com.dynamic_host.pocketerhalchal.database.PocketContract.ExpenseEntry;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.dynamic_host.pocketerhalchal.R;

public class PocketCursorAdapter extends CursorAdapter {

    int j;

    public PocketCursorAdapter(Context context, Cursor c, int i) {
        super(context, c, 0);
        j=i;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvDate = (TextView) view.findViewById(R.id.list_date);
        TextView tvSource = (TextView) view.findViewById(R.id.list_sourceItem);
        TextView tvAmount = (TextView) view.findViewById(R.id.list_amount);

        if(j==1){
            int incomeDateColumnIndex = cursor.getColumnIndex(IncomeEntry.COLUMN_INCOME_DATE);
            int sourceColumnIndex = cursor.getColumnIndex(IncomeEntry.COLUMN_INCOME_SOURCE);
            int incomeAmountColumnIndex = cursor.getColumnIndex(IncomeEntry.COLUMN_INCOME_AMOUNT);
            tvDate.setText(cursor.getString(incomeDateColumnIndex));
            tvSource.setText(cursor.getString(sourceColumnIndex));
            tvAmount.setText(cursor.getString(incomeAmountColumnIndex));
        }
        else {
            int expenseDateColumnIndex = cursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_DATE);
            int itemColumnIndex = cursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_ITEM);
            int expenseAmountColumnIndex = cursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_AMOUNT);
            tvDate.setText(cursor.getString(expenseDateColumnIndex));
            tvSource.setText(cursor.getString(itemColumnIndex));
            tvAmount.setText(cursor.getString(expenseAmountColumnIndex));
        }
    }
}
