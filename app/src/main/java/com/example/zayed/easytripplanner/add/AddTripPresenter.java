package com.example.zayed.easytripplanner.add;

import com.example.zayed.easytripplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        if(!data[0].isEmpty())
        {
            if (source.length > 0) {
                if (dest.length > 0) {
                    try {
                        if(!data[1].isEmpty()) {
                            if (new Date().before(new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).parse(data[1]))) {
                                doSave(data, source, dest , notes);
                            } else {
                                addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.error_date));
                            }
                        }
                        else {
                            addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.error_empty_date));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.error_destination));
                }
            } else {
                addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.error_source));
            }
        }
        else
        {
            addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.error_title));
        }
    }

    /**
     * Saves Trip data to Firebase
     *
     * @param data tripName , tripDate
     * @param source sourceLocationName , lat , long
     * @param dest destinationLocationName , lat , long
     * @param notes user trip notes
     */
    private void doSave(String[] data, String[] source, String[] dest , String [] notes) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null) {

            if(data[2].equals("2")) {
                //TODO save data to Fire-base as round trip
            }
            else {
                //TODO save data to Fire-base as one way trip
            }
            addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.trip_saved));
        }
        else {
            addTripActivity.showMessage(addTripActivity.getResources().getString(R.string.authintication_failed));
        }
    }
}
