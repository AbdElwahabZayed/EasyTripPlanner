package com.iti.mansoura.tot.easytripplanner.trip.add;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.home.upcoming.TripsRecyclerViewAdapter;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripDateStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripDestinationStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripNotesStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripSourceStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripTimeStep;
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
    private TripTimeStep tripTimeStep;
    private TripDateStep tripDateStep;
    private TripNotesStep tripNotesStep;
    private VerticalStepperFormView verticalStepperFormView;
    private Toolbar toolbar;
    private TripsRecyclerViewAdapter tripsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        if(getIntent()!=null){
            tripsRecyclerViewAdapter=(TripsRecyclerViewAdapter) getIntent().getSerializableExtra("adapter");
        }
        initComponent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initComponent() {
        toolbar = findViewById(R.id.toolbar);
        verticalStepperFormView = findViewById(R.id.stepper_form);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addTripPresenter = new AddTripPresenter(this);

        tripTitleStep = new TripTitleStep(getResources().getString(R.string.trip_title_hint));
        tripTypeStep = new TripTypeStep(getResources().getString(R.string.trip_type_title));
        tripSourceStep = new TripSourceStep(getResources().getString(R.string.source),getSupportFragmentManager());
        tripDestinationStep = new TripDestinationStep(getResources().getString(R.string.destination),getSupportFragmentManager());
        tripDateStep = new TripDateStep(getResources().getString(R.string.date));
        tripTimeStep = new TripTimeStep(getResources().getString(R.string.time));
        tripNotesStep = new TripNotesStep(getResources().getString(R.string.trip_notes),getSupportFragmentManager());
        verticalStepperFormView
                .setup(this,
                        tripTitleStep,
                        tripTypeStep,
                        tripDateStep,
                        tripTimeStep,
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
        String [] data = new String[4];
        data[0] = tripTitleStep.getStepDataAsHumanReadableString();
        data[1] = tripDateStep.getStepData();
        data[2] = tripTimeStep.getStepData();
        if(tripTypeStep.getStepData().equals(getResources().getString(R.string.radio_round)))
            data[3] = "2";
        else
            data[3] = "1";

        String [] sourceData = tripSourceStep.getStepData().split(" , ");
        String [] destData = tripDestinationStep.getStepData().split(" , ");

        String [] notes;
        if(tripNotesStep.getStepData() != null)
            notes = tripNotesStep.getStepData().split(" , ");
        else
            notes = new String[]{""};

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
        tripsRecyclerViewAdapter.notifyDataSetChanged();
        verticalStepperFormView.cancelFormCompletionOrCancellationAttempt();
        AddTripActivity.this.finish();
    }
}
