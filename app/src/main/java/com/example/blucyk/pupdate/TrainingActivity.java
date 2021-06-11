/**
 * Class Name:  TrainingActivity.java
 * Purpose:     Class for TrainingActivity objects containing the information for the training
 *              activities recorded in the database.
 * Author:      Bryanna Lucyk
 * Date:        June 10, 2021
 */

package com.example.blucyk.pupdate;

public class TrainingActivity {
    private String activityName;
    private float activityRating;

    public TrainingActivity(String activityName, float activityRating) {
        this.activityName = activityName;
        this.activityRating = activityRating;
    }

    public String getActivityName() { return this.activityName; }
    public float getActivityRating(){ return this.activityRating; }
}
