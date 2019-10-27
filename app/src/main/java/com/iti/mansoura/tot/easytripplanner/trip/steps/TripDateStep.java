package com.iti.mansoura.tot.easytripplanner.trip.steps;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.widget.AppCompatEditText;

import com.iti.mansoura.tot.easytripplanner.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ernestoyaquello.com.verticalstepperform.Step;

public class TripDateStep extends Step<String> {

    private AppCompatEditText tripDate;

    public TripDateStep(String title) {
        super(title);
    }

    /**
     * Gets the data of this step (i.e., the information that the user has filled in for this field).
     *
     * @return The step data.
     */
    @Override
    public String getStepData() {
        Editable tripDate = this.tripDate.getText();
        return tripDate != null ? tripDate.toString() : "";
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
        String tripDate = getStepData();
        return !tripDate.isEmpty() ? tripDate : getContext().getResources().getString(R.string.empty_step);
    }

    /**
     * Restores the step data. Useful for when restoring the state of the form.
     *
     * @param data The step data to restore.
     */
    @Override
    public void restoreStepData(String data) {
        try {
            String stringCurrentDate = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
            Date currentDateTime = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).parse(stringCurrentDate);
            Date tripDateTime = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).parse(data);
            if (currentDateTime.before(tripDateTime))
                tripDate.setText(data);
        }
        catch (ParseException ex){
            Log.e("Date Step", ""+ex.getMessage());
        }
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
        boolean isDateValid = stepData.length() > 0;
        String errorMessage = !isDateValid ? getContext().getResources().getString(R.string.error_title) : "";

        return new IsDataValid(isDateValid, errorMessage);
    }

    /**
     * This method will be called automatically by the form in order to get the layout of the step.
     *
     * @return The step's layout.
     */
    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.trip_date_layout, null);
        tripDate = view.findViewById(R.id.date_value);

        tripDate.addTextChangedListener(new TextWatcher() {
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

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateValue(myCalendar);
            }
        };

        tripDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog  = new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTime().getTime());
                datePickerDialog.show();
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

    private void updateDateValue(Calendar myCalendar) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        tripDate.setText(sdf.format(myCalendar.getTime()));
    }
}
