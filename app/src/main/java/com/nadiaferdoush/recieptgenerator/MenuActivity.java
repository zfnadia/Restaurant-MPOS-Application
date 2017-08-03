package com.nadiaFerdoush.recieptgenerator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loggedIn = pref.getBoolean("logged_in", false);

        if (!loggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
    }

    public void goToEditCategory(View v) {
        Intent intent = new Intent(this, CategoryItemActivity.class);
        startActivity(intent);
    }

    public void goToEditItem(View v) {
        Intent intent = new Intent(this, ItemActivity.class);
        startActivity(intent);
    }

    public void goToManageEmployee(View v) {
        Intent intent = new Intent(this, EmployeeInfoActivity.class);
        startActivity(intent);
    }

    public void logOut(View v){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("logged_in", false);
        editor.commit();
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goToAllBills(View v) {
        Intent intent = new Intent(this, AllBillsActivity.class);
        startActivity(intent);
    }
}
