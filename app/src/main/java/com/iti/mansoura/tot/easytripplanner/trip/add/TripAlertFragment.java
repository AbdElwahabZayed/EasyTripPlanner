package com.iti.mansoura.tot.easytripplanner.trip.add;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.models.Trip;
import com.iti.mansoura.tot.easytripplanner.retorfit.NetworkStatusAndType;

import java.util.Random;

public class TripAlertFragment extends DialogFragment {

    private FirebaseAuth mAuth;
    private AppCompatTextView mTripTitle;
    private AppCompatButton mStart , mCancel , mLater;
    private String tripUID;
    private MediaPlayer mediaPlayer;
    private Trip mTrip;

    public TripAlertFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
        mAuth = FirebaseAuth.getInstance();
    }

    public static TripAlertFragment newInstance(String title,String tripUID) {
        TripAlertFragment frag = new TripAlertFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("tripUID", tripUID);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getArguments().getString("title", "Trip Started");
        tripUID =  getArguments().getString("tripUID", "");
//        getDialog().setTitle(title);
        // set style for the fragment
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alert_fragment_layout, container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTripTitle = view.findViewById(R.id.trip_title);
        mStart = view.findViewById(R.id.start);
        mLater = view.findViewById(R.id.later);
        mCancel = view.findViewById(R.id.cancel);

        mediaPlayer = MediaPlayer.create(getContext(), R.raw.town);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        if (new NetworkStatusAndType(getActivity()).NetworkStatus() == 2) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Trips").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren())
                    {
                        Trip mTrip = childDataSnapshot.getValue(Trip.class);
                        if(mTrip.getTripUID().equals(tripUID) && mTrip.getUserUID().equals(mAuth.getCurrentUser().getUid())) {
                            TripAlertFragment.this.mTrip = mTrip;
                            mTripTitle.setText(mTrip.getTripTitle());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("trip alert f " , "error"+databaseError.getMessage());
                }
            });
        }else {
            // TODO fetch trip data from local
        }

        mLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTrip != null)
                    showNotification(mTrip.getTripTitle(), mTrip.getTripSource() + " -> " + mTrip.getTripDestination());
                else
                    showNotification("N/A" , "N/A -> N/A");

                TripAlertFragment.this.dismissAllowingStateLoss();
                getActivity().finish();
            }
        });

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new NetworkStatusAndType(getActivity()).NetworkStatus() == 2) {
                    // TODO fetch trip data then open map activity + History Worker + floating widget
                    TripAlertFragment.this.dismissAllowingStateLoss();
                    getActivity().finish();
                    showMap();
                    showNotification(mTrip.getTripTitle(), mTrip.getTripSource() + " -> " + mTrip.getTripDestination());
                } else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.network_not_avaliable),Toast.LENGTH_LONG).show();
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new NetworkStatusAndType(getActivity()).NetworkStatus() == 2) {
                    // TODO add trip to history ( update firebase + local)
                }
                else {
                    // TODO add trip to history ( update local)
                    Toast.makeText(getActivity(),getResources().getString(R.string.network_not_avaliable),Toast.LENGTH_LONG).show();
                }
                TripAlertFragment.this.dismissAllowingStateLoss();
                getActivity().finish();
            }
        });
    }

    private void showNotification(String tripTitle, String route) {
        // random number between 0 : 15 == 16
        int random = new Random().nextInt(15);

        NotificationManagerCompat manager = NotificationManagerCompat.from(getActivity());

        String channelId = "task_channel";
        String channelName = "task_name";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        Intent action1Intent = new Intent(getActivity(), NotificationActionService.class)
                .putExtra("tripUID",tripUID)
                .putExtra("notificationID",random)
                .setAction(tripUID);

        PendingIntent action1PendingIntent = PendingIntent.getService(getActivity(), 0,
                action1Intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), channelId)
                .setContentTitle(tripTitle)
                .setContentText(route)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setAutoCancel(false)
                .addAction(new NotificationCompat.Action(R.drawable.ic_start,
                        getResources().getString(R.string.take_action), action1PendingIntent));


        manager.notify(random, builder.build());
    }

    private void showMap()
    {
        try {
            // navigation intent
            Uri gmmIntentUri = Uri.parse("google.navigation:"+mTrip.getSourceLat()+","+mTrip.getSourceLong()+"?z=10&q="+mTrip.getDestinationLat()+","+mTrip.getDestinationLong()+"&avoid=tb");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
        catch (ActivityNotFoundException e) {
            try {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.google.android.apps.maps")));
            }
            catch (ActivityNotFoundException a) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
            }

            e.printStackTrace();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mediaPlayer.setLooping(false);
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
