package com.iti.mansoura.tot.easytripplanner.trip.add;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

import java.util.UUID;

public class AddTripPresenter implements AddTripContract.IAddTripPresenter {

    private FirebaseAuth mAuth;
    private AddTripActivity addTripActivity;
    private  FirebaseUser currentUser;
    private WorkManager mWorkManager;
    private OneTimeWorkRequest mWorkRequest;
    private static final String WORKER_TAG = "worker";

    AddTripPresenter(AddTripActivity addTripActivity)
    {
        mAuth = FirebaseAuth.getInstance();
        this.addTripActivity = addTripActivity;
        currentUser = mAuth.getCurrentUser();
    }

    /**
     * @param data   tripName , tripDate
     * @param source sourceLocationName , lat , long
     * @param dest   destinationLocationName , lat , long
     * @param notes user trip notes
     */
    @Override
    public void tripProcess(String[] data ,String [] source , String [] dest , String [] notes) {
        doSave(data, source, dest , notes);
    }

    /**
     * Saves Trip data to Firebase
     *
     * @param data tripName , tripDate
     * @param source sourceLocationName , lat , long
     * @param dest destinationLocationName , lat , long
     * @param notes user trip notes
     */
    private void doSave(final String[] data, final String[] source, final String[] dest , final String [] notes) {

        if(currentUser !=null) {
            mWorkManager = WorkManager.getInstance(addTripActivity);
            mWorkRequest = new OneTimeWorkRequest.Builder(TripSchedulingWorker.class)
                    .addTag(WORKER_TAG)
                    .build();

            mWorkManager.enqueueUniqueWork(WORKER_TAG, ExistingWorkPolicy.REPLACE,mWorkRequest);

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            final String tripUID = UUID.randomUUID().toString();
            final Trip mTrip = new Trip();
            // one way
            mTrip.setTripUID(tripUID);
            mTrip.setUserUID(currentUser.getUid());
            mTrip.setTripTitle(data[0]);
            mTrip.setTripDate(data[1]);
            mTrip.setTripTime(data[2]);
            mTrip.setTripType(data[3]);

            mTrip.setTripSource(source[0]);
            mTrip.setSourceLat(Double.parseDouble(source[1]));
            mTrip.setSourceLong(Double.parseDouble(source[2]));

            mTrip.setTripDestination(dest[0]);
            mTrip.setDestinationLat(Double.parseDouble(dest[1]));
            mTrip.setDestinationLong(Double.parseDouble(dest[2]));

            mTrip.setNotes(TextUtils.join(" , ", notes));

            mTrip.setStatus(0);

            reference.child("Trips").push().setValue(mTrip)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(data[3].equals("1")) {
                                addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.trip_saved));
                                addTripActivity.finish();
                            }
                            else
                            {
                                addRoundTrip(data,source, dest ,notes,tripUID);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    addTripActivity.showMessage(e.getMessage());
                }
            });
        }
        else {
            addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.authintication_failed));
        }
    }

    private void addRoundTrip(String[] data, String[] source, String[] dest , String [] notes , String tripUID)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        // round
        Trip mTrip = new Trip();
        mTrip.setTripUID(tripUID);
        mTrip.setUserUID(currentUser.getUid());
        mTrip.setTripTitle(data[0]);
        mTrip.setTripTime(data[2]);
        mTrip.setTripType(data[3]);

        mTrip.setTripSource(dest[0]);
        mTrip.setSourceLat(Double.parseDouble(dest[1]));
        mTrip.setSourceLong(Double.parseDouble(dest[2]));

        mTrip.setTripDestination(source[0]);
        mTrip.setDestinationLat(Double.parseDouble(source[1]));
        mTrip.setDestinationLong(Double.parseDouble(source[2]));

        mTrip.setNotes(TextUtils.join(" , ", notes));

        mTrip.setStatus(0);

        reference.child("Trips").push().setValue(mTrip)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.trip_saved));
                        addTripActivity.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                addTripActivity.showMessage(e.getMessage());
            }
        });
    }
}
