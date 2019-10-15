package com.iti.mansoura.tot.easytripplanner.trip.steps;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

import com.iti.mansoura.tot.easytripplanner.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class TripTitleStep extends Step<String> {

    private AppCompatEditText tripTitle;

    public TripTitleStep(String title) {
        super(title);
    }

    /**
     * Gets the data of this step (i.e., the information that the user has filled in for this field).
     *
     * @return The step data.
     */
    @Override
    public String getStepData() {
        Editable tripTitle = this.tripTitle.getText();
        return tripTitle != null ? tripTitle.toString() : "";
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
        String tripTitle = getStepData();
        return !tripTitle.isEmpty() ? tripTitle : getContext().getResources().getString(R.string.empty_step);
    }

    /**
     * Restores the step data. Useful for when restoring the state of the form.
     *
     * @param data The step data to restore.
     */
    @Override
    public void restoreStepData(String data) {
        tripTitle.setText(data);
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
        boolean isNameValid = stepData.length() > 0;
        String errorMessage = !isNameValid ? getContext().getResources().getString(R.string.error_title) : "";

        return new IsDataValid(isNameValid, errorMessage);
    }

    /**
     * This method will be called automatically by the form in order to get the layout of the step.
     *
     * @return The step's layout.
     */
    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.trip_title_layout, null);
        tripTitle = view.findViewById(R.id.title);
        tripTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!s.toString().trim().isEmpty())
                    markAsCompleted(true);
                else
                    markAsUncompleted(getContext().getResources().getString(R.string.error_title),true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                markAsCompletedOrUncompleted(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().trim().isEmpty())
                    markAsCompleted(true);
                else
                    markAsUncompleted(getContext().getResources().getString(R.string.error_title),true);

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
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(tripTitle.getWindowToken(), 0);

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
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tripTitle.getWindowToken(), 0);

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
