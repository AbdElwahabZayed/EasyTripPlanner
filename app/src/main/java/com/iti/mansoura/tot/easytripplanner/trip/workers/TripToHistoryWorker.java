package com.iti.mansoura.tot.easytripplanner.trip.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

public class TripToHistoryWorker extends Worker {

    public TripToHistoryWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String firebaseUID = getInputData().getString("firebaseUID");
        addToHistory(firebaseUID);
        return Result.success();
    }

    private void addToHistory(final String firebaseUID)
    {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Trips").child(firebaseUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Trip mTrip = dataSnapshot.getValue(Trip.class);
                if(mTrip!=null) {
                    mTrip.setStatus(2);
                    reference.child("Trips").child(firebaseUID).removeValue();
                    reference.child("Trips").child(firebaseUID).setValue(mTrip);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("history worker " , "error"+databaseError.getMessage());
            }
        });
    }
}