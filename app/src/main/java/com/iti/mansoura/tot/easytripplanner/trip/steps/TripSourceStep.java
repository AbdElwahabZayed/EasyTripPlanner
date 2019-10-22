package com.iti.mansoura.tot.easytripplanner.trip.steps;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.iti.mansoura.tot.easytripplanner.R;

import java.util.Arrays;
import java.util.Locale;

import ernestoyaquello.com.verticalstepperform.Step;

public class TripSourceStep extends Step<String> {

    private AppCompatTextView locationText , locationValue;
    private AutocompleteSupportFragment mSource;
    private FragmentManager fragmentManager;
    private static final String TAG = TripSourceStep.class.getSimpleName();

    public TripSourceStep(String title , FragmentManager fragmentManager) {
        super(title);
        this.fragmentManager = fragmentManager;
    }

    /**
     * Gets the data of this step (i.e., the information that the user has filled in for this field).
     *
     * @return The step data.
     */
    @Override
    public String getStepData() {
        String tripType = this.locationValue.getText().toString();
        return tripType;
    }

    /**
     * Gets the data of this step (i.e., the information that the user has filled in for this field)
     * as a human-readable string. When the option displayStepDataInSubtitleOfClosedSteps is
     * activated, the text returned by this method will be the one displayed in the step's subtitle.
     *
     * @return The step data as a human-readable string.
     */
    @Override
    public String getStepDataAsHumanReadableString() {
        String tripType = getStepData();
        return !tripType.isEmpty() ? tripType : getContext().getResources().getString(R.string.empty_step);
    }

    /**
     * Restores the step data. Useful for when restoring the state of the form.
     *
     * @param data The step data to restore.
     */
    @Override
    public void restoreStepData(String data) {
        locationValue.setText(data);
    }

    /**
     * Returns an instance of IsDataValid that indicates whether the step data is valid or not.
     * This instance also contains an optional error message for when the data is not valid.
     *
     * @param stepData The data whose validity will be checked.
     * @return An instance of IsDataValid with information about the validity of the data.
     */
    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        boolean isTypeValid = stepData.length() > 0;
        String errorMessage = !isTypeValid ? getContext().getResources().getString(R.string.error_source) : "";

        return new IsDataValid(isTypeValid, errorMessage);
    }

    /**
     * This method will be called automatically by the form in order to get the layout of the step.
     *
     * @return The step's layout.
     */
    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.trip_source_layout, null);

        locationText = view.findViewById(R.id.source_text);
        locationValue = view.findViewById(R.id.source_value);
        mSource = (AutocompleteSupportFragment)fragmentManager.findFragmentById(R.id.source_select);

        locationText.setText(getContext().getResources().getString(R.string.source));

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getContext().getResources().getString(R.string.google_api_key), Locale.getDefault());
            PlacesClient placesClient = Places.createClient(getContext());
        }

        locationValue.setText("Mansoura".concat(" , ").concat("31.0409").concat(" , ").concat("31.3785"));

        mSource.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        mSource.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if(place != null) {
                    Log.e(TAG, "Place: " + place.getName());
                    locationValue.setText(place.getName().concat(" , ").concat(String.valueOf(place.getLatLng().latitude)).concat(" , ").concat(String.valueOf(place.getLatLng().longitude)));
                    markAsCompleted(true);
                }
            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, "An error occurred: " + status.getStatusMessage() + " , " + status.getStatusCode());
                locationValue.setText("");
                markAsUncompleted(getContext().getResources().getString(R.string.error_source),true);
            }
        });
        return view;
    }

    /**
     * This method will be called every time the step is opened.
     *
     * @param animated True if the step was opened using animations; false otherwise.
     *                 Generally, it will only be false if the step was opened on loading or on
     *                 restoration.
     */
    @Override
    protected void onStepOpened(boolean animated) {

    }

    /**
     * This method will be called every time the step is closed.
     *
     * @param animated True if the step was closed using animations; false otherwise.
     *                 Generally, it will only be false if the step was closed on loading or on
     *                 restoration.
     */
    @Override
    protected void onStepClosed(boolean animated) {

    }

    /**
     * This method will be called every time the step is marked as completed.
     *
     * @param animated True if the step was marked as completed using animations; false otherwise.
     *                 Generally, it will only be false if the step was marked as completed on
     *                 loading or on restoration.
     */
    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {

    }

    /**
     * This method will be called every time the step is marked as uncompleted.
     *
     * @param animated True if the step was marked as uncompleted using animations; false otherwise.
     *                 Generally, it will only be false if the step was marked as uncompleted on
     *                 loading or on restoration.
     */
    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {

    }
}
