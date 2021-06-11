/*
  Class Name:  SessionActivity.java
  Purpose:     The code behind activity_session.xml
  Author:      Bryanna Lucyk
  Date:        June 10, 2021
 */

package com.example.blucyk.pupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SessionActivity extends AppCompatActivity{

    private DBAdapter db;
    Calendar c;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        Button chooseDate = findViewById(R.id.btnChooseDate);
        TextView sessionDate = findViewById(R.id.textSessionDate);

        // set up the date picker
        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(SessionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        sessionDate.setText(mYear + "/" + (mMonth + 1)+ "/" + mDay);
                    }
                }, day, month, year);
                dpd.updateDate(year, month, day);
                dpd.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // retrieve and display puppy name
        SharedPreferences settings = getSharedPreferences("pupdateSharedPrefs", Context.MODE_PRIVATE);
        String puppyName = settings.getString("puppyName", "your puppy");

        TextView h2RatingPrompt = findViewById(R.id.h2Rating);
        h2RatingPrompt.setText("How did " + puppyName + " do?");

        // default session to current date and time
        TextView sessionDate = findViewById(R.id.textSessionDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = dateFormat.format(new Date());
        sessionDate.setText(currentDate);

        TextView sessionTime = findViewById(R.id.textSessionTime);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");
        String currentTime = timeFormat.format(new Date());
        sessionTime.setText(currentTime);
    }

    public void onSubmit(View view) {
        // pull the date
        TextView sessionDate = findViewById(R.id.textSessionDate);
        String date = String.valueOf(sessionDate.getText());
        TextView sessionTime = findViewById(R.id.textSessionTime);
        String time = String.valueOf(sessionTime.getText());

        // for each activity, get the activity and the rating
        Spinner spinnerActivityOne = findViewById(R.id.activityOne);
        String activityOne = spinnerActivityOne.getSelectedItem().toString();
        RatingBar ratingBarActivityOne = findViewById(R.id.ratingBarActivityOne);
        float activityOneRating = ratingBarActivityOne.getRating();

        Spinner spinnerActivityTwo = findViewById(R.id.activityTwo);
        String activityTwo = spinnerActivityTwo.getSelectedItem().toString();
        RatingBar ratingBarActivityTwo = findViewById(R.id.ratingBarActivityTwo);
        float activityTwoRating = ratingBarActivityTwo.getRating();

        Spinner spinnerActivityThree = findViewById(R.id.activityThree);
        String activityThree = spinnerActivityThree.getSelectedItem().toString();
        RatingBar ratingBarActivityThree = findViewById(R.id.ratingBarActivityThree);
        float activityThreeRating = ratingBarActivityThree.getRating();

        // store each activity in the database
        db = new DBAdapter(this);
        db.open();
        // first create session, return the session id
        long sessionID = db.insertSession(date, time);
        db.addActivityToSession(sessionID, activityOne, activityOneRating);
        db.addActivityToSession(sessionID, activityTwo, activityTwoRating);
        db.addActivityToSession(sessionID, activityThree, activityThreeRating);
        db.close();
        // alert user that session has been recorded
        Toast toast = Toast.makeText(getApplicationContext(),
                  "Session added!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL
                              | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public void onClickCancel(View view) {
        finish();
    }

    // time picker
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        /*
         * Creates a new instance of TimePickerDialog and returns it. Uses the current time as
         * the default values for the picker.
         *
         * @param   Bundle savedInstanceState
         * @return  Dialog TimePickerDialog
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        /*
         * Formats and displays the chosen date/time.
         *
         * @param   TimePicker view, int hourOfDay, int minute
         * @return  void
         */
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            TextView sessionTime = getActivity().findViewById(R.id.textSessionTime);
            String chosenTime;

            int convertedHour;

            if(hourOfDay == 0) {
                convertedHour = 12;
            }
            else if(hourOfDay > 12) {
                convertedHour = (hourOfDay - 12);
            }
            else {
                convertedHour = hourOfDay;
            }

            if(minute < 10) {
                chosenTime = convertedHour + ":0" + minute;
            }
            else {
                chosenTime = convertedHour + ":" + minute;
            }

            if(hourOfDay >= 12) {
                chosenTime += " p.m.";
            }
            else {
                chosenTime += " a.m.";
            }
            sessionTime.setText(chosenTime);
        }
    }

    /*
     * Opens the TimePickerDialog fragment.
     *
     * @param   View v
     * @return  void
     */
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}