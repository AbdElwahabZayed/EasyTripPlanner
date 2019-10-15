package com.iti.mansoura.tot.easytripplanner.login;

public interface LoginContract {
    interface LoginView{
        void initComponent();
        void showMsg(String msg);

    }
    interface ILoginPresenter{
        void loginProcess(String s, String toString);
        void onStart();

    }
}
