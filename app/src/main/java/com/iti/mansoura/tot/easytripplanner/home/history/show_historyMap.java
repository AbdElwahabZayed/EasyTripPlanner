package com.iti.mansoura.tot.easytripplanner.home.history;

import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.home.history.map.DirectionFinder;
import com.iti.mansoura.tot.easytripplanner.home.history.map.DirectionFinderListener;
import com.iti.mansoura.tot.easytripplanner.home.history.map.Route;
import com.iti.mansoura.tot.easytripplanner.home.viewmodel.TripViewModel;
import com.iti.mansoura.tot.easytripplanner.models.Trip;

import java.io.UnsupportedEncodingException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.iti.mansoura.tot.easytripplanner.home.history.map.GoogleMapHelper.buildCameraUpdate;
import static com.iti.mansoura.tot.easytripplanner.home.history.map.GoogleMapHelper.defaultMapSettings;
import static com.iti.mansoura.tot.easytripplanner.home.history.map.GoogleMapHelper.getDefaultPolyLines;
import static com.iti.mansoura.tot.easytripplanner.home.history.map.GoogleMapHelper.getDottedPolylines;
import static com.iti.mansoura.tot.easytripplanner.home.history.map.UiHelper.showAlwaysCircularProgressDialog;


public class show_historyMap extends AppCompatActivity implements DirectionFinderListener {
    private enum PolylineStyle {
        DOTTED,
        PLAIN
    }

    private static final String[] POLYLINE_STYLE_OPTIONS = new String[]{
            "PLAIN",
            "DOTTED"
    };
    private PolylineStyle polylineStyle = PolylineStyle.PLAIN;
    private GoogleMap googleMap1;
    private Polyline polyline;
    private MaterialDialog materialDialog;
    private Toolbar toolbar;
    private TripViewModel tripViewModel;
    private FirebaseAuth mAuth;
    private LiveData<Trip> history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history_map);
        mAuth =FirebaseAuth.getInstance();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        assert mapFragment != null;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                defaultMapSettings(googleMap);
                show_historyMap.this.googleMap1 = googleMap;
            }
        });
        boolean fromFab=getIntent().getBooleanExtra("FromFAB",false);
        tripViewModel= ViewModelProviders.of(this).get(TripViewModel.class);
        tripViewModel.setContext(this);
        if(fromFab) {
            tripViewModel.getAllHistoryTrips(mAuth.getUid()).observe(this, new Observer<List<Trip>>() {
                @Override
                public void onChanged(List<Trip> trips) {
                    for (Trip t : trips) {
                        if (trips != null)
                            System.out.println("wwwwwwwwwww" + trips.size());
                        String o = "";
                        o = o + "" + t.getSourceLat() + "," + t.getSourceLong();
                        String d = "";
                        d = d + "" + t.getDestinationLat() + "," + t.getDestinationLong();
                        System.out.println(o + "," + d);
                        if (!o.isEmpty() && !d.isEmpty())
                            fetchDirections(o, d);
                    }
                }
            });
        }else {
            String o=getIntent().getStringExtra("Origin");
            String d=getIntent().getStringExtra("Destination");
            fetchDirections(o,d);
        }
        //fetchDirections("31.040949,31.378469", "30.044420,31.235712");
        //fetchDirections("31.040949,31.378469", "31.200092,29.918739");
    }
    private void fetchDirections(String origin, String destination) {
        try {
            new DirectionFinder(this, origin, destination).execute(); // 1
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDirectionFinderStart() {
        if (materialDialog == null)
            materialDialog = showAlwaysCircularProgressDialog(this, "Fetching Directions...");
        else materialDialog.show();
    }

    @Override
    public void onDirectionFinderSuccess(List<com.iti.mansoura.tot.easytripplanner.home.history.map.Route> routes) {
        if (materialDialog != null && materialDialog.isShowing())
            materialDialog.dismiss();
        //if (!routes.isEmpty() && polyline != null) polyline.remove();
        try {
            for (Route route : routes) {
                PolylineOptions polylineOptions = getDefaultPolyLines(route.points);
                if (polylineStyle == PolylineStyle.DOTTED)
                    polylineOptions = getDottedPolylines(route.points);
                polyline = googleMap1.addPolyline(polylineOptions);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error occurred on finding the directions...", Toast.LENGTH_SHORT).show();
        }
        if(routes.size()>0) {
            googleMap1.setMinZoomPreference(6.0f);
            googleMap1.setMaxZoomPreference(8.0f);
            googleMap1.animateCamera(buildCameraUpdate(routes.get(0).endLocation),10, null);

        }
    }
}
