package com.oneteam.graduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Before starting the LoginActivity check if the user is authorized or not

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
