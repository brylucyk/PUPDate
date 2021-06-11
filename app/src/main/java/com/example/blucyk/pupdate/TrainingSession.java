/*
  Class Name:  TrainingActivity.java
  Purpose:     Class for TrainingSession objects containing the user's training session
               information recorded in the database.
  Author:      Bryanna Lucyk
  Date:        June 10, 2021
 */

package com.example.blucyk.pupdate;

import java.util.ArrayList;

public class TrainingSession {
    private int id;
    private String date;
    private String time;
    private ArrayList<TrainingActivity> activities;

    public TrainingSession(int id, String date, String time) {
        this.id = id;
        this.date = date;
        this.time = time;

        activities = new ArrayList<TrainingActivity>();
    }

    public int getId() {
        return this.id;
    }
    public String getDate() { return this.date; }
    public String getTime() { return this.time; }

    /*
     * Stores the activities completed during the training session.
     *
     * @param   TimePicker view, int hourOfDay, int minute
     * @return  void
     */
    public void addTrainingActivity(TrainingActivity activity) {
        this.activities.add(activity);
    }

    public ArrayList<TrainingActivity> getActivities() {
        return this.activities;
    }
}
