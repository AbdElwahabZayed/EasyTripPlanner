package com.iti.mansoura.tot.easytripplanner.signup;

public interface SignUpContract {
    interface SignUpView{
        void initComponent();
        void showMsg(String msg);

    }
    interface ISignUpPresenter{
        void signUpProcess(String email, String userName,String pass1,String pass2);


    }
}
