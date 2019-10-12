package com.example.zayed.easytripplanner.loginpkg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zayed.easytripplanner.R;
import com.example.zayed.easytripplanner.signuppkg.SignUp;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity implements LoginContract.LoginView {
    LoginPresenter loginPresenter;
    Button btn_Login;
    EditText editText_Mail;
    EditText editText_Pass;
    FirebaseAuth mAuth;
    Button btn_SignUP;
    ImageView imgView_Logo_Login;
    private RotateAnimation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth=FirebaseAuth.getInstance();

        initComponent();
    }

    @Override
    public void initComponent() {
        loginPresenter=new LoginPresenter(this);
        editText_Mail=findViewById(R.id.editText_Mail_Sign);
        editText_Pass=findViewById(R.id.editText_Pass2_Sign);
        btn_Login=findViewById(R.id.btn_SignUP);
        imgView_Logo_Login=findViewById(R.id.imgView_Logo_login);

        rotate = new RotateAnimation(0, 10, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setRepeatCount(5);
        rotate.setRepeatMode(Animation.REVERSE);
        rotate.setStartTime(10000);
        rotate.setDuration(40);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setStartTime(10000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgView_Logo_Login.startAnimation(rotate);
            }
        },800);
    }


    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.onStart();

    }


    @Override
    public void showMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_LONG).show();
    }


    public void btnLoginClick(View view) {
        loginPresenter.loginProcess(editText_Mail.getText().toString(),
                                    editText_Pass.getText().toString());
    }

    public void txtPassClick(View view) {
        editText_Mail.setHintTextColor(Color.WHITE);
        editText_Pass.setHintTextColor(Color.WHITE);
    }

    public void txtEmailClick(View view) {
        editText_Mail.setHintTextColor(Color.WHITE);
        editText_Pass.setHintTextColor(Color.WHITE);
    }

    public void btn_signUp(View view) {
        startActivity(new Intent(this, SignUp.class));
    }
}
