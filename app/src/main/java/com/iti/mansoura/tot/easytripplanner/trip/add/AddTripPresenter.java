package com.iti.mansoura.tot.easytripplanner.trip.add;

import android.text.TextUtils;

import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.home.viewmodel.TripViewModel;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

import java.util.UUID;

public class AddTripPresenter implements AddTripContract.IAddTripPresenter {

    private FirebaseAuth mAuth;
    private AddTripActivity addTripActivity;
    private FirebaseUser currentUser;

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

            String tripFireBaseUID = UUID.randomUUID().toString();
            mTrip.setFirebaseUID(tripFireBaseUID);
            TripViewModel tripViewModel= ViewModelProviders.of(addTripActivity).get(TripViewModel.class);
            tripViewModel.setContext(addTripActivity.getApplicationContext());
            tripViewModel.addTripWithReminder(mTrip);

            if(data[3].equals("2")) {
                addRoundTrip(data,source, dest ,notes,tripUID);
            }

            addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.trip_saved));
            addTripActivity.onBackPressed();
        }
        else{
            addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.authintication_failed));
        }
    }

    private void addRoundTrip(String[] data, String[] source, String[] dest , String [] notes , String tripUID)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        // round
        final Trip mTrip = new Trip();
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
        TripViewModel tripViewModel= ViewModelProviders.of(addTripActivity).get(TripViewModel.class);
        tripViewModel.setContext(addTripActivity.getApplicationContext());
        tripViewModel.addRoundTrip(mTrip);

        addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.trip_saved));
        addTripActivity.onBackPressed();
    }
}
