package com.example.blucyk.pupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
    }

    public void onClickRecord(View view) {
        Intent sessionIntent = new Intent(this, SessionActivity.class);
        startActivity(sessionIntent);
    }

    public void onClickInstructions(View view) {
        Intent sessionIntent = new Intent(this, InstructionsActivity.class);
        startActivity(sessionIntent);
    }

    public void onClickSettings(View view) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    public void onClickHistory(View view) {
    }
}