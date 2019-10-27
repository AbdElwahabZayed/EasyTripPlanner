package com.iti.mansoura.tot.easytripplanner.home.history.map;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GoogleMapHelper {

    private static final int ZOOM_LEVEL = 18;
    private static final int TILT_LEVEL = 25;
    private static final int PATTERN_GAP_LENGTH_PX = 10;
    private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final Dot DOT = new Dot();
    private static final List<PatternItem> PATTERN_DOTTED = Arrays.asList(DOT, GAP);

    public static PolylineOptions getDefaultPolyLines(List<LatLng> points) {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        int randomColor = Color.rgb(r,g,b);
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(randomColor);
        for (LatLng point : points) polylineOptions.add(point);
        return polylineOptions;
    }

    public static PolylineOptions getDottedPolylines(List<LatLng> points) {
        PolylineOptions polylineOptions = getDefaultPolyLines(points);
        polylineOptions.pattern(PATTERN_DOTTED);
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        int randomColor = Color.rgb(r,g,b);
        polylineOptions.color(randomColor);
        return polylineOptions;
    }

    public static void defaultMapSettings(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.setBuildingsEnabled(true);
    }

    public static CameraUpdate buildCameraUpdate(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .tilt(TILT_LEVEL)
                .zoom(ZOOM_LEVEL)
                .build();
        return CameraUpdateFactory.newCameraPosition(cameraPosition);
    }

}