/*
  Class Name:  RecyclerViewAdapter
  Purpose:     Creates and populates the RecyclerView for Recorded Activities.
  Modified By: Bryanna Lucyk
  Date:        June 10, 2021
 */

package com.example.blucyk.pupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewerAdapter extends RecyclerView.Adapter<RecyclerViewerAdapter.ViewHolder>
{
    ArrayList<TrainingSession> trainingSessionArrayList;
    Context context;
    View recyclerViewLayout;
    ViewHolder recyclerViewHolder;

    public RecyclerViewerAdapter(Context appContext, ArrayList<TrainingSession> sessions){
        context = appContext;
        trainingSessionArrayList = sessions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public Button deleteButton;
        public TextView rVSessionDate;
        public TextView rVSessionTime;
        public TextView activity1;
        public TextView activity2;
        public TextView activity3;
        public RatingBar ratingBarActivity1;
        public RatingBar ratingBarActivity2;
        public RatingBar ratingBarActivity3;

        public ViewHolder(View v){
            super(v);
            deleteButton = (Button)v.findViewById(R.id.deleteButton);
            rVSessionTime = (TextView)v.findViewById(R.id.rVSessionTime);
            rVSessionDate = (TextView)v.findViewById(R.id.rVSessionDate);
            activity1 = (TextView)v.findViewById(R.id.activity1);
            activity2 = (TextView)v.findViewById(R.id.activity2);
            activity3 = (TextView)v.findViewById(R.id.activity3);
            ratingBarActivity1 = (RatingBar)v.findViewById(R.id.ratingBarActivity1);
            ratingBarActivity2 = (RatingBar)v.findViewById(R.id.ratingBarActivity2);
            ratingBarActivity3 = (RatingBar)v.findViewById(R.id.ratingBarActivity3);
        }
    }

    @Override
    public RecyclerViewerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        recyclerViewLayout = LayoutInflater.from(context).inflate(R.layout.recyclerview_layout,parent,false);
        recyclerViewHolder = new ViewHolder(recyclerViewLayout);
        recyclerViewHolder.itemView.getLayoutParams().height=800;
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.rVSessionDate.setText(trainingSessionArrayList.get(position).getDate());
        holder.rVSessionTime.setText(trainingSessionArrayList.get(position).getTime());
        holder.activity1.setText(
                trainingSessionArrayList.get(position).getActivities().get(0).getActivityName());
        holder.activity2.setText(
                trainingSessionArrayList.get(position).getActivities().get(1).getActivityName());
        holder.activity3.setText(
                trainingSessionArrayList.get(position).getActivities().get(2).getActivityName());
        holder.ratingBarActivity1.setRating(
                trainingSessionArrayList.get(position).getActivities().get(0).getActivityRating());
        holder.ratingBarActivity1.setEnabled(false);
        holder.ratingBarActivity2.setRating(
                trainingSessionArrayList.get(position).getActivities().get(1).getActivityRating());
        holder.ratingBarActivity2.setEnabled(false);
        holder.ratingBarActivity3.setRating(
                trainingSessionArrayList.get(position).getActivities().get(2).getActivityRating());
        holder.ratingBarActivity3.setEnabled(false);
    }

    @Override
    public int getItemCount(){
        if(!trainingSessionArrayList.isEmpty()) {
            return trainingSessionArrayList.size();
        }
        else {
            return 0;
        }
    }
}

