package com.nadiaFerdoush.recieptgenerator;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EmployeeInfoActivity extends AppCompatActivity {

    ArrayAdapter<Employee> mAdapter;
    private Employee selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppDatabase db = AppDatabase.getInstance(this);

        List<Employee> employees = db.getEmployees();

        ListView employeeList = (ListView) findViewById(R.id.category_list_view);
        mAdapter = new EmployeeListAdapter(this, R.layout.employee_list, employees);
        employeeList.setAdapter(mAdapter);

        employeeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActionMode != null)
                    mActionMode.finish();
                startSupportActionMode(mActionModeCallback);
                selectedItem = mAdapter.getItem(position);
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNameDialog();
            }

        });
    }

    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mActionMode = mode;
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    AppDatabase database = AppDatabase.getInstance(EmployeeInfoActivity.this);
                    database.getWritableDatabase().delete("employee", "id = ?", new String[]{String.valueOf(selectedItem.id)});
                    mAdapter.remove(selectedItem);
                    mAdapter.notifyDataSetChanged();
                    if (mActionMode != null)
                        mActionMode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //mActionMode = null;
        }
    };

    private void showNameDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View v = LayoutInflater.from(this).inflate(R.layout.employee_info_input_form, null);


        final Spinner spinner = (Spinner) v.findViewById(R.id.select_category);

        List<String> jobTypes = new ArrayList<String>();
        jobTypes.add("Select employee type");
        jobTypes.add("Manager");
        jobTypes.add("Cashier");
        jobTypes.add("Waiter");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, jobTypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        builder.setTitle("Create New Employee");
        builder.setView(v);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = ((EditText) v.findViewById(R.id.editName)).getText().toString();
                String birthDate = ((EditText) v.findViewById(R.id.editBirthDate)).getText().toString();
                String address = ((EditText) v.findViewById(R.id.editAddress)).getText().toString();
                String email = ((EditText) v.findViewById(R.id.editEmail)).getText().toString();
                String phoneNumber = ((EditText) v.findViewById(R.id.editPhoneNumber)).getText().toString();
                int salary = Integer.parseInt(((EditText) v.findViewById(R.id.editSalary)).getText().toString());
                String password = ((EditText) v.findViewById(R.id.editPhoneNumber)).getText().toString();

                int selected = spinner.getSelectedItemPosition();

                if (name.length() > 0 && birthDate.length() > 0 && address.length() > 0
                        && email.length() > 0 && phoneNumber.length() > 0 && salary > 0 && password.length() > 0 && selected > 0 ) {
                    AppDatabase db = AppDatabase.getInstance(EmployeeInfoActivity.this);
                    Employee employee= new Employee(name, birthDate, address, email, password, phoneNumber, salary, selected - 1);
                    db.insertEmployee(employee);
                    mAdapter.add(employee);
                    mAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });

        //builder.create().show();

    }

    static class EmployeeListAdapter extends ArrayAdapter<Employee> {

        public EmployeeListAdapter(Context context, int resource, List<Employee> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.employee_list, null);

            Employee employee = getItem(position);
            TextView employeeNameView = (TextView) v.findViewById(R.id.tv_employee_name);
            employeeNameView.setText(employee.getName());

            TextView employeeTypeView = (TextView) v.findViewById(R.id.tv_employee_type);
            employeeTypeView.setText(new String[]{"Manager", "Cashier", "Waiter"}[employee.getType()]);

            return v;
        }
    }


}
