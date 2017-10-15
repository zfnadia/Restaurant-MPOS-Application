package com.nadiaFerdoush.recieptgenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }


    public void addRegisterClicked(View v){
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    public void addLogInClicked(View v){
        startActivity(new Intent(this, LoginActivity.class));
    }

}
