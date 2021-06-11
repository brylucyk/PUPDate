/**
 * Class Name:  HistoryActivity.java
 * Purpose:     The code behind activity_history.xml
 * Author:      Bryanna Lucyk
 * Date:        June 10, 2021
 */

package com.example.blucyk.pupdate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<TrainingSession> trainingSessionArrayList;

    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewManager;

    private DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById( R.id.recyclerView );
        recyclerViewManager = new LinearLayoutManager( getApplicationContext() );
        recyclerView.setLayoutManager( recyclerViewManager );
        recyclerView.setHasFixedSize( true );

        trainingSessionArrayList = new ArrayList<TrainingSession>();

        // load the sessions into the array list
        db = new DBAdapter(this);

        // get the existing database file or from the assets folder if it doesn't exist
        try {
            String destPath = "data/data/"+getPackageName()+"/databases";
            File f = new File(destPath);
            if(!f.exists()){
                f.mkdirs();
                CopyDB(getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream(destPath + "/MyDB"));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        db.open();
        Cursor cSessions = db.getAllSessions();

        if(cSessions.moveToFirst())
        {

            do{
                // create objects for the sessions stored in the database
                TrainingSession session = new TrainingSession(cSessions.getInt(0),
                        cSessions.getString(1),
                        cSessions.getString(2));

                Cursor cSessionActivities = db.getSessionActivities(cSessions.getInt(0));

                if(cSessionActivities.moveToFirst()) {
                    // add the activities to the sessions
                    do {
                        TrainingActivity activity = new TrainingActivity(
                                cSessionActivities.getString(0),
                                cSessionActivities.getFloat(1));

                        session.addTrainingActivity(activity);
                    } while(cSessionActivities.moveToNext());
                }

                trainingSessionArrayList.add(session);

            } while(cSessions.moveToNext());
        }
        db.close();

        // enable/disable the button to clear all sessions
        Button clearButton = findViewById(R.id.buttonClearAll);

        if(trainingSessionArrayList.isEmpty()) {
            clearButton.setEnabled(false);
            clearButton.setText(getResources().getString(R.string.btn_no_sessions));
        }
        else {
            clearButton.setEnabled(true);
            clearButton.setText(String.format(getResources().getString(R.string.btn_clear_sessions),
                    String.valueOf(trainingSessionArrayList.size())));
        }

        recyclerViewAdapter = new RecyclerViewerAdapter( HistoryActivity.this, trainingSessionArrayList);
        recyclerView.setAdapter( recyclerViewAdapter );
    }

    /*
     * Copies the existing database file.
     *
     * @author  Lianne Wong
     * @param   InputStream inputStream, OutputStream outputStream
     * @return  void
     */
    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        //Copy one byte at a time
        byte [] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();
    }

    /*
     * The callback function for when the clicks the button to clear all sessions.
     * Prompts the user to confirm choice.
     *
     * @param   InputStream inputStream, OutputStream outputStream
     * @return  void
     */
    public void clearSessions(View view) {
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

    /*
     * Deletes all training sessions from the database.
     *
     * @param   void
     * @return  void
     */
    public void deleteSessions() {

        db.open();
        if(db.deleteSessions() > 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Sessions cleared", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            // redirect back to navigation
            finish();
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Sorry, sessions were not deleted!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        db.close();
    }

    /*
     * Creates an alert dialog box asking the user to confirm that they wish to delete all sessions.
     *
     * @param   void
     * @return  AlertDialog
     */
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Clear Sessions")
                .setMessage("Your session history will be deleted permanently. " +
                        "Are you sure you want to delete all sessions?")
                .setIcon(R.drawable.ic_delete_forever_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteSessions();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    /*
     * Deletes an individual training session.
     *
     * @param   View view
     * @return  void
     */
    public void deleteSession(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Session deleted!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        // refresh page?
    }
}