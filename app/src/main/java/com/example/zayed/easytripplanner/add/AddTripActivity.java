package com.example.zayed.easytripplanner.add;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zayed.easytripplanner.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;

public class AddTripActivity extends AppCompatActivity implements AddTripContract.IAddTripView{

    private AppCompatEditText mTripTitle, mDate,mnote;
    private AutocompleteSupportFragment mSource, mDestination;
    private AppCompatButton mSave;
    private RadioGroup mTripGroup;
    private RadioButton mOne,mRound;
    private ViewStub mViewStub;
    private AppCompatImageButton mAdd;
    private static final String TAG = AddTripActivity.class.getSimpleName();
    private String [] sourceData , destData , notes;

    private AddTripPresenter addTripPresenter;

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
        mnote=findViewById(R.id.note_data);
        addTripPresenter = new AddTripPresenter(this);
        ///Added By Abdelwahab did not work with me without adding these two lines
        ///{
        Places.initialize(getApplicationContext(),"AIzaSyBiSF7FB8JNV1DTfyZyYSNyOGkrdW7xGeU");
        PlacesClient placesClient = Places.createClient(this);
        ////////}
        mSave = findViewById(R.id.save);
        mTripTitle = findViewById(R.id.title);
        mDate = findViewById(R.id.date_value);
        mTripGroup = findViewById(R.id.radio_trip);
        mOne = findViewById(R.id.radio_one);
        mRound = findViewById(R.id.radio_round);
        mViewStub = findViewById(R.id.notes);
        mSource = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.source_select);
        mDestination = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.destination_select);
        mAdd = findViewById(R.id.add_note);

        notes = new String[10];
        setUpViewStub();

        mSource.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        mDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        mSource.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if(place != null &&place.getLatLng()!=null) {
                    Log.e(TAG, "Place: " + place.getName());
                    sourceData = new String[3];
                    mSource.setText(place.getName());
                    sourceData[0] = place.getName();
                    sourceData[1] = String.valueOf(place.getLatLng().latitude);
                    sourceData[2] = String.valueOf(place.getLatLng().longitude);
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e(TAG, "An error occurred: " + status);
            }
        });

        mDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.e(TAG, "Place: " + place.getName());
                if(place != null &&place.getLatLng()!=null) {
                    destData = new String[3];
                    destData[0] = place.getName();
                    destData[1] = String.valueOf(place.getLatLng().latitude);
                    destData[2] = String.valueOf(place.getLatLng().longitude);
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e(TAG, "An error occurred: " + status);
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

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog  = new DatePickerDialog(AddTripActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTime().getTime());
                datePickerDialog.show();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] data = new String[3];
                data[0] = mDate.getText().toString().trim();
                data[1] = mTripTitle.getText().toString().trim();
                if(mTripGroup.getCheckedRadioButtonId() == mOne.getId())
                    data[2] = "1";
                else
                    data[2] = "2";

                addTripPresenter.tripProcess(data, sourceData , destData , notes );
            }
        });
    }

    private void setUpViewStub()
    {
        mViewStub.setLayoutResource(R.layout.sub_view_stub);
        View inflatedView = mViewStub.inflate();
        final LinearLayoutCompat container = inflatedView.findViewById(R.id.container);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(container.getChildCount() < 10) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View inflatedView = inflater.inflate(R.layout.note_layout, null);
                    inflatedView.setId(View.generateViewId());
                    container.addView(inflatedView);

                    AppCompatImageButton mRemove = inflatedView.findViewById(R.id.remove);
                    final AppCompatEditText mNote = inflatedView.findViewById(R.id.note_data);
                    mNote.setMaxHeight(300);
                    mNote.setOnTouchListener(new View.OnTouchListener() {
                        public boolean onTouch(View view, MotionEvent event) {
                            // TODO Auto-generated method stub
                            if (view.getId() ==R.id.note_data) {
                                view.getParent().requestDisallowInterceptTouchEvent(true);
                                switch (event.getAction()&MotionEvent.ACTION_MASK){
                                    case MotionEvent.ACTION_UP:
                                        view.getParent().requestDisallowInterceptTouchEvent(false);
                                        break;
                                }
                            }
                            return false;
                        }
                    });
                    mNote.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {



                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // ensure its not null
                            if (!mNote.getText().toString().trim().isEmpty()) {
                                // get index of the view
                                int indexOfMyView = ((ViewGroup) mNote.getParent()).indexOfChild(mNote);
                                // add to array
                                notes[indexOfMyView] = mNote.getText().toString().trim();
                            }
                        }
                    });

                    mRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // remove view using it's child
                            container.removeView((View) v.getParent());
                        }
                    });
                }
                else {
                    showMessage(getResources().getString(R.string.notes_limit));
                }
            }
        });
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    }

    private void updateDateValue(Calendar myCalendar) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        mDate.setText(sdf.format(myCalendar.getTime()));
    }
}
