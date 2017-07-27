package com.nadiaferdoush.recieptgenerator;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class ItemActivity extends AppCompatActivity {

    ArrayAdapter<Item> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppDatabase db = AppDatabase.getInstance(this);

        List<Item> item = db.getItems(null);

        ListView itemList = (ListView) findViewById(R.id.category_list_view);
        mAdapter = new ItemListAdapter(this, R.layout.item_list, item);
        itemList.setAdapter(mAdapter);

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

        final View v = LayoutInflater.from(this).inflate(R.layout.item_input_form, null);


        final Spinner spinner = (Spinner) v.findViewById(R.id.select_category);
        AppDatabase db = AppDatabase.getInstance(this);
        List<Category> categories = db.getCategoryItems();

        Category cat = new Category("Select Category", "");
        cat.id = 0;
        categories.add(0, cat);

        ArrayAdapter<Category> spinnerAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        builder.setTitle("Create New Item");
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

                String name = ((EditText) v.findViewById(R.id.editItemName)).getText().toString();
                String price = ((EditText) v.findViewById(R.id.editItemPrice)).getText().toString();
                String description = ((EditText) v.findViewById(R.id.editItemDescription)).getText().toString();

                Category selected = (Category) spinner.getSelectedItem();

                if (name.length() > 0 && price.length() > 0 && description.length() > 0 && selected.id > 0) {
                    AppDatabase db = AppDatabase.getInstance(ItemActivity.this);
                    Item item = new Item(name, Float.parseFloat(price), description);
                    db.insertItems(item);
                    mAdapter.add(item);
                    mAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });

    }

    static class ItemListAdapter extends ArrayAdapter<Item> {

        public ItemListAdapter(Context context, int resource, List<Item> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.item_list, null);

            Item item = getItem(position);
            TextView itemNameView = (TextView) v.findViewById(R.id.tv_item_name);
            itemNameView.setText(item.getName());

            TextView itemPriceView = (TextView) v.findViewById(R.id.tv_item_price);
            itemPriceView.setText("BDT " + String.valueOf(item.getPrice()));

            TextView itemDescriptionView = (TextView) v.findViewById(R.id.tv_item_description);
            itemDescriptionView.setText(item.getDescription());
            return v;
        }
    }


}
