package com.iti.mansoura.tot.easytripplanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iti.mansoura.tot.easytripplanner.home.HomeActivity;
import com.iti.mansoura.tot.easytripplanner.login.Login;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        //to grant the permission.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser==null) {
                    startActivity(new Intent(MainActivity.this, Login.class));
                }else{
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
                finish();
            }
        },500);
    }
}
