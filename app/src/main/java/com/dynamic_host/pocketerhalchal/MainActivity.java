package com.dynamic_host.pocketerhalchal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btIncome, btExpense, btReport, btSettings;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pieChart = findViewById(R.id.pieChart);
        btIncome = findViewById(R.id.btIncome);
        btExpense = findViewById(R.id.btExpense);
        btReport = findViewById(R.id.btReport);
        btSettings = findViewById(R.id.btSettings);

        setupPieChart();

        btIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityIncome.class);
                startActivity(intent);
            }
        });

        btExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_expense.class);
                startActivity(intent);
            }
        });

        btReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity_Report.class);
                startActivity(intent);
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setupPieChart() {
        pieChart.animateXY(1000,1000);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleRadius(40);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.setCenterText("Monthly\nReport");
        pieChart.setCenterTextSize(16);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(90);
        pieChart.setTransparentCircleColor(Color.GRAY);
        pieChart.setTransparentCircleRadius(50);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        l.setTextSize(16f);
        l.setTextColor(Color.WHITE);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(25000, "Income"));
        entries.add(new PieEntry(18000, "Expense"));

        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setSliceSpace(0.5f);
        dataSet.setValueTextSize(14f);
        dataSet.setSelectionShift(0f);
        //Add custom color
        int[] colors = new int[] {Color.GREEN, Color.RED};
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();  //refresh
    }
}