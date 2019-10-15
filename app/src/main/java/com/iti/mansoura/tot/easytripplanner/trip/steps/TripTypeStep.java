package com.iti.mansoura.tot.easytripplanner.trip.steps;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.iti.mansoura.tot.easytripplanner.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class TripTypeStep extends Step<String> {

    private AppCompatTextView tripType;
    private RadioGroup mTripGroup;
    private RadioButton mOne,mRound;

    public TripTypeStep(String title) {
        super(title);
    }

    /**
     * Gets the data of this step (i.e., the information that the user has filled in for this field).
     *
     * @return The step data.
     */
    @Override
    public String getStepData() {
        String tripType = this.tripType.getText().toString();
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
        tripType.setText(data);
        if(data.equals(getContext().getResources().getString(R.string.radio_round)))
            mRound.setChecked(true);
        else
            mOne.setChecked(true);
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
        String errorMessage = !isTypeValid ? getContext().getResources().getString(R.string.trip_type_title) : "";

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
        View view = inflater.inflate(R.layout.trip_type_layout, null);
        tripType = view.findViewById(R.id.trip_selected_type);
        mTripGroup = view.findViewById(R.id.radio_trip);
        mOne = view.findViewById(R.id.radio_one);
        mRound = view.findViewById(R.id.radio_round);
        mTripGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    markAsCompleted(true);
                    tripType.setText(checkedRadioButton.getText());
                }
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
