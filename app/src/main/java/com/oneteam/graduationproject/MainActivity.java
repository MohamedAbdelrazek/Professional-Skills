package com.oneteam.graduationproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
   private UserSession zUserSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zUserSession = new UserSession(this);
        // checking if the user already has email and password
        //if there is no data this func will launch the login activity && if not it does nothing 
        zUserSession.checkLogin();

    }
}
