/**
 * Class Name:  SettingsActivity.java
 * Purpose:     The code behind activity_setting.xml
 * Author:      Bryanna Lucyk
 * Date:        June 10, 2021
 */

package com.example.blucyk.pupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    Button btnSetAlarm;
    Button btnPickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnSetAlarm = findViewById(R.id.btnSetAlarm);
        btnPickTime = findViewById(R.id.btnPickTime);

        SharedPreferences settings = getSharedPreferences("pupdateSharedPrefs",
                                     Context.MODE_PRIVATE);
        String alarmTime = settings.getString("alarmTime", "00:00");
        TextView displayChosenTime = findViewById(R.id.settingsChosenTime);
        displayChosenTime.setText(alarmTime);

        // disable "set alarm" button if no time has been selected
        if(displayChosenTime.getText().equals("00:00")) {
            btnSetAlarm.setEnabled(false);
            btnSetAlarm.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                                            R.color.light_grey));
        }

        // auto-fill the puppy name field
        String newName = settings.getString("puppyName", "your puppy");
        EditText inputPuppyName = findViewById(R.id.editTextChangePuppyName);
        inputPuppyName.setText(newName);
    }

    /*
     * Saves the puppy name the user has entered. If no name has been entered, then "your puppy"
     * is saved by default. Notifies the user via Toast upon success.
     *
     * @param   View view
     * @return  void
     */
    public void setPuppyName(View view) {
        EditText inputPuppyName = findViewById(R.id.editTextChangePuppyName);
        String puppyName = inputPuppyName.getText().toString();

        if(TextUtils.isEmpty(puppyName)) {
            puppyName = "your puppy";
        }

        SharedPreferences settings = getSharedPreferences("pupdateSharedPrefs",
                                     Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("puppyName", puppyName);
        editor.apply();

        String newName = settings.getString("puppyName", "your puppy");
        if(newName == puppyName) {
            // alert success
            Toast toast = Toast.makeText(getApplicationContext(), "Name updated!",
                                                                        Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL |
                                    Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    // --- schedule future training session
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        /*
         * Creates the time picker dialog for the alarm.
         *
         * @param   Bundle savedInstanceState
         * @return  Dialog TimePickerDialog
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and returns it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        /*
         * Sets an alarm on the user's phone for the time that was selected. Also displays the
         * time in the Textview.
         *
         * @param   Timepicker view, int hourOfDay, int minute
         * @return  void
         */
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String chosenTime;
            if(minute < 10) {
                chosenTime = hourOfDay + ":0" + minute;
            }
            else {
                chosenTime = hourOfDay + ":" + minute;
            }
            TextView displayChosenTime = getActivity().findViewById(R.id.settingsChosenTime);
            displayChosenTime.setText(chosenTime);

            Button btnSetAlarm = getActivity().findViewById(R.id.btnSetAlarm);
            btnSetAlarm.setEnabled(true);
            btnSetAlarm.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }
    }

    /*
     * Displays the TimePickerDialog fragment when the user is selecting an alarm time.
     *
     * @param   View v
     * @return  void
     */
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /*
     * The callback function triggered when the user clicks the "set alarm" button.
     * Saves the time selected by the user.
     * Calls the createAlarm() function, passing the time and alarm message.
     *
     * @param   View view
     * @return  void
     */
    public void setAlarm(View view) {
        SharedPreferences settings = getSharedPreferences("pupdateSharedPrefs",
                                    Context.MODE_PRIVATE);
        String puppyName = settings.getString("puppyName", "your puppy");
        String alarmMessage = "Time to train " + puppyName;

        TextView displayChosenTime = findViewById(R.id.settingsChosenTime);
        String chosenTime = (String) displayChosenTime.getText();
        String[] split = chosenTime.split(":");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("alarmTime", chosenTime);
        editor.apply();

        createAlarm(alarmMessage, hour, minute);
    }

    /*
     * Creates/starts the intent to set the alarm for the training session.
     *
     * @param   String message (purpose of alarm), and selected time as int hour, int minutes
     * @return  void
     */
    public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}