package com.iti.mansoura.tot.easytripplanner.login;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.iti.mansoura.tot.easytripplanner.home.HomeActivity;

import androidx.annotation.NonNull;

public class LoginPresenter implements LoginContract.ILoginPresenter {
    private FirebaseAuth mAuth;
    private Login loginActivity;
    private Intent intentGoHome;

    public LoginPresenter(Login login){
        mAuth = FirebaseAuth.getInstance();
        loginActivity=login;
    }
    @Override
    public void onStart() {
    }

    @Override
    public void loginProcess(String mail, String password) {
        if(!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(mail,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            loginActivity.startActivity(new Intent(loginActivity.getApplicationContext(), HomeActivity.class));
                            loginActivity.finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(loginActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{
            loginActivity.showMsg("You can't let any failed empty");
            loginActivity.editText_Mail.setHintTextColor(Color.RED);
            loginActivity.editText_Pass.setHintTextColor(Color.RED);
        }
    }
}
