package com.example.blucyk.pupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void setPuppyName(View view) {
        EditText inputPuppyName = findViewById(R.id.editTextChangePuppyName);
        String puppyName = inputPuppyName.getText().toString();

        if(TextUtils.isEmpty(puppyName)) {
            puppyName = "your puppy";
        }

        SharedPreferences settings = getSharedPreferences("pupdateSharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("puppyName", puppyName);
        editor.apply();

      String newName = settings.getString("puppyName", "your puppy");
      if(newName == puppyName) {
          // alert success
          Toast toast = Toast.makeText(getApplicationContext(), "Name updated!", Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
          toast.show();
      }
    }


}