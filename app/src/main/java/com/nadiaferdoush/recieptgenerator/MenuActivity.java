package com.nadiaFerdoush.recieptgenerator;

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
        startActivity(new Intent(this, LoginActivity.class));
    }
}
