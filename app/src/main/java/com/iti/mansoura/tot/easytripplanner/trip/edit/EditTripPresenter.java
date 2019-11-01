package com.iti.mansoura.tot.easytripplanner.trip.edit;

import android.text.TextUtils;

import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.home.viewmodel.TripViewModel;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

import java.util.UUID;

public class EditTripPresenter implements EditTripContract.IEditTripPresenter {

    private FirebaseAuth mAuth;
    private EditTripActivity editTripActivity;
    private FirebaseUser currentUser;

    EditTripPresenter(EditTripActivity editTripActivity)
    {
        mAuth = FirebaseAuth.getInstance();
        this.editTripActivity = editTripActivity;
        currentUser = mAuth.getCurrentUser();
    }

    /**
     * @param data   tripName , tripDate
     * @param source sourceLocationName , lat , long
     * @param dest   destinationLocationName , lat , long
     * @param notes user trip notes
     */
    @Override
    public void upcomingTripProcess(String[] data ,String [] source , String [] dest , String [] notes,String tripUID,String firebaseUID,boolean alarm) {
        doSave(data, source, dest , notes, tripUID, firebaseUID,alarm);
    }

    @Override
    public void historyTripProcess(String[] data, String[] source, String[] dest, String[] notes,boolean alarm) {
        TripViewModel tripViewModel= ViewModelProviders.of(editTripActivity).get(TripViewModel.class);
        tripViewModel.setContext(editTripActivity.getApplicationContext());
        Trip trip=new Trip();
        trip.setTripUID(data[4]);
        trip.setStatus(3);
        tripViewModel.updateTrip(trip);
        doSave(data, source, dest , notes,alarm);
    }

    /**
     * Saves Trip data to Firebase
     * @param data tripName , tripDate
     * @param source sourceLocationName , lat , long
     * @param dest destinationLocationName , lat , long
     * @param notes user trip notes
     *
     */
    private void doSave(final String[] data, final String[] source, final String[] dest ,
                        final String [] notes,String tripUID , String tripFireBaseUID,boolean alarm)
    {

        if(currentUser !=null) {
            Trip mTrip = new Trip();
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

            mTrip.setFirebaseUID(tripFireBaseUID);
            TripViewModel tripViewModel= ViewModelProviders.of(editTripActivity).get(TripViewModel.class);
            tripViewModel.setContext(editTripActivity.getApplicationContext());
            tripViewModel.updateTripWithReminder(mTrip);

            if(data[3].equals("2") && !alarm) {
                //TODO query trip firebase UID
                updateRoundTrip(data,source, dest ,notes,tripUID);
            }

            editTripActivity.showMessage(editTripActivity.getResources().getString(R.string.trip_saved));
            editTripActivity.onBackPressed();
        }
        else{
            editTripActivity.showMessage(editTripActivity.getResources().getString(R.string.authintication_failed));
        }
    }

    private void doSave(final String[] data, final String[] source, final String[] dest ,
                        final String [] notes,boolean alarm)
    {

        if(currentUser !=null) {
            Trip mTrip = new Trip();
            // one way
            mTrip.setTripUID(UUID.randomUUID().toString());
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

            mTrip.setFirebaseUID(UUID.randomUUID().toString());
            TripViewModel tripViewModel= ViewModelProviders.of(editTripActivity).get(TripViewModel.class);
            tripViewModel.setContext(editTripActivity.getApplicationContext());
            tripViewModel.addTripWithReminder(mTrip);

            if(data[3].equals("2") && !alarm) {
                addRoundTrip(data,source, dest ,notes,mTrip.getTripUID());
            }

            editTripActivity.showMessage(editTripActivity.getResources().getString(R.string.trip_saved));
            editTripActivity.onBackPressed();
        }
        else{
            editTripActivity.showMessage(editTripActivity.getResources().getString(R.string.authintication_failed));
        }
    }

    private void updateRoundTrip(String[] data, String[] source, String[] dest , String [] notes , String tripUID)
    {
        // round
        Trip mTrip = new Trip();
        mTrip.setTripUID(tripUID);
        mTrip.setUserUID(currentUser.getUid());
        mTrip.setTripTitle(data[0]);
        mTrip.setTripDate(data[1]);
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

        //TODO assign the quiried firebase UID
        mTrip.setFirebaseUID("");
        TripViewModel tripViewModel= ViewModelProviders.of(editTripActivity).get(TripViewModel.class);
        tripViewModel.setContext(editTripActivity.getApplicationContext());
        tripViewModel.updateRoundTrip(mTrip);

        editTripActivity.showMessage(editTripActivity.getResources().getString(R.string.trip_saved));
        editTripActivity.onBackPressed();
    }

    private void addRoundTrip(String[] data, String[] source, String[] dest , String [] notes , String tripUID)
    {
        // round
        Trip mTrip = new Trip();
        mTrip.setTripUID(tripUID);
        mTrip.setUserUID(currentUser.getUid());
        mTrip.setTripTitle(data[0]);
        mTrip.setTripDate(data[1]);
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

        String tripFireBaseUID = UUID.randomUUID().toString();
        mTrip.setFirebaseUID(tripFireBaseUID);
        TripViewModel tripViewModel= ViewModelProviders.of(editTripActivity).get(TripViewModel.class);
        tripViewModel.setContext(editTripActivity.getApplicationContext());
        tripViewModel.addRoundTrip(mTrip);

        editTripActivity.showMessage(editTripActivity.getResources().getString(R.string.trip_saved));
        editTripActivity.onBackPressed();
    }
}
