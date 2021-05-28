package com.example.blucyk.pupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if puppy name is not null, then set layout to menu

        setContentView(R.layout.activity_main);
    }

    public void onClickGo(View view) {
        // get and save the puppy name

        // go to new activity
        Intent i = new Intent(this, NavigationActivity.class);
        startActivity(i);
    }
}