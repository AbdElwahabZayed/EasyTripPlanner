package com.iti.mansoura.tot.easytripplanner.trip.steps;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.trip.add.AddTripActivity;

public class NoteFragment extends DialogFragment {

    private FloatingActionButton mAddNote;
    private AppCompatButton mSave;
    private LinearLayoutCompat mContainer;
    private String [] notes;
    private Notable notable;

    public NoteFragment(Notable notable) {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
        this.notable = notable;
    }

    public static NoteFragment newInstance(String title,String [] notes,Notable notable) {
        NoteFragment frag = new NoteFragment(notable);
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArray("notes", notes);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set style for the fragment
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_fragment_layout, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mContainer = view.findViewById(R.id.container);
        mAddNote = view.findViewById(R.id.add);
        mSave = view.findViewById(R.id.save);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        notes = getArguments().getStringArray("notes");
        if(notes == null)
        {
            notes = new String[10];
        }
        else
        {
            displayNotes();
        }

        setUpContainer();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notable.onSaveNotesClicked(notes);
                NoteFragment.this.dismissAllowingStateLoss();
            }
        });
    }

    private void displayNotes()
    {
        for(int i = 0; i < notes.length ; i++) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View inflatedView = inflater.inflate(R.layout.note_layout, null);
            inflatedView.setId(View.generateViewId());
            mContainer.addView(inflatedView);

            AppCompatImageButton mRemove = inflatedView.findViewById(R.id.remove);
            AppCompatEditText mNote = inflatedView.findViewById(R.id.note_data);

            mNote.setText(notes[i]);

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
                    if (!s.toString().trim().isEmpty()) {
                        // get index of the layout inside the container
                        int indexOfMyView = mContainer.indexOfChild(inflatedView);
                        // add to array
                        notes[indexOfMyView] = s.toString().trim();
                    }
                }
            });

            mRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get index of the layout inside the container
                    // then update notes
                    int indexOfMyView = mContainer.indexOfChild(inflatedView);
                    if(notes.length-1 >= indexOfMyView)
                        notes[indexOfMyView] = null;
                    // remove view using it's child
                    mContainer.removeView((View) v.getParent());
                }
            });
        }
    }

    private void setUpContainer()
    {
        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContainer.getChildCount() < 10) {
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View inflatedView = inflater.inflate(R.layout.note_layout, null);
                    inflatedView.setId(View.generateViewId());
                    mContainer.addView(inflatedView);

                    AppCompatImageButton mRemove = inflatedView.findViewById(R.id.remove);
                    AppCompatEditText mNote = inflatedView.findViewById(R.id.note_data);

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
                            if (!s.toString().trim().isEmpty()) {
                                // get index of the layout inside the container
                                int indexOfMyView = mContainer.indexOfChild(inflatedView);
                                // add to array
                                notes[indexOfMyView] =s.toString().trim();
                            }
                        }
                    });

                    mRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // get index of the layout inside the container
                            // then update notes
                            int indexOfMyView = mContainer.indexOfChild(inflatedView);
                            Log.e("index", ""+indexOfMyView);
                            if(notes.length-1 >= indexOfMyView)
                                notes[indexOfMyView] = null;

                            // remove view using it's child
                            mContainer.removeView((View) v.getParent());
                        }
                    });
                }
                else {
                    ((AddTripActivity)getActivity()).showMessage(getContext().getResources().getString(R.string.notes_limit));
                }
            }
        });
    }
}
