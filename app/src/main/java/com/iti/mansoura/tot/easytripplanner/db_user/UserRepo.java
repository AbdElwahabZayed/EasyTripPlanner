package com.iti.mansoura.tot.easytripplanner.db_user;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.mansoura.tot.easytripplanner.models.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class UserRepo {
    private UserDataBase userDataBase;
    private UserDao userDao;
    private FirebaseAuth mAuth;
    private User user;
    public UserRepo(){

        userDataBase=UserDataBase.getDataBaseInstance();
        userDao=userDataBase.getDaoInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void addUser(User user){
        new AddUser().execute(user);
    }
    public LiveData<User> getUserLiveData(String uid){
        return userDao.getUserLiveData(uid);
    }
    private class AddUser extends AsyncTask<User,Void,Void> {
        @Override
        protected Void doInBackground(final User... users) {
            userDao.addUser(users[0]);
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Users").child(users[0].getUuid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    reference.child("Users").child(users[0].getUuid()).setValue(users[0]);
                    System.out.println("reference.child(\"Users\").child(users[0].getUuid()).setValue(users[0]);");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return null;
        }
    }

    public User getUser(String uid){
        user=userDao.getUser(uid);
        //System.out.println("user.getEmail()"+user.getEmail());
        if(user==null || user.getEmail().isEmpty() || user.getUserName().isEmpty()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren())
                    {
                        final User user1 = childDataSnapshot.getValue(User.class);
                        //System.out.println("UserName : "+user1.getUserName());
                        System.out.println(user1.getUuid()+"    <---->   "+mAuth.getUid());
                        if (user1.getUuid().equals(mAuth.getUid())){
                            System.out.println("hgjhkl;l;lgkj   "+user1.getUserName());
                            user=user1;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    deleteUser(user1);
                                    addUser(user1);
                                }
                            }).start();
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("upComing " , "error"+databaseError.getMessage());
                }
            });
        }

        return user;
    }
    void deleteUser(User u){
        new DeleteUser().execute(u);

    }

    private class DeleteUser extends AsyncTask<User,Void,Void>{
        @Override
        protected Void doInBackground(User... users) {
            userDao.deletUser(users[0]);
            return null;
        }
    }
}
