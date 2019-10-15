package com.iti.mansoura.tot.easytripplanner.trip.add;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

public class AddTripPresenter implements AddTripContract.IAddTripPresenter {

    private FirebaseAuth mAuth;
    private AddTripActivity addTripActivity;

    AddTripPresenter(AddTripActivity addTripActivity)
    {
        mAuth = FirebaseAuth.getInstance();
        this.addTripActivity = addTripActivity;
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
    private void doSave(final String[] data, String[] source, String[] dest , String [] notes) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference();

            Trip mTrip = new Trip();
            // one way
            mTrip.setTripTitle(data[0]);
            mTrip.setTripDate(data[1]);
            mTrip.setTripType(data[2]);

            mTrip.setTripSource(source[0]);
            mTrip.setSourceLat(Double.parseDouble(source[1]));
            mTrip.setSourceLong(Double.parseDouble(source[2]));

            mTrip.setTripDestination(dest[0]);
            mTrip.setDestinationLat(Double.parseDouble(dest[1]));
            mTrip.setDestinationLong(Double.parseDouble(dest[2]));

            mTrip.setNotes(TextUtils.join(" , ", notes));

            reference.child("Trips").push().setValue(mTrip)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.trip_saved));
                            if(data[2].equals("1"))
                                addTripActivity.finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    addTripActivity.showMessage(e.getMessage());
                }
            });

            // round trip
            if(data[2].equals("2")) {
                mTrip.setTripTitle(data[0]);
                mTrip.setTripDate(data[1]);
                mTrip.setTripType(data[2]);

                mTrip.setTripSource(dest[0]);
                mTrip.setSourceLat(Double.parseDouble(dest[1]));
                mTrip.setSourceLong(Double.parseDouble(dest[2]));

                mTrip.setTripDestination(source[0]);
                mTrip.setDestinationLat(Double.parseDouble(source[1]));
                mTrip.setDestinationLong(Double.parseDouble(source[2]));

                mTrip.setNotes(TextUtils.join(" , ", notes));

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
        else {
            addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.authintication_failed));
        }
    }
}
