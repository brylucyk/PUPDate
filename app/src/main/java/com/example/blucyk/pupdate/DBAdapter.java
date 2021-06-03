/**
 * Class Name:  DBAdapter.java
 * Purpose:     Creates the SQLite database and access methods.
 *
 * Original file created by Lianne Wong on 9/28/2015.
 * Modified by Bryanna Lucyk on 05/29/2021.
 */

package com.example.blucyk.pupdate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBAdapter {

    // Database & Table Names
    static final String DATABASE_NAME = "MyDB";
    static final String DT_SESSIONS = "Sessions";
    static final String DT_ACTIVITIES = "Activities";
    static final String DT_SESSIONS_ACTIVITIES = "Sessions_Activities";

    // Table Keys
    static final String KEY_SESSION_ID = "Id";
    static final String KEY_SESSION_DATE = "Date";
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
                           + KEY_SESSION_DATE + " STRING)");
                db.execSQL("create table " + DT_ACTIVITIES
                           + "(" + KEY_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                           + KEY_ACTIVITY_NAME + " TEXT)");
                db.execSQL("create table " + DT_SESSIONS_ACTIVITIES
                        + "(" + KEY_SESSION_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + KEY_FK_SESSION_ID + " INTEGER, "
                        + KEY_FK_ACTIVITY_ID + " INTEGER, "
                        + KEY_ACTIVITY_RATING + " INTEGER, "
                        + "FOREIGN KEY (Session_Id) REFERENCES "
                        + DT_SESSIONS + "(" + KEY_SESSION_ID + "),"
                        + "FOREIGN KEY (Activity_id) REFERENCES "
                                + DT_ACTIVITIES + "(" + KEY_ACTIVITY_ID + "))");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // loads activity names into the DT_ACTIVITIES table
            try {
                for(int i = 0; i < ACTIVITY_NAMES.length; i++) {
                    ContentValues initialValues = new ContentValues();
                    initialValues.put(KEY_ACTIVITY_NAME, ACTIVITY_NAMES[i]);
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
     * @return  void
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
    public long insertSession() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SESSION_DATE, currentDateandTime);
        return db.insert(DT_SESSIONS, null, initialValues);
    }

    /*
     * Adds an activity to an existing session.
     *
     * @param   long sessionId, String name, int rating
     * @return  long (the session id in the DT_SESSIONS_ACTIVITIES table)
     */
    public long addActivityToSession(long sessionId, String name, int rating) {
        // retrieve the activity id from DT_ACTIVITIES
        Cursor cursor = this.db.rawQuery("select " + KEY_ACTIVITY_ID + " from " + DT_ACTIVITIES
                + " where " + KEY_ACTIVITY_NAME + "='Sit'", null);

        cursor.moveToFirst();
        int retrievedActivityId = cursor.getInt(0);

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FK_SESSION_ID, (int) sessionId);
        initialValues.put(KEY_FK_ACTIVITY_ID, retrievedActivityId);
        initialValues.put(KEY_ACTIVITY_RATING, rating);

        return db.insert(DT_SESSIONS_ACTIVITIES, null, initialValues);
    }

    // 


    /*
     * Retrieves the name of an Activity from the DT_ACTIVITIES table.
     *
     * @param   int id
     * @return  String (activity name)
     */
    public String getActivityName(int id) {
        Cursor cursor = this.db.rawQuery("select " + KEY_ACTIVITY_NAME + " from " + DT_ACTIVITIES
                + " where " + KEY_ACTIVITY_ID + "=" + id, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    /*
     * Retrieves the rating of an Activity from the DT_SESSION_ACTIVITIES table.
     *
     * @param   int id
     * @return  int (activity rating)
     */
    public int getActivityRating(int id) {
        Cursor cursor = this.db.rawQuery("select " + KEY_ACTIVITY_RATING + " from " + DT_ACTIVITIES
                + " where " + KEY_ACTIVITY_ID + "=" + id, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

//
//    //---deletes a particular contact---
//    public boolean deleteContact(long rowId)
//    {
//        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
//    }
//
//    //---retrieves all the contacts---
//    public Cursor getAllContacts()
//    {
//        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
//                KEY_EMAIL, KEY_AGE}, null, null, null, null, null);
//    }
//
//    //---retrieves a particular contact---
//    public Cursor getContact(long rowId) throws SQLException
//    {
//        Cursor mCursor =
//                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
//                                KEY_NAME, KEY_EMAIL,KEY_AGE}, KEY_ROWID + "=" + rowId, null,
//                        null, null, null, null);
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//        return mCursor;
//    }
//
//    //---updates a contact---
//    public boolean updateContact(long rowId, String name, String email,int age)
//    {
//        ContentValues args = new ContentValues();
//        args.put(KEY_NAME, name);
//        args.put(KEY_EMAIL, email);
//        args.put(KEY_AGE, age);
//        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
//    }

}