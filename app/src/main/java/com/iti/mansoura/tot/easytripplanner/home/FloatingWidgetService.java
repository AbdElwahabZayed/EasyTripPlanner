package com.iti.mansoura.tot.easytripplanner.home;


import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andremion.counterfab.CounterFab;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.trip.steps.TripNotesStep;

/**
 * Created by anupamchugh on 01/08/17.
 */

public class FloatingWidgetService extends Service {


    private WindowManager mWindowManager;
    private View mOverlayView;
    private int mWidth;
    private CounterFab counterFab;
    boolean activity_background;
    AppCompatActivity mAppCompatActivity;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            activity_background = intent.getBooleanExtra("activity_background", false);

        }

        // check if permission allowed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {

                if (mOverlayView == null) {

                    mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);

                    final WindowManager.LayoutParams params;

                    // for oreo and later APIs TYPE_APPLICATION_OVERLAY
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        params = new WindowManager.LayoutParams(
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                PixelFormat.TRANSLUCENT);
                        //Specify the view position
                        params.gravity = Gravity.TOP | Gravity.LEFT; //Initially view will be added to top-left corner
                        params.x = 0;
                        params.y = 100;
                    } else {
                        // for Old APIs TYPE_PHONE
                        params = new WindowManager.LayoutParams(
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.WRAP_CONTENT,
                                WindowManager.LayoutParams.TYPE_PHONE,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                PixelFormat.TRANSLUCENT);
                        //Specify the view position
                        params.gravity = Gravity.TOP | Gravity.LEFT; //Initially view will be added to top-left corner
                        params.x = 0;
                        params.y = 100;
                    }

                    mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

                    mWindowManager.addView(mOverlayView, params);

                    Display display = mWindowManager.getDefaultDisplay();
                    final Point size = new Point();
                    display.getSize(size);

                    counterFab = mOverlayView.findViewById(R.id.fabHead);
                    counterFab.setCount(intent.getIntExtra("notesCount",1));
                    final RelativeLayout layout = mOverlayView.findViewById(R.id.layout);
                    ViewTreeObserver vto = layout.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            int width = layout.getMeasuredWidth();

                            //To get the accurate middle of the screen we subtract the width of the floating widget.
                            mWidth = size.x - width;

                        }
                    });


                    counterFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    counterFab.setOnTouchListener(new View.OnTouchListener() {
                        private int initialX;
                        private int initialY;
                        private float initialTouchX;
                        private float initialTouchY;

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    //remember the initial position.
                                    initialX = params.x;
                                    initialY = params.y;


                                    //get the touch location
                                    initialTouchX = event.getRawX();
                                    initialTouchY = event.getRawY();


                                    return true;
                                case MotionEvent.ACTION_UP:

                                    //Only start the activity if the application is in background. Pass the current badge_count to the activity
                                    if (activity_background) {

                                        float xDiff = event.getRawX() - initialTouchX;
                                        float yDiff = event.getRawY() - initialTouchY;

                                        if ((Math.abs(xDiff) < 5) && (Math.abs(yDiff) < 5)) {
                                            Intent intent = new Intent(FloatingWidgetService.this, TripNotesStep.class);
                                            intent.putExtra("badge_count", counterFab.getCount());
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            String[] multiChoiceItems = new String[]{"Yes I does ","No you doesnt","yes he do","no she dont","","","","","","","","",""};
                                            //getResources().getStringArray(R.array.dialog_multi_choice_array);
                                            final boolean[] checkedItems = {false, false, false, false ,false ,false ,false , false, false,false,false, false, false};
                                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch(which){
                                                        case DialogInterface.BUTTON_POSITIVE:
                                                            // User clicked the Yes button
                                                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(getApplicationContext())) {
                                                                startService(new Intent(getApplicationContext(), FloatingWidgetService.class).putExtra("activity_background", true));
                                                            }
                                                            break;

                                                        case DialogInterface.BUTTON_NEGATIVE:
                                                            // User clicked the No button
                                                            break;
                                                    }
                                                }
                                            };
                                            AlertDialog alertDialog=  new AlertDialog.Builder(FloatingWidgetService.this)
                                                    .setTitle("Your Notes")
                                                    .setMultiChoiceItems(multiChoiceItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int index, boolean isChecked) {
                                                            Log.d("MainActivity", "clicked item index is " + index);
                                                        }
                                                    })
                                                    .setPositiveButton("Continue Trip", dialogClickListener)
                                                    .setNegativeButton("Close Widget", dialogClickListener)
                                                    .create();
                                            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                            alertDialog.show();

                                            stopSelf();

                                        }

                                    }
                                    //Logic to auto-position the widget based on where it is positioned currently w.r.t middle of the screen.
                                    int middle = mWidth / 2;
                                    float nearestXWall = params.x >= middle ? mWidth : 0;
                                    params.x = (int) nearestXWall;


                                    mWindowManager.updateViewLayout(mOverlayView, params);


                                    return true;
                                case MotionEvent.ACTION_MOVE:


                                    int xDiff = Math.round(event.getRawX() - initialTouchX);
                                    int yDiff = Math.round(event.getRawY() - initialTouchY);


                                    //Calculate the X and Y coordinates of the view.
                                    params.x = initialX + xDiff;
                                    params.y = initialY + yDiff;

                                    //Update the layout with new X & Y coordinates
                                    mWindowManager.updateViewLayout(mOverlayView, params);


                                    return true;
                            }
                            return false;
                        }
                    });
                }
                else {

                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.AppTheme);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOverlayView != null)
            mWindowManager.removeView(mOverlayView);
    }
}
