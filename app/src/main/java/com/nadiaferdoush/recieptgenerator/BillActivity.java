package com.nadiaFerdoush.recieptgenerator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BillActivity extends AppCompatActivity {

    GridView itemGridView;
    ArrayAdapter<Item> mAdapter;
    TagAdapter<Item> addedItemsAdapter;
    List<Item> mAddedItems = new ArrayList<>();
    Map<Integer, Item> mAddedItemsMap = new HashMap<>();
    float netAmount;
    TextView mNetItemPriceView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loggedIn = pref.getBoolean("logged_in", false);
        /*
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("logged_in", true);
        */

        if (!loggedIn){

            AppDatabase db = AppDatabase.getInstance(this);
            long count = DatabaseUtils.queryNumEntries(db.getReadableDatabase(), "employee");
            if (count > 0){
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                startActivity(new Intent(this, CreateAccountActivity.class));
            }
            finish();
        }

        itemGridView = (GridView) findViewById(R.id.grid);
        mNetItemPriceView = (TextView) findViewById(R.id.net_item_price);

        /*

        - check if logged in, if yes, do nothing
        - if no
        - if employee count > 0, if yes, finish this activity, and show login activity
        - if no, show employee add activity


         */

        // Selected items display
        TagFlowLayout itemList = (TagFlowLayout) findViewById(R.id.id_flowLayout);
        addedItemsAdapter = new ItemListAdapter(this, mAddedItems);
        itemList.setAdapter(addedItemsAdapter);

        itemList.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                Item item = addedItemsAdapter.getItem(position);
                if (item.count > 1) {
                    item.count--;
                } else {
                    mAddedItemsMap.remove(item.id);
                }

                Collection<Item> items = mAddedItemsMap.values();
                refreshNetPrice(items);

                mAddedItems.clear();
                mAddedItems.addAll(items);
                addedItemsAdapter.notifyDataChanged();

                mAdapter.notifyDataSetChanged();

                return false;
            }
        });

        final AppDatabase db = AppDatabase.getInstance(this);

        // All items grid display
        itemGridView = (GridView) findViewById(R.id.grid);
        mAdapter = new ItemGridAdapter(this, R.layout.bill_grid_item, new ArrayList<Item>());
        itemGridView.setAdapter(mAdapter);

        itemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = mAdapter.getItem(i);  // clicked item

                Item added = mAddedItemsMap.get(item.id); //

                if (added == null) {
                    item.count = 1;
                    mAddedItemsMap.put(item.id, item);
                } else {
                    added.count++;
                }

                Collection<Item> items = mAddedItemsMap.values();
                mAddedItems.clear();
                mAddedItems.addAll(items);
                refreshNetPrice(items);
                addedItemsAdapter.notifyDataChanged();

                mAdapter.notifyDataSetChanged();

            }

        });


        searchView = (SearchView) findViewById(R.id.search_menu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Item> items = db.getItems(newText);
                mAdapter.clear();
                mAdapter.addAll(items);
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        final AppDatabase db = AppDatabase.getInstance(this);
        String text = searchView.getQuery().toString();
        List<Item> items = db.getItems(text.length() > 0 ? text : null);
        mAdapter.clear();
        mAdapter.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    @SuppressLint("DefaultLocale")
    private void refreshNetPrice(Collection<Item> items) {
        float netItemPrice = 0;
        for (Item item : items) {
            netItemPrice += item.count * item.getPrice();
        }

        netAmount = netItemPrice;
        mNetItemPriceView.setText(String.format("%.2f", netItemPrice));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_manage_menu) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ItemGridAdapter extends ArrayAdapter<Item> {

        public ItemGridAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Item> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.bill_grid_item, null);

            Item item = getItem(position);
            TextView topTextView = (TextView) v.findViewById(R.id.grid_top_text);
            topTextView.setText(item.getName());

//          TextView itemImageView = (TextView) v.findViewById(R.id.grid_image);
//          itemImageView.setText(item.getImage());

            TextView countView = (TextView) v.findViewById(R.id.grid_item_count);
            Item addedItem = mAddedItemsMap.get(item.id);
            if (addedItem == null) {
                countView.setVisibility(View.GONE);
            } else {
                countView.setVisibility(View.VISIBLE);
                countView.setText(String.valueOf(addedItem.count));
            }

            TextView bottomTextView = (TextView) v.findViewById(R.id.grid_bottom_text);
            bottomTextView.setText(String.valueOf(item.getPrice()));
            return v;
        }
    }

    class ItemListAdapter extends TagAdapter<Item> {

        Context context;

        public ItemListAdapter(Context context, List<Item> datas) {
            super(datas);
            this.context = context;
        }

        @Override
        public View getView(FlowLayout parent, int position, Item item) {
            View v = LayoutInflater.from(this.context).inflate(R.layout.flowlayout_item, null);

            TextView itemNameView = (TextView) v.findViewById(R.id.item_name);
            itemNameView.setText(item.getName());

            TextView itemCountView = (TextView) v.findViewById(R.id.item_count);
            itemCountView.setText(String.valueOf(item.count));

            return v;
        }
    }

    public void goToAdditionalBillInfo(View v) {
        showNameDialog();
    }

    private void showOpenBillDialog(final int billId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Bill #" + billId);
        builder.setMessage(Html.fromHtml("Bill <b>#" + billId + "</b>"));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Intent invoiceIntent = new Intent(BillActivity.this, InvoiceActivity.class);
                invoiceIntent.putExtra("bill_id", billId);
                startActivity(invoiceIntent);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        }).create().show();


    }

    private void showNameDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View v = LayoutInflater.from(this).inflate(R.layout.additional_bill_info, null);


        final Spinner spinner = (Spinner) v.findViewById(R.id.select_payment_method);

        List<String> paymentTypes = new ArrayList<String>();
        paymentTypes.add("Select Payment Method");
        paymentTypes.add("Cash");
        paymentTypes.add("Cheque");
        paymentTypes.add("Credit Card");
        paymentTypes.add("Debit Card");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, paymentTypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        builder.setTitle("Fill the following fields");
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

                float paidAmount = Float.parseFloat(((EditText) v.findViewById(R.id.editPaidAmount)).getText().toString());
                int tableNumber = Integer.parseInt(((EditText) v.findViewById(R.id.editTableNumber)).getText().toString());
                String waiter = ((EditText) v.findViewById(R.id.editServerName)).getText().toString();
                float discountPt = Float.parseFloat(((EditText) v.findViewById(R.id.editDiscount)).getText().toString());

                float vatPt = 15;
                float vat = (vatPt / 100);
                float discount = (discountPt / 100);
                float afterVat = vat * netAmount;
                float afterDiscount = discount * netAmount;
                float grossAmount = (netAmount - afterDiscount) + afterVat;

                float changeAmount = paidAmount - grossAmount;

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = mdFormat.format(calendar.getTime());
                String timeCreated = strDate;

                int selected = spinner.getSelectedItemPosition();
                int paymentType = selected;

                if (paidAmount > 0 && tableNumber > 0 && waiter.length() > 0
                        && discountPt >= 0 && selected > 0) {
                    AppDatabase db = AppDatabase.getInstance(BillActivity.this);

                    Bill bill = new Bill(grossAmount, paidAmount, netAmount, changeAmount, vatPt, discountPt, timeCreated,
                            tableNumber, paymentType);
                    int billId = (int) db.insertInBill(bill);

                    for (Item item : mAddedItemsMap.values()) {
                        db.insertBillItem(billId, item);
                    }

                    dialog.dismiss();

                    showOpenBillDialog(billId);
                }
            }
        });
    }
}
