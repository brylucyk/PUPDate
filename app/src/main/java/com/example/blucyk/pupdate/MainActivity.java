/**
 * Class Name:  MainActivity.java
 * Purpose:     The code behind activity_main.xml
 * Author:      Bryanna Lucyk
 * Date:        June 10, 2021
 */

package com.example.blucyk.pupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if puppy name is not null, then set layout to menu
        SharedPreferences settings = getSharedPreferences("pupdateSharedPrefs",
                                     Context.MODE_PRIVATE);
        String puppyName = settings.getString("puppyName", "your puppy");

        setContentView(R.layout.activity_main);

        // display welcome back message for returning user
        if(!puppyName.equals("your puppy")) {
            TextView welcomeMessage = findViewById(R.id.welcomeMessage);
            welcomeMessage.setText("Is " + puppyName + " ready for training?");
            EditText inputPuppyName = findViewById(R.id.editTextPuppyName);
            inputPuppyName.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if puppy name is not null, then set layout to menu
        SharedPreferences settings = getSharedPreferences("pupdateSharedPrefs",
                                     Context.MODE_PRIVATE);
        String puppyName = settings.getString("puppyName", "your puppy");

        setContentView(R.layout.activity_main);

        if(!puppyName.equals("your puppy")) {
            TextView welcomeMessage = findViewById(R.id.welcomeMessage);
            welcomeMessage.setText("Is " + puppyName + " ready for training?");
            EditText inputPuppyName = findViewById(R.id.editTextPuppyName);
            inputPuppyName.setVisibility(View.GONE);
        }
    }

    /*
     * Retrieves/saves the user's puppy name and starts next activity.
     *
     * @param   View view
     * @return  void
     */
    public void onClickGo(View view) {
        // retrieve and save the puppy name
        EditText inputPuppyName = findViewById(R.id.editTextPuppyName);

        if(inputPuppyName.getVisibility() != View.GONE) {
            setPuppyName();
        }

        // go to new activity
        Intent i = new Intent(this, NavigationActivity.class);
        startActivity(i);
    }

    /*
     * Saves the puppy name the user has entered. If no name has been entered, then
     * "your puppy" is saved by default.
     *
     * @param   void
     * @return  void
     */
    public void setPuppyName() {
        EditText inputPuppyName = findViewById(R.id.editTextPuppyName);
        String puppyName = inputPuppyName.getText().toString();

        if(TextUtils.isEmpty(puppyName)) {
            puppyName = "your puppy";
        }

        SharedPreferences settings = getSharedPreferences("pupdateSharedPrefs",
                                     Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("puppyName", puppyName);
        editor.apply();
    }
}