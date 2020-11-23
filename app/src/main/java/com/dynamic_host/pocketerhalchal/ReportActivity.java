package com.dynamic_host.pocketerhalchal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class ReportActivity extends AppCompatActivity {
    CheckBox cbIncome;
    CheckBox cbExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__report);
        cbIncome = findViewById(R.id.cbIncome);
        cbExpense = findViewById(R.id.cbExpense);

    }

    public void onCheckBoxClicked(View view) {
        switch (view.getId())
        {
            case R.id.cbIncome:
                cbExpense.setChecked(false);
                break;
            case R.id.cbExpense:
                cbIncome.setChecked(false);
                break;
        }

    }
}