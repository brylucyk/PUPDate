/**
 * Class Name:  InstructionsActivity.java
 * Purpose:     The code behind activity_instructions.xml
 * Author:      Bryanna Lucyk
 * Date:        June 10, 2021
 */

package com.example.blucyk.pupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
    }

    /*
     * The callback function for when the user clicks the button to return to the Navigation.
     *
     * @param   View view
     * @return  void
     */
    public void onClickNavigate(View view) {
        finish();
    }
}