package com.example.vanocniprojekt;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class TaskFullfilment extends AppCompatActivity {

    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_fullfilment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        //Setting the title of the second fragment and the color of the title
        toolbar = findViewById(R.id.toolbar_task);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("New Task");
    }

}