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
import android.widget.TextView;

import java.util.List;

public class CategoryItemActivity extends AppCompatActivity {

    ArrayAdapter<Category> mAdapter;
    private Category selectedItem;

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

        categoryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                    AppDatabase database = AppDatabase.getInstance(CategoryItemActivity.this);
                    database.getWritableDatabase().delete("item_category", "id = ?", new String[]{String.valueOf(selectedItem.id)});
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
