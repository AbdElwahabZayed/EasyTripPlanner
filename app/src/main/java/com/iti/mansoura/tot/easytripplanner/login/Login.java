package com.iti.mansoura.tot.easytripplanner.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.db_user.UserDataBase;
import com.iti.mansoura.tot.easytripplanner.signup.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity implements LoginContract.LoginView {
    LoginPresenter loginPresenter;
    Button btn_Login;
    EditText editText_Mail;
    EditText editText_Pass;
    FirebaseAuth mAuth;
    Button btn_SignUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth=FirebaseAuth.getInstance();
        UserDataBase.dbcontext=this;
        initComponent();
    }

    @Override
    public void initComponent() {
        loginPresenter=new LoginPresenter(this);
        editText_Mail=findViewById(R.id.editText_Mail_Sign);
        editText_Pass=findViewById(R.id.editText_Pass2_Sign);
        btn_Login=findViewById(R.id.btn_Login);

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

    public void forgot_Password(View view) {
        if(!editText_Mail.getText().toString().isEmpty()) {
            mAuth.sendPasswordResetEmail(editText_Mail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            showMsg("Check your Email");
                        }

                    });

        }else{
            showMsg("Enter Your email first");
        }
    }
}
