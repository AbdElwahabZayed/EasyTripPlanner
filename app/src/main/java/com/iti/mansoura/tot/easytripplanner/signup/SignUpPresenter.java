package com.iti.mansoura.tot.easytripplanner.signup;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.iti.mansoura.tot.easytripplanner.home.HomeActivity;
import com.iti.mansoura.tot.easytripplanner.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;

import java.util.UUID;

public class SignUpPresenter implements SignUpContract.ISignUpPresenter{
    private FirebaseDatabase database;
    SignUp signUpActivity;
    private FirebaseAuth mAuth;
    public SignUpPresenter(SignUp signUp) {
        signUpActivity=signUp;

        database = FirebaseDatabase.getInstance();
        mAuth =FirebaseAuth.getInstance();
    }

    @Override
    public void signUpProcess(String email, String userName, String pass1, String pass2) {
        final DatabaseReference reference = database.getReference();
        if(TextUtils.equals(pass1,pass2)){
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(pass1) && !TextUtils.isEmpty(pass2) ){
                    //should check if the username and email is valid
                    //should check if user is already signed up
                    //should check is already taken
                mAuth.createUserWithEmailAndPassword(email,pass1)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                signUpActivity.startActivity(new Intent(signUpActivity.getBaseContext(), HomeActivity.class));
                                signUpActivity.finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        signUpActivity.showMsg(e.getMessage());
                    }
                });

                // UUID >> unique user id
//                User newUser =new User(email,userName,pass1, UUID.randomUUID().toString());

//                User newUser =new User(email,userName,pass1);
//                reference.child("Users").push().setValue(newUser)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                signUpActivity.showMsg("added");
//                                signUpActivity.startActivity(new Intent(signUpActivity.getBaseContext(), HomeActivity.class));
//                                signUpActivity.finish();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        signUpActivity.showMsg(e.getMessage());
//                    }
//                });
            }else {
                signUpActivity.showMsg("You can't let any failed empty");
            }
        }else{
            signUpActivity.showMsg("The Two Passwords Are Not Identical");
        }
    }
}
