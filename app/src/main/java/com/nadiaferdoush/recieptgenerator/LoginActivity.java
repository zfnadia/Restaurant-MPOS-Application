package com.nadiaFerdoush.recieptgenerator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText editUserName;
    EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        editUserName = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase database = AppDatabase.getInstance(LoginActivity.this);
//                database.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM employee  WHERE email = ? AND password = ?",
//                        new String[]{editUserName.getText().toString(), editPassword.getText().toString()});

                long count = DatabaseUtils.queryNumEntries(database.getReadableDatabase(), "employee", "email = ? AND password = ?", new String[]{editUserName.getText().toString(), editPassword.getText().toString()});
                if (count > 0){
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                    editor.putBoolean("logged_in", true);
                    editor.commit();
                    finish();
                    startActivity(new Intent(LoginActivity.this, BillActivity.class));
                }
                else {
                    Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
