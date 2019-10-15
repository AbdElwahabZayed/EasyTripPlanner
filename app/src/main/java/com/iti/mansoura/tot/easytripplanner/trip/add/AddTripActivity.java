package com.iti.mansoura.tot.easytripplanner.trip.add;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripDateStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripDestinationStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripNotesStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripSourceStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripTitleStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripTypeStep;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class AddTripActivity extends AppCompatActivity implements AddTripContract.IAddTripView , StepperFormListener {

    private static final String TAG = AddTripActivity.class.getSimpleName();
    private AddTripPresenter addTripPresenter;
    private TripTitleStep tripTitleStep;
    private TripTypeStep tripTypeStep;
    private TripSourceStep tripSourceStep;
    private TripDestinationStep tripDestinationStep;
    private TripDateStep tripDateStep;
    private TripNotesStep tripNotesStep;
    private VerticalStepperFormView verticalStepperFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        initComponent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initComponent() {
        addTripPresenter = new AddTripPresenter(this);

        tripTitleStep = new TripTitleStep(getResources().getString(R.string.trip_title_hint));
        tripTypeStep = new TripTypeStep(getResources().getString(R.string.trip_type_title));
        tripSourceStep = new TripSourceStep(getResources().getString(R.string.source),getSupportFragmentManager());
        tripDestinationStep = new TripDestinationStep(getResources().getString(R.string.destination),getSupportFragmentManager());
        tripDateStep = new TripDateStep(getResources().getString(R.string.date));
        tripNotesStep = new TripNotesStep(getResources().getString(R.string.trip_notes),getSupportFragmentManager());
        verticalStepperFormView = findViewById(R.id.stepper_form);
        verticalStepperFormView
                .setup(this,
                        tripTitleStep,
                        tripTypeStep,
                        tripDateStep,
                        tripNotesStep,
                        tripSourceStep,
                        tripDestinationStep)
                .init();
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    }

    /**
     * This method will be called when the user clicks on the last button after all the steps have
     * been marked as completed. It can be used to trigger showing loaders, sending the data, etc.
     * <p>
     * Before this method gets called, the form disables the navigation between steps, as well as
     * all the buttons. To revert the form to normal, call cancelFormCompletionOrCancellationAttempt().
     */
    @Override
    public void onCompletedForm() {
        String [] data = new String[3];
        data[0] = tripTitleStep.getStepDataAsHumanReadableString();
        data[1] = tripDateStep.getStepData();
        if(tripTypeStep.getStepData().equals(getResources().getString(R.string.radio_round)))
            data[2] = "2";
        else
            data[2] = "1";

        String [] sourceData = tripSourceStep.getStepData().split(" , ");
        String [] destData = tripDestinationStep.getStepData().split(" , ");
        String [] notes = tripNotesStep.getStepData().split(" , ");
        addTripPresenter.tripProcess(data, sourceData , destData , notes );
    }

    /**
     * This method will be called when the form has been cancelled, which would generally mean that
     * the user has decided to not save/send the data (for example, by clicking on the cancellation
     * button of the confirmation step).
     * <p>
     * Before this method gets called, the form disables the navigation between steps, as well as
     * all the buttons. To revert the form to normal, call cancelFormCompletionOrCancellationAttempt().
     */
    @Override
    public void onCancelledForm() {
        verticalStepperFormView.cancelFormCompletionOrCancellationAttempt();
        AddTripActivity.this.finish();
    }
}
