package com.iti.mansoura.tot.easytripplanner.trip.steps;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;

import com.iti.mansoura.tot.easytripplanner.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class TripNotesStep extends Step<String> implements Notable {

    private AppCompatImageButton mAdd;
    private FragmentManager fragmentManager;
    private String [] notes;

    public TripNotesStep(String title, FragmentManager fragmentManager) {
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
        if(notes != null)
            return TextUtils.join(" , ", notes);
        else
            return null;
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
        if(notes != null)
            return TextUtils.join(" , ", notes);
        else
            return null;
    }

    /**
     * Restores the step data. Useful for when restoring the state of the form.
     *
     * @param data The step data to restore.
     */
    @Override
    public void restoreStepData(String data) {
        if(data != null) {
            notes = data.split(" , ");
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
        return new IsDataValid(true, "");
    }

    /**
     * This method will be called automatically by the form in order to get the layout of the step.
     *
     * @return The step's layout.
     */
    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.trip_notes_layout, null);
        mAdd = view.findViewById(R.id.add_note);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteFragment noteFragment = NoteFragment.newInstance(getContext().getResources().getString(R.string.add_notes),notes,TripNotesStep.this);
                noteFragment.show(fragmentManager, "note_fragment");
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

    @Override
    public void onSaveNotesClicked(String[] notes) {
        StringBuilder filteredNotes = new StringBuilder();
        for (String note : notes) {
            if(note != null) {
                if(filteredNotes.length() != 0)
                    filteredNotes.append(" , ");
                filteredNotes.append(note);
            }
        }
        this.notes = filteredNotes.toString().split(" , ");
    }
}
