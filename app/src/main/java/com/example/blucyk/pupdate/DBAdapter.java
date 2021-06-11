/*
  Class Name:  DBAdapter.java
  Purpose:     Creates the SQLite database and access methods.

  Original file created by Lianne Wong on 9/28/2015.
  Modified by Bryanna Lucyk on 05/29/2021.
 */

package com.example.blucyk.pupdate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    // Database & Table Names
    static final String DATABASE_NAME = "MyDB";
    static final String DT_SESSIONS = "Sessions";
    static final String DT_ACTIVITIES = "Activities";
    static final String DT_SESSIONS_ACTIVITIES = "Sessions_Activities";

    // Table Keys
    static final String KEY_SESSION_ID = "Id";
    static final String KEY_SESSION_DATE = "Date";
    static final String KEY_SESSION_TIME = "Time";
    static final String KEY_ACTIVITY_ID = "Id";
    static final String KEY_ACTIVITY_NAME = "Name";
    static final String KEY_ACTIVITY_RATING = "Rating";
    static final String KEY_SESSION_ACTIVITY_ID = "Id";
    static final String KEY_FK_SESSION_ID = "Session_Id";
    static final String KEY_FK_ACTIVITY_ID = "Activity_Id";

    static final String TAG = "DBAdapter";

    static final int DATABASE_VERSION = 1;

    static final String[] ACTIVITY_NAMES = {"Bring It", "Come", "Down", "Drop It", "Heel",
                                             "Leave It", "Look at Me", "Quiet", "Roll Over",
                                            "Shake a Paw", "Sit", "Speak", "Stand on Hind Legs",
                                            "Stand Still", "Stay", "Touch"};
    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /*
         * Creates the SQLite Database. Populates the DT_ACTIVITIES table.
         *
         * @param   SQLiteDatabase db
         * @return  void
         */
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL("create table " + DT_SESSIONS
                           + "(" + KEY_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                           + KEY_SESSION_DATE + " STRING,"
                           + KEY_SESSION_TIME + " STRING)");
                db.execSQL("create table " + DT_ACTIVITIES
                           + "(" + KEY_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                           + KEY_ACTIVITY_NAME + " TEXT)");
                db.execSQL("create table " + DT_SESSIONS_ACTIVITIES
                        + "(" + KEY_SESSION_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + KEY_FK_SESSION_ID + " INTEGER, "
                        + KEY_FK_ACTIVITY_ID + " INTEGER, "
                        + KEY_ACTIVITY_RATING + " REAL, "
                        + "FOREIGN KEY (Session_Id) REFERENCES "
                        + DT_SESSIONS + "(" + KEY_SESSION_ID + "),"
                        + "FOREIGN KEY (Activity_id) REFERENCES "
                                + DT_ACTIVITIES + "(" + KEY_ACTIVITY_ID + "))");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // loads activity names into the DT_ACTIVITIES table
            try {
                for (String activityName : ACTIVITY_NAMES) {
                    ContentValues initialValues = new ContentValues();
                    initialValues.put(KEY_ACTIVITY_NAME, activityName);
                    db.insert(DT_ACTIVITIES, null, initialValues);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /*
         * Creates the SQLite Database. Populates the DT_ACTIVITIES table.
         *
         * @param   SQLiteDatabase db, int oldVersion, int newVersion
         * @return  void
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    /*
     * Opens the Database.
     *
     * @param   void
     * @return  DBAdapter
     * @throws  SQLException
     *
     */
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    /*
     * Closes the Database.
     *
     * @param   void
     * @return  void
     */
    public void close()
    {
        DBHelper.close();
    }

    /*
     * Creates a new training session.
     *
     * @param   void
     * @return  long (the session id in the DT_SESSIONS table)
     */
    public long insertSession(String date, String time) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SESSION_DATE, date);
        initialValues.put(KEY_SESSION_TIME, time);
        return db.insert(DT_SESSIONS, null, initialValues);
    }

    /*
     * Adds an activity to an existing session.
     *
     * @param   long sessionId, String name, int rating
     * @return  long (the session id in the DT_SESSIONS_ACTIVITIES table)
     */
    public long addActivityToSession(long sessionId, String name, float rating) {
        // retrieve the activity id from DT_ACTIVITIES
        Cursor cursor = this.db.rawQuery("select " + KEY_ACTIVITY_ID + " from " + DT_ACTIVITIES
                + " where " + KEY_ACTIVITY_NAME + " = '" + name + "'", null);

        cursor.moveToFirst();
        int retrievedActivityId = cursor.getInt(0);

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FK_SESSION_ID, (int) sessionId);
        initialValues.put(KEY_FK_ACTIVITY_ID, retrievedActivityId);
        initialValues.put(KEY_ACTIVITY_RATING, rating);

        cursor.close();

        return db.insert(DT_SESSIONS_ACTIVITIES, null, initialValues);
    }

    /*
     * Retrieves all sessions stored in the DT_SESSIONS table.
     *
     * @param   void
     * @return  cursor storing all records from DT_SESSIONS
     */
    public Cursor getAllSessions() {
        return db.query(DT_SESSIONS, new String[] {KEY_SESSION_ID, KEY_SESSION_DATE,
                KEY_SESSION_TIME}, null, null, null, null, KEY_SESSION_ID + " DESC");
    }

    /*
     * Retrieves the activity names and ratings associated with a specific training session.
     *
     * @param   int sessionId
     * @return  cursor storing the activity names and ratings
     */
    public Cursor getSessionActivities(int sessionId) {
        return this.db.rawQuery(
                "select " + KEY_ACTIVITY_NAME + ", " + KEY_ACTIVITY_RATING
                        + " from " + DT_SESSIONS_ACTIVITIES + " INNER JOIN " + DT_ACTIVITIES
                        + " ON " + DT_ACTIVITIES + "." + KEY_ACTIVITY_ID
                        + " = " + DT_SESSIONS_ACTIVITIES + "." + KEY_FK_ACTIVITY_ID
                        + " where " + KEY_FK_SESSION_ID + " = " + sessionId, null);
    }

    /*
     * Deletes a specific training session from the database.
     *
     * Note: Will first try to delete from the DT_SESSION_ACTIVITIES table.
     *       If this does not work, will not proceed with trying to delete
     *       from the DT_SESSIONS table.
     *
     * @param   int sessionId
     * @return  boolean (true if deleted, false if not)
     */
    public boolean deleteSession(long sessionId) {

        if(deleteSessionActivities(sessionId)) {
            return db.delete(DT_SESSIONS, KEY_SESSION_ID + "=" + sessionId, null) > 0;
        }
        else {
          return false;
        }
    }

    /*
     * Deletes all activities associated with a particular session.
     *
     * @param   int sessionId
     * @return  boolean (true if deleted, false if not)
     */
    public boolean deleteSessionActivities(long sessionId) {
        return db.delete(DT_SESSIONS_ACTIVITIES, KEY_FK_SESSION_ID + "=" + sessionId, null) > 0;
    }

    /*
     * Deletes all training sessions from the database.
     *
     * @param   void
     * @return  int (number of rows deleted)
     */
    public int deleteSessions() {
        deleteAllSessionActivities();

        return db.delete(DT_SESSIONS, "1", null);
    }

    /*
     * Deletes all session activities from the database.
     *
     * @param   void
     * @return  int (number of rows deleted)
     */
    public int deleteAllSessionActivities() {
        return db.delete(DT_SESSIONS_ACTIVITIES, "1", null);
    }

}
