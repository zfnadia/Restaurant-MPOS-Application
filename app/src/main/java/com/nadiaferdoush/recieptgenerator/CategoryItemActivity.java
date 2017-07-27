package com.nadiaferdoush.recieptgenerator;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class CategoryItemActivity extends AppCompatActivity {

    ArrayAdapter<Category> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppDatabase db = AppDatabase.getInstance(this);

        List<Category> categoryItems = db.getCategoryItems();

        ListView categoryList = (ListView) findViewById(R.id.category_list_view);
        mAdapter = new CategoryListAdapter(this, R.layout.category_list, categoryItems);
        categoryList.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNameDialog();
            }
        });
    }

    private void showNameDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View v = LayoutInflater.from(this).inflate(R.layout.category_input_form, null);

        builder.setTitle("Create New Category");
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

                String name = ((EditText) v.findViewById(R.id.editCategoryName)).getText().toString();
                String time = ((EditText) v.findViewById(R.id.editCategoryTime)).getText().toString();
                if (name.length() > 0 && time.length() > 0) {
                    AppDatabase db = AppDatabase.getInstance(CategoryItemActivity.this);
                    Category category = new Category(name, time);
                    db.insertCategoryItems(category);
                    mAdapter.add(category);
                    mAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });

    }

    class CategoryListAdapter extends ArrayAdapter<Category> {

        public CategoryListAdapter(Context context, int resource, List<Category> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.category_list, null);

            Category category = getItem(position);
            TextView categoryNameView = (TextView) v.findViewById(R.id.tv_category_name);
            categoryNameView.setText(category.getName());

            TextView timeView = (TextView) v.findViewById(R.id.tv_category_time);
            timeView.setText(category.getTime());

            return v;
        }
    }
}
