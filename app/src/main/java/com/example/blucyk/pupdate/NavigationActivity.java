/**
 * Class Name:  NavigationActivity.java
 * Purpose:     The code behind activity_navigation.xml
 * Author:      Bryanna Lucyk
 * Date:        June 10, 2021
 */

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

    /*
     * The callback function for when user clicks "Instructions/How to Use".
     *
     * @param   View view
     * @return  void
     */
    public void onClickInstructions(View view) {
        Intent sessionIntent = new Intent(this, InstructionsActivity.class);
        startActivity(sessionIntent);
    }

    /*
     * The callback function for when user clicks "Configure Settings".
     *
     * @param   View view
     * @return  void
     */
    public void onClickSettings(View view) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    /*
     * The callback function for when user clicks "Record Training Session".
     *
     * @param   View view
     * @return  void
     */
    public void onClickRecord(View view) {
        Intent sessionIntent = new Intent(this, SessionActivity.class);
        startActivity(sessionIntent);
    }

    /*
     * The callback function for when user clicks "View Training Progress".
     *
     * @param   View view
     * @return  void
     */
    public void onClickHistory(View view) {
        Intent settingsIntent = new Intent(this, HistoryActivity.class);
        startActivity(settingsIntent);
    }
}