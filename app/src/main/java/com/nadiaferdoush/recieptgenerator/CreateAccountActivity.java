package com.nadiaFerdoush.recieptgenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        findViewById(R.id.select_category).setVisibility(View.GONE);
    }

    public void goToAddManagerInfo(View v) {

        String name = ((EditText) findViewById(R.id.editName)).getText().toString();
        String birthDate = ((EditText) findViewById(R.id.editBirthDate)).getText().toString();
        String address = ((EditText) findViewById(R.id.editAddress)).getText().toString();
        String email = ((EditText) findViewById(R.id.editEmail)).getText().toString();
        String phoneNumber = ((EditText) findViewById(R.id.editPhoneNumber)).getText().toString();
        int salary = Integer.parseInt(((EditText) findViewById(R.id.editSalary)).getText().toString());
        String password = ((EditText) findViewById(R.id.editPhoneNumber)).getText().toString();

        if (name.length() > 0 && birthDate.length() > 0 && address.length() > 0
                && email.length() > 0 && phoneNumber.length() > 0 && salary > 0 && password.length() > 0) {
            AppDatabase db = AppDatabase.getInstance(CreateAccountActivity.this);
            Employee employee = new Employee(name, birthDate, address, email, password, phoneNumber, salary, 0);
            db.insertEmployee(employee);
            finish();

            startActivity(new Intent(this, LoginActivity.class));
        }

        else {
            startActivity(new Intent(this, WelcomeActivity.class));

        }

    }

}