package com.iti.mansoura.tot.easytripplanner.trip.edit;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.db.TripDB.TripRepository;
import com.iti.mansoura.tot.easytripplanner.models.Trip;
import com.iti.mansoura.tot.easytripplanner.retorfit.NetworkStatusAndType;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripDateStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripDestinationStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripNotesStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripSourceStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripTimeStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripTitleStep;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripTypeStep;

import androidx.appcompat.app.AppCompatActivity;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class EditTripActivity extends AppCompatActivity implements EditTripContract.IEditTripView , StepperFormListener {
    private static final String TAG = EditTripActivity.class.getSimpleName();
    private EditTripPresenter editTripPresenter;
    private TripTitleStep tripTitleStep;
    private TripTypeStep tripTypeStep;
    private TripSourceStep tripSourceStep;
    private TripDestinationStep tripDestinationStep;
    private TripTimeStep tripTimeStep;
    private TripDateStep tripDateStep;
    private TripNotesStep tripNotesStep;
    private VerticalStepperFormView verticalStepperFormView;
    private FirebaseAuth mAuth;
    private String tripUID ,firebaseUID;
    private int tripStatus; // using trip status
    private Trip mTrip;
    private TripRepository tripRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        tripUID = getIntent().getExtras().getString("tripUID");
        firebaseUID = getIntent().getExtras().getString("firebaseUID");
        tripStatus = getIntent().getExtras().getInt("tripStatus");

        mAuth = FirebaseAuth.getInstance();
        tripRepository = new TripRepository(this);

        if (new NetworkStatusAndType(this).NetworkStatus() == 2) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Trips").child(firebaseUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0)
                    {
                        Trip mTrip = dataSnapshot.getValue(Trip.class);
                        if(mTrip.getTripUID().equals(tripUID) && mTrip.getUserUID().equals(mAuth.getCurrentUser().getUid())) {
                            EditTripActivity.this.mTrip = mTrip;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("trip edit f " , "error"+databaseError.getMessage());
                }
            });
        }else {
            if(tripStatus == 0)
                mTrip = tripRepository.getUpComingTrip(mAuth.getCurrentUser().getUid(), tripUID);
            else
                mTrip = tripRepository.getHistoryTrip(mAuth.getCurrentUser().getUid(), tripUID);
            Log.e("trip edit l " , "local");
        }

        initComponent();
    }

    @Override
    public void initComponent() {
        editTripPresenter = new EditTripPresenter(this);

        tripTitleStep = new TripTitleStep(getResources().getString(R.string.trip_title_hint));
        tripTypeStep = new TripTypeStep(getResources().getString(R.string.trip_type_title));
        tripSourceStep = new TripSourceStep(getResources().getString(R.string.source),getSupportFragmentManager());
        tripDestinationStep = new TripDestinationStep(getResources().getString(R.string.destination),getSupportFragmentManager());
        tripDateStep = new TripDateStep(getResources().getString(R.string.date));
        tripTimeStep = new TripTimeStep(getResources().getString(R.string.time));
        tripNotesStep = new TripNotesStep(getResources().getString(R.string.trip_notes),getSupportFragmentManager());
        verticalStepperFormView = findViewById(R.id.stepper_form);



        verticalStepperFormView
                .setup(this,
                        tripTitleStep,
                        tripTypeStep,
                        tripDateStep,
                        tripTimeStep,
                        tripNotesStep,
                        tripSourceStep,
                        tripDestinationStep)
                .lastStepNextButtonText(getResources().getString(R.string.update_trip))
                //.displayStepButtons(false)// for show
                .init();
        if(mTrip != null) {
            tripTitleStep.restoreStepData(mTrip.getTripTitle());
            tripTypeStep.restoreStepData(mTrip.getTripType());
            tripSourceStep.restoreStepData(mTrip.getTripSource());
            tripDestinationStep.restoreStepData(mTrip.getTripDestination());
            tripDateStep.restoreStepData(mTrip.getTripDate());
            tripTimeStep.restoreStepData(mTrip.getTripTime());
            tripNotesStep.restoreStepData(mTrip.getNotes());
        }else{
            Log.e("null","null");
        }

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

        editTripPresenter.tripProcess(data, sourceData , destData , notes );
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
        EditTripActivity.this.finish();
    }
}
