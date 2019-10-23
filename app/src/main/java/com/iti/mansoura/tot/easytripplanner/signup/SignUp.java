package com.iti.mansoura.tot.easytripplanner.signup;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iti.mansoura.tot.easytripplanner.R;
import com.iti.mansoura.tot.easytripplanner.db_user.UserDataBase;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity implements SignUpContract.SignUpView {
    SignUpPresenter signUpPresenter;
    Button btn_SignUp;
    EditText editText_Mail_SignUp;
    EditText editText_Pass1_SignUp;
    EditText editText_Pass2_SignUp;
    EditText editText_UserName_SignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        UserDataBase.dbcontext=this;
        initComponent();
    }

    @Override
    public void initComponent() {
        signUpPresenter=new SignUpPresenter(this,getApplicationContext());
        btn_SignUp=findViewById(R.id.btn_SignUP);
        editText_Mail_SignUp=findViewById(R.id.editText_Mail_Sign);
        editText_Pass1_SignUp=findViewById(R.id.editText_Pass1_Sign);
        editText_Pass2_SignUp=findViewById(R.id.editText_Pass2_Sign);
        editText_UserName_SignUp=findViewById(R.id.editText_UserName_Sign);

    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_LONG).show();
    }


    public void txtEmailClick_Sign(View view) {
        editText_Mail_SignUp.setHintTextColor(Color.WHITE);
        editText_UserName_SignUp.setHintTextColor(Color.WHITE);
        editText_Pass1_SignUp.setHintTextColor(Color.WHITE);
        editText_Pass2_SignUp.setHintTextColor(Color.WHITE);
    }

    public void txtPassClick_Sign(View view) {
        editText_Mail_SignUp.setHintTextColor(Color.WHITE);
        editText_UserName_SignUp.setHintTextColor(Color.WHITE);
        editText_Pass1_SignUp.setHintTextColor(Color.WHITE);
        editText_Pass2_SignUp.setHintTextColor(Color.WHITE);
    }

    public void btnSignClick(View view) {
        signUpPresenter.signUpProcess(editText_Mail_SignUp.getText().toString(),
                                      editText_UserName_SignUp.getText().toString(),
                                      editText_Pass1_SignUp.getText().toString(),
                                      editText_Pass2_SignUp.getText().toString());
    }

    public void txtUserNameClick_Sign(View view) {
        editText_Mail_SignUp.setHintTextColor(Color.WHITE);
        editText_UserName_SignUp.setHintTextColor(Color.WHITE);
        editText_Pass1_SignUp.setHintTextColor(Color.WHITE);
        editText_Pass2_SignUp.setHintTextColor(Color.WHITE);
    }
}
