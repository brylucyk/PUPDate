package com.example.blucyk.pupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionActivity extends AppCompatActivity {

    private DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // retrieve and display puppy name
        SharedPreferences settings = getSharedPreferences("pupdateSharedPrefs", Context.MODE_PRIVATE);
        String puppyName = settings.getString("puppyName", "your puppy");

        TextView h2RatingPrompt = findViewById(R.id.h2Rating);
        h2RatingPrompt.setText("How did " + puppyName + " do?");
//
        TextView sessionDate = findViewById(R.id.editTextDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = dateFormat.format(new Date());
        sessionDate.setText(currentDate);

        TextView sessionTime = findViewById(R.id.editTextTime);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");
        String currentTime = timeFormat.format(new Date());
        sessionTime.setText(currentTime);
    }

    public void onClickCancel(View view) {
        finish();
    }

}